package com.degreemap.DegreeMap.users;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.degreemap.DegreeMap.auth.JpaUserDetailsService;
import com.degreemap.DegreeMap.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.degreemap.DegreeMap.users.UserController.AuthResponse;
import com.degreemap.DegreeMap.utility.JwtUtil;

import java.util.Optional;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class,
        // Should this guy be mocked?
        JpaUserDetailsService.class})
public class UserAuthenticationTests {
    
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;
    
    @Test
    public void testAuthUserSuccess() throws Exception {
        String plainPassword = "Password123!";
        String hashedPassword = passwordEncoder.encode(plainPassword);
        User user = new User("test@example.com", hashedPassword);
        user.setId(1L);

        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + user.getEmail() + "\", \"password\":\"" + plainPassword + "\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(result -> assertTrue(passwordEncoder.matches(plainPassword, user.getPassword())));

        String expectedToken = JwtUtil.generateToken(user.getEmail());
        AuthResponse expectedResponse = new AuthResponse(expectedToken);

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + user.getEmail() + "\", \"password\":\"" + plainPassword + "\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.jwt").value(expectedResponse.jwt));
    }

    @Test
    public void testAuthUserFailureWrongPassword() throws Exception {
        String plainPassword = "Password123!";
        String hashedPassword = passwordEncoder.encode(plainPassword);
        User user = new User("test@example.com", hashedPassword);
        user.setId(1L);

        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.findByEmail(any(String.class))).willReturn(Optional.of(user));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + user.getEmail() + "\", \"password\":\"" + plainPassword + "\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(result -> assertTrue(passwordEncoder.matches(plainPassword, user.getPassword())));

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + user.getEmail() + "\", \"password\":\"" + "wrong password" + "\"}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAuthUserFailureEmailNotFound() throws Exception {
        mockMvc.perform(post("/api/users/login")
        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + "email that doesn't exist" + "\", \"password\":\"" + "..." + "\"}"))
            .andExpect(status().isNotFound());
    }

}
