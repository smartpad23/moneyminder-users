package com.moneyminder.moneyminderusers.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestDtoTest {

    @Test
    @DisplayName("Getters test with Builder")
    void testGettersWithBuilder() {
        LoginRequestDto loginRequest = LoginRequestDto.builder()
                .username("builderUser")
                .password("builderPassword")
                .build();

        assertEquals("builderUser", loginRequest.getUsername());
        assertEquals("builderPassword", loginRequest.getPassword());
    }

    @Test
    @DisplayName("Setters test with NoArgsConstructor")
    void testSettersWithNoArgsConstructor() {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername("setterUser");
        loginRequest.setPassword("setterPassword");

        assertEquals("setterUser", loginRequest.getUsername());
        assertEquals("setterPassword", loginRequest.getPassword());
    }

}