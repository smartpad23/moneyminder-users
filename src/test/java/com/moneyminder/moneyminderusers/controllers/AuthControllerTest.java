package com.moneyminder.moneyminderusers.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneyminder.moneyminderusers.dto.LoginRequestDto;
import com.moneyminder.moneyminderusers.processors.authServices.AuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("login success test")
    void loginSuccess() throws Exception {
        LoginRequestDto loginRequest = new LoginRequestDto("user", "password");
        String fakeToken = "fake-jwt-token";

        Mockito.when(authenticationService.authenticationMethod(any(LoginRequestDto.class)))
                .thenReturn(fakeToken);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(fakeToken));
    }

    @Test
    @DisplayName("login unauthorized test")
    void loginUnauthorized() throws Exception {
        LoginRequestDto loginRequest = new LoginRequestDto("user", "wrong-password");

        Mockito.when(authenticationService.authenticationMethod(any(LoginRequestDto.class)))
                .thenThrow(new AuthenticationException("Credenciales inválidas") {});

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Credenciales inválidas"));
    }

    @Test
    @DisplayName("login internal server error test")
    void loginInternalServerError() throws Exception {
        LoginRequestDto loginRequest = new LoginRequestDto("user", "password");

        Mockito.when(authenticationService.authenticationMethod(any(LoginRequestDto.class)))
                .thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Ocurrió un error inesperado"));
    }
}