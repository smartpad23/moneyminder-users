package com.moneyminder.moneyminderusers.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDtoTest {

    @Test
    @DisplayName("Getters test with Builder")
    void testGettersWithBuilder() {
        UserResponseDto userResponse = UserResponseDto.builder()
                .username("testUser")
                .email("testuser@example.com")
                .build();

        assertEquals("testUser", userResponse.getUsername());
        assertEquals("testuser@example.com", userResponse.getEmail());
    }

    @Test
    @DisplayName("Setters test with NoArgsConstructor")
    void testSettersWithNoArgsConstructor() {
        UserResponseDto userResponse = new UserResponseDto();
        userResponse.setUsername("setterUser");
        userResponse.setEmail("setteruser@example.com");

        assertEquals("setterUser", userResponse.getUsername());
        assertEquals("setteruser@example.com", userResponse.getEmail());
    }
}