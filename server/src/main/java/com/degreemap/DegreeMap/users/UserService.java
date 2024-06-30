package com.degreemap.DegreeMap.users;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    // TODO 1 add input validation
    // TODO 2 make sure that Users' passwords are not returned when returning bodies (only return their email and id)
    // TODO 3 add security headers
    private final PasswordEncoder passwordEncoder;

    /**
     * Meant to store mutable information about a user, that is, information the user can edit.
     * @param email The user's email
     * @param password The user's password
     */
    public record UserInfo(String email, String password) {}

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email '"+email+"' not found."));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id '"+id+"' not found."));
    }

    public User updateUser(Long id, UserInfo userInfo) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setEmail(userInfo.email());
                    existingUser.setPassword(passwordEncoder.encode(userInfo.password));
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new IllegalArgumentException("User with id '"+id+"' not found."));
    }

    public User deleteUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return user;
                })
                .orElseThrow(() -> new IllegalArgumentException("User with id '"+id+"' not found."));
        //TODO when users are associated with course catalogs and other pieces of data, delete all of that as well.
    }
}
