package com.moneyminder.moneyminderusers.persistence.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GroupRequestEntityTest {

    @Test
    @DisplayName("Getters test with constructor simulation")
    void testGetters() {
        GroupRequestEntity requestEntity = new GroupRequestEntity();
        requestEntity.setId("321e4567-e89b-12d3-a456-426614174000");

        GroupEntity group = new GroupEntity();
        group.setId("group-uuid");

        UserEntity requestingUser = new UserEntity();
        requestingUser.setUsername("requestingUser");

        UserEntity requestedUser = new UserEntity();
        requestedUser.setUsername("requestedUser");

        requestEntity.setGroup(group);
        requestEntity.setRequestingUser(requestingUser);
        requestEntity.setRequestedUser(requestedUser);
        requestEntity.setDate(LocalDate.of(2025, 4, 26));
        requestEntity.setResolvedDate(LocalDate.of(2025, 5, 1));
        requestEntity.setAccepted(true);

        assertEquals("321e4567-e89b-12d3-a456-426614174000", requestEntity.getId());
        assertEquals("group-uuid", requestEntity.getGroup().getId());
        assertEquals("requestingUser", requestEntity.getRequestingUser().getUsername());
        assertEquals("requestedUser", requestEntity.getRequestedUser().getUsername());
        assertEquals(LocalDate.of(2025, 4, 26), requestEntity.getDate());
        assertEquals(LocalDate.of(2025, 5, 1), requestEntity.getResolvedDate());
        assertTrue(requestEntity.getAccepted());
    }

    @Test
    @DisplayName("Setters test with NoArgsConstructor")
    void testSetters() {
        GroupRequestEntity requestEntity = new GroupRequestEntity();

        requestEntity.setId("654e3210-b89b-43d3-a789-123456789abc");
        requestEntity.setDate(LocalDate.of(2025, 6, 15));
        requestEntity.setResolvedDate(LocalDate.of(2025, 7, 1));
        requestEntity.setAccepted(false);

        assertEquals("654e3210-b89b-43d3-a789-123456789abc", requestEntity.getId());
        assertEquals(LocalDate.of(2025, 6, 15), requestEntity.getDate());
        assertEquals(LocalDate.of(2025, 7, 1), requestEntity.getResolvedDate());
        assertFalse(requestEntity.getAccepted());
    }

}