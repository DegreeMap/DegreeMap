package com.degreemap.DegreeMap.auth;

import com.degreemap.DegreeMap.users.UserController;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody UserController.Request req,
                                              HttpServletResponse response) {
        return ResponseEntity.ok(
                authService.getAccessTokenFromCredentials(
                        req.email, req.password, response
                )
        );
    }

    // Create a new User
    // curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d "email=EMAIl&password=PASSWORD" http://localhost:8080/api/auth/register
    // ^ On windows USE COMMAND PROMPT
    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@RequestBody UserController.Request req,
                                             HttpServletResponse response) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        authService.registerUserAndGetAccessToken(
                                req.email, req.password, response
                        )
                );
    }

}
