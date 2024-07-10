package com.degreemap.DegreeMap.auth;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<?> authenticateUser(@RequestParam String email,
                                              @RequestParam String password,
                                              HttpServletResponse response) {
        return ResponseEntity.ok(
                authService.getAccessTokenFromCredentials(
                        email, password, response
                )
        );
    }

    // Create a new User
    // curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d "email=EMAIl&password=PASSWORD" http://localhost:8080/api/auth/register
    // ^ On windows USE COMMAND PROMPT
    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@RequestParam String email,
                                             @RequestParam String password,
                                             HttpServletResponse response) {

        authService.registerUserWithoutLogin(email, password);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> getRefreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        return ResponseEntity.ok(
                authService.getAccessTokenFromRefreshToken(authHeader)
        );
    }

}
