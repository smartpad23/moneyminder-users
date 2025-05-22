package com.moneyminder.moneyminderusers.persistence.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    @Test
    @DisplayName("Getters test with constructor simulation")
    void testGetters() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        userEntity.setEmail("testuser@example.com");
        userEntity.setPassword("securePassword");

        GroupEntity group1 = new GroupEntity();
        group1.setId("group1");

        GroupEntity group2 = new GroupEntity();
        group2.setId("group2");

        SessionEntity session = new SessionEntity();
        session.setId("session1");

        GroupRequestEntity requestingGroup = new GroupRequestEntity();
        requestingGroup.setId("requesting1");

        GroupRequestEntity requestedGroup = new GroupRequestEntity();
        requestedGroup.setId("requested1");

        userEntity.setGroups(List.of(group1, group2));
        userEntity.setSessions(List.of(session));
        userEntity.setRequestingGroups(List.of(requestingGroup));
        userEntity.setRequestedGroups(List.of(requestedGroup));

        assertEquals("testuser", userEntity.getUsername());
        assertEquals("testuser@example.com", userEntity.getEmail());
        assertEquals("securePassword", userEntity.getPassword());
        assertEquals(2, userEntity.getGroups().size());
        assertEquals(1, userEntity.getSessions().size());
        assertEquals(1, userEntity.getRequestingGroups().size());
        assertEquals(1, userEntity.getRequestedGroups().size());
    }

    @Test
    @DisplayName("Setters test with NoArgsConstructor")
    void testSetters() {
        UserEntity userEntity = new UserEntity();

        userEntity.setUsername("setteruser");
        userEntity.setEmail("setteruser@example.com");
        userEntity.setPassword("setterPassword");

        assertEquals("setteruser", userEntity.getUsername());
        assertEquals("setteruser@example.com", userEntity.getEmail());
        assertEquals("setterPassword", userEntity.getPassword());
    }

}