package com.degreemap.DegreeMap.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository; 
    // @Autowired
    // private PasswordEncoder passwordEncoder;
    // TODO hash passwords 

    static class Request {
        public String email;
        public String password;
    }

    // Create a new User
    // curl -X POST -H "Content-Type: application/json" -d "{\"email\":\"email\", \"password\":\"password\"}" http://localhost:8080/api/users
    @PostMapping
    public User registerNewUser(@RequestBody Request postRequest) {
        User user = new User(postRequest.email, postRequest.password);
        return userRepository.save(user);
    }

    // http://localhost:8080/api/users
    @GetMapping
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    // http://localhost:8080/api/users/x
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id){
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody Request putRequest) {
        return userRepository.findById(id).map(user -> {
            user.setEmail(putRequest.email);
            user.setPasswordHash(putRequest.password);
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        userRepository.delete(user);
    }
}