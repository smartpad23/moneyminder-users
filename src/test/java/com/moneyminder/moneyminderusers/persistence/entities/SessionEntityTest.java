package com.moneyminder.moneyminderusers.persistence.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SessionEntityTest {

    @Test
    @DisplayName("Getters test with constructor simulation")
    void testGetters() {
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setId("123e4567-e89b-12d3-a456-426614174001");
        sessionEntity.setToken("securetoken123");
        sessionEntity.setCreationDate(LocalDate.of(2025, 4, 26));
        sessionEntity.setExpirationDate(LocalDate.of(2025, 5, 26));

        UserEntity user = new UserEntity();
        user.setUsername("testUser");

        sessionEntity.setUser(user);

        assertEquals("123e4567-e89b-12d3-a456-426614174001", sessionEntity.getId());
        assertEquals("securetoken123", sessionEntity.getToken());
        assertEquals(LocalDate.of(2025, 4, 26), sessionEntity.getCreationDate());
        assertEquals(LocalDate.of(2025, 5, 26), sessionEntity.getExpirationDate());
        assertEquals("testUser", sessionEntity.getUser().getUsername());
    }

    @Test
    @DisplayName("Setters test with NoArgsConstructor")
    void testSetters() {
        SessionEntity sessionEntity = new SessionEntity();

        sessionEntity.setId("987e6543-e21b-45d6-c123-556788991111");
        sessionEntity.setToken("anothertoken456");
        sessionEntity.setCreationDate(LocalDate.of(2025, 7, 1));
        sessionEntity.setExpirationDate(LocalDate.of(2025, 8, 1));

        assertEquals("987e6543-e21b-45d6-c123-556788991111", sessionEntity.getId());
        assertEquals("anothertoken456", sessionEntity.getToken());
        assertEquals(LocalDate.of(2025, 7, 1), sessionEntity.getCreationDate());
        assertEquals(LocalDate.of(2025, 8, 1), sessionEntity.getExpirationDate());
    }

}