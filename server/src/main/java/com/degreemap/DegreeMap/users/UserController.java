package com.degreemap.DegreeMap.users;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.degreemap.DegreeMap.utility.PasswordEncoderUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository; 
    // @Autowired
    // private PasswordEncoder passwordEncoder;
    // TODO add input validation & make sure that Users' passwords are not returned when returning bodies (only return their email and id)

    static class Request {
        public String email;
        public String password;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Request loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email);
        if (user != null && PasswordEncoderUtil.matches(loginRequest.password, user.getPassword())) {
            return ResponseEntity.ok("User authenticated successfully");
        }
        else if (user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with email " + loginRequest.email);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
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