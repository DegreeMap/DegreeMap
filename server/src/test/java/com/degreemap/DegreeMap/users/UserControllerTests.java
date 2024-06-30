package com.degreemap.DegreeMap.users;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import com.degreemap.DegreeMap.config.PasswordEncoderConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import(PasswordEncoderConfig.class)
public class UserControllerTests {
    
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserService userService;

    @Test
    public void testGetAllUsers() throws Exception{
        given(userService.getAllUsers()).willReturn(
                List.of(
        new User("getall0@example.com", "Password6666!"),

        new User("getall1@example.com", "Password6666!"),
        new User("getall2@example.com", "Password6666!")



                )
        );

        mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetUserById() throws Exception{
        User u = new User("getall1@example.com", passwordEncoder.encode("Password7777!"));
        u.setId(1L);
        given(userService.getUserById(1L)).willReturn(u);

        mockMvc.perform(get("/api/users/" + u.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(u.getEmail()))
                .andExpect(jsonPath("$.password").value(u.getPassword()));
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception{
        long id = 0;
        given(userService.getUserById(id))
                .willThrow(new IllegalArgumentException());

        mockMvc.perform(get("/api/users/" + id))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void testEditingUser() throws Exception {
        User u = new User("test@example.com", "Password7777!");
        u.setId(1L);

        String newEmail = "newEmail@fart.com";
        String newPassword = "passwordFart712!";

        given(userService.updateUser(eq(1L), any(UserService.UserInfo.class)))
                .will(invocation -> {
                    u.setEmail(newEmail);
                    u.setPassword(passwordEncoder.encode(newPassword));
                    return u;
                });

        mockMvc.perform(put("/api/users/" + u.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + newEmail + "\", \"password\":\"" + newPassword + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(newEmail))
                .andExpect(jsonPath("$.password").value(u.getPassword()));


    }

    @Test
    public void testEditingUserNotFound() throws Exception {
        long id = 0;

        String newEmail = "GAAAAAAAAAAAAAAAAAAAAAAAA@dad.com";
        String newPassword = "passwordDeNada";

        given(userService.updateUser(eq(id), any(UserService.UserInfo.class))
        ).willThrow(new IllegalArgumentException());

        mockMvc.perform(put("/api/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + newEmail + "\", \"password\":\"" + newPassword + "\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        long id = 0;
        given(userService.deleteUser(id))
                .willThrow(new IllegalArgumentException());

        mockMvc.perform(delete("/api/users/" + id))
                .andExpect(status().isNotFound());
    }
}
