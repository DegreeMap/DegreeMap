package com.degreemap.DegreeMap.users;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

import com.degreemap.DegreeMap.auth.AuthService;
import com.degreemap.DegreeMap.auth.JpaUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTests {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private JpaUserDetailsService userDetailsService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private AuthService authService;

    @Test
    public void testGetAllUsers() throws Exception{
        User[] users = new User[3];
        users[0] = new User("getall0@example.com", "Password6666!");
        users[1] = new User("getall1@example.com", "Password6666!");
        users[2] = new User("getall2@example.com", "Password6666!");
        given(userRepository.save(any(User.class))).willReturn(users[0]); 
        given(userRepository.save(any(User.class))).willReturn(users[1]); 
        given(userRepository.save(any(User.class))).willReturn(users[2]);
        
        for(User user : users){
            mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"" + user.getEmail() + "\", \"password\":\"" + user.getPassword() + "\"}"))
                .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetUserById() throws Exception{
        User user1 = new User("getall1@example.com", "Password7777!");
        user1.setId(1L);
        given(userRepository.findById(1L)).willReturn(Optional.of(user1));

        mockMvc.perform(get("/api/users/" + user1.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("getall1@example.com"))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception{
        User user1 = new User("getall1@example.com", "Password7777!");
        user1.setId(1L);
        given(userRepository.findById(2L)).willReturn(Optional.of(user1));

        mockMvc.perform(get("/api/users/" + user1.getId()))
        .andExpect(status().isNotFound());
    }
    
    @Test
    public void testEditingUser() throws Exception {
        User user = new User("test@example.com", "Password7777!");
        user.setId(1L);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.save(any(User.class))).willReturn(user); 

        mockMvc.perform(put("/api/users/" + user.getId())
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"email\":\"" + "newEmail@fart.com" + "\", \"password\":\"" + "passwordFart712!" + "\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("newEmail@fart.com"))
            .andExpect(result -> assertTrue(passwordEncoder.matches("passwordFart712!", user.getPassword())));
    }

    @Test
    public void testEditingUserNotFound() throws Exception {
        User user = new User("test@example.com", "Password7777!");
        user.setId(1L);

        given(userRepository.findById(2L)).willReturn(Optional.of(user));
        given(userRepository.save(any(User.class))).willReturn(user); 

        mockMvc.perform(put("/api/users/" + user.getId())
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"email\":\"" + "newEmail@fart.com" + "\", \"password\":\"" + "passwordFart712!" + "\"}"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {
        User user = new User("test@example.com", "Password7777!");
        user.setId(1L);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        mockMvc.perform(delete("/api/users/" + user.getId()))
        .andExpect(status().isOk());

        verify(userRepository).delete(user);
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        User user = new User("test@example.com", "Password7777!");
        user.setId(1L);

        given(userRepository.findById(2L)).willReturn(Optional.of(user));

        mockMvc.perform(delete("/api/users/" + user.getId()))
        .andExpect(status().isNotFound());
    }
}
