package com.degreemap.DegreeMap.users;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.degreemap.DegreeMap.utility.PasswordEncoderUtil;

@WebMvcTest(UserController.class)
public class UserAuthenticationTests {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;
    
    @Test
    public void testAuthUserSuccess() throws Exception {
        String plainPassword = "Password123!";
        String hashedPassword = PasswordEncoderUtil.hashPassword(plainPassword);
        User user = new User("test@example.com", hashedPassword);
        user.setId(1L);

        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.findByEmail(any(String.class))).willReturn(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + user.getEmail() + "\", \"password\":\"" + plainPassword + "\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(result -> assertTrue(PasswordEncoderUtil.matches(plainPassword, user.getPassword())));

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + user.getEmail() + "\", \"password\":\"" + plainPassword + "\"}"))
            .andExpect(status().isOk());
        //TODO confirm token is sent
    }

    @Test
    public void testAuthUserFailureWrongPassword() throws Exception {
        String plainPassword = "Password123!";
        String hashedPassword = PasswordEncoderUtil.hashPassword(plainPassword);
        User user = new User("test@example.com", hashedPassword);
        user.setId(1L);

        given(userRepository.save(any(User.class))).willReturn(user);
        given(userRepository.findByEmail(any(String.class))).willReturn(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + user.getEmail() + "\", \"password\":\"" + plainPassword + "\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(result -> assertTrue(PasswordEncoderUtil.matches(plainPassword, user.getPassword())));

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
