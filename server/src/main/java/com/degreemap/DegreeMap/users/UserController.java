package com.degreemap.DegreeMap.users;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.degreemap.DegreeMap.auth.JpaUserDetailsService;
import com.degreemap.DegreeMap.auth.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.degreemap.DegreeMap.utility.JwtUtil;
import com.degreemap.DegreeMap.utility.PasswordEncoderUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JpaUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger log = LoggerFactory.getLogger(UserController.class);

    // @Autowired
    // private PasswordEncoder passwordEncoder;
    // TODO 1 add input validation 
    // TODO 2 make sure that Users' passwords are not returned when returning bodies (only return their email and id)
    // TODO 3 add security headers

    /*
     * Format for receiving requests from frontend.
     */
    // TODO: We can use @RequestParams arguments instead of making a new class for these simple requests.
    // TODO: I keep it here for now because it's outside the scope of what I'm doing rn.
    static class Request {
        public String email;
        public String password;
    }

    /*
     * Format for Authentication Responses being sent to frontend. 
     */
    static class AuthResponse {
        public String jwt;
        public AuthResponse(String jwt) {
            this.jwt = jwt;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Request loginRequest) {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(loginRequest.email);

            if (passwordEncoder.matches(loginRequest.password, user.getPassword())) {
                String jwtToken = JwtUtil.generateToken(user.getUsername());
                AuthResponse response = new AuthResponse(jwtToken);
                return ResponseEntity.ok(response);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with email " + loginRequest.email);
        }
    }

    // Create a new User
    // curl -X POST -H "Content-Type: application/json" -d "{\"email\":\"email\", \"password\":\"password\"}" http://localhost:8080/api/users
    // ^ On windows USE COMMAND PROMPT
    @PostMapping
    public ResponseEntity<?> registerNewUser(@RequestBody Request postRequest) throws NoSuchAlgorithmException {
        try {
            String hashedPassword = PasswordEncoderUtil.hashPassword(postRequest.password);
            User user = new User(postRequest.email, hashedPassword);
            User savedUser = userRepository.save(user);
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUser);
        }
        catch(DataIntegrityViolationException e){
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Registration failed: Duplicate email. Please use a different email.");
        }
    }

    // http://localhost:8080/api/users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userRepository.findAll());
    }

    // http://localhost:8080/api/users/x
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        try {
            return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Request putRequest) {
        try {
            return userRepository.findById(id).map(user -> {
                user.setEmail(putRequest.email);
                user.setPassword(PasswordEncoderUtil.hashPassword(putRequest.password));
                return ResponseEntity.ok(userRepository.save(user));
            }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        try {
            return userRepository.findById(id).map(user -> {
                userRepository.delete(user);
                return ResponseEntity.ok().build(); // .build() means no body
            }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  
        }
        //TODO when users are associated with course catalogs and other pieces of data, delete all of that as well.
    }
}