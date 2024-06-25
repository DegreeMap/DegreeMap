package com.degreemap.DegreeMap.users;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.degreemap.DegreeMap.auth.AuthService;
import com.degreemap.DegreeMap.auth.JpaUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JpaUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService authService;

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
    // TODO: From the frontend, we'd send requests of type `application/x-www-form-urlencoded/' with
    // TODO: "email" and "password" fields.
    // TODO: I keep this here for now because it's outside the scope of what I'm doing rn.
    static class Request {
        public String email;
        public String password;
    }

    /*
     * Format for Authentication Responses being sent to frontend. 
     */
    // TODO: Phase this guy out in favor of AuthResponseDto
    static class AuthResponse {
        public String jwt;
        public AuthResponse(String jwt) {
            this.jwt = jwt;
        }
    }

    // Current error response example:
    // {
    //    "timestamp": "2024-06-25T03:31:22.876+00:00",
    //    "status": 409,
    //    "error": "Conflict",
    //    "path": "/api/users"
    // }
    // TODO: Change it to include the actual message
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Request loginRequest) {
        return ResponseEntity.ok(
                authService.getAccessTokenFromCredentials(
                        loginRequest.email, loginRequest.password
                )
        );
    }

    // Create a new User
    // curl -X POST -H "Content-Type: application/json" -d "{\"email\":\"email\", \"password\":\"password\"}" http://localhost:8080/api/users
    // ^ On windows USE COMMAND PROMPT
    @PostMapping
    public ResponseEntity<?> registerNewUser(@RequestBody Request postRequest) throws NoSuchAlgorithmException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        authService.registerUserAndGetAccessToken(
                                postRequest.email, postRequest.password
                        )
                );
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
                user.setPassword(passwordEncoder.encode(putRequest.password));
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