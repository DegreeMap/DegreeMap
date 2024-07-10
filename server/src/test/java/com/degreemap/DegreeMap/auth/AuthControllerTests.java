package com.degreemap.DegreeMap.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.degreemap.DegreeMap.auth.jwt.AuthResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.server.ResponseStatusException;

@WebMvcTest(AuthController.class)
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    public void testRegisterNewUser() throws Exception {
        String email = "u1@goop.com";
        String password = "thepass";

        // Mock registering a user within authService
        willDoNothing().given(authService).registerUserWithoutLogin(eq(email), eq(password));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isCreated());
    }


    @Test
    public void testRegisterWithDuplicateEmail() throws Exception{
        String email = "u1@goop.com";
        String password = "thepass";

        // Mock registering a user within authService with a duplicate email
        willThrow(new ResponseStatusException(CONFLICT, "Email already in use"))
                .given(authService).registerUserWithoutLogin(eq(email), eq(password));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isConflict());
    }

    @Test
    public void testAuthUserSuccess() throws Exception {
        String email = "u2@goop.com";
        String password = "thepass";
        int ACCESS_TOKEN_EXPIRATION = 15 * 60;

        // Mock authenticating a user within authService
        given(authService.getAccessTokenFromCredentials(eq(email), eq(password), any(HttpServletResponse.class)))
                .willReturn(new AuthResponseDto("jwt", ACCESS_TOKEN_EXPIRATION, email));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.accessTokenExpiry").value(ACCESS_TOKEN_EXPIRATION));
    }

    @Test
    public void testAuthUserFailureWrongPassword() throws Exception {
        String email = "u2@goop.com";
        String password = "NOTthepass";

        // Mock authenticating a user within authService with a wrong password
        given(authService.getAccessTokenFromCredentials(eq(email), eq(password), any(HttpServletResponse.class)))
                .willThrow(new ResponseStatusException(UNAUTHORIZED, "Incorrect password"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAuthUserFailureEmailNotFound() throws Exception {
        String email = "u0@goop.com";
        String password = "thepass";

        // Mock authenticating a user within authService with an email not found
        given(authService.getAccessTokenFromCredentials(eq(email), eq(password), any(HttpServletResponse.class)))
                .willThrow(new ResponseStatusException(NOT_FOUND, "User not found"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isNotFound());
    }

}
