package com.moneyminder.moneyminderusers.persistence.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupEntityTest {

    @Test
    @DisplayName("Getters test with constructor simulation")
    void testGetters() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId("123e4567-e89b-12d3-a456-426614174000");
        groupEntity.setName("Developers Group");

        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");

        UserEntity user2 = new UserEntity();
        user2.setUsername("user2");

        GroupRequestEntity request1 = new GroupRequestEntity();
        GroupRequestEntity request2 = new GroupRequestEntity();

        groupEntity.setUsers(List.of(user1, user2));
        groupEntity.setGroupRequests(List.of(request1, request2));

        assertEquals("123e4567-e89b-12d3-a456-426614174000", groupEntity.getId());
        assertEquals("Developers Group", groupEntity.getName());
        assertEquals(2, groupEntity.getUsers().size());
        assertEquals("user1", groupEntity.getUsers().get(0).getUsername());
        assertEquals(2, groupEntity.getGroupRequests().size());
    }

    @Test
    @DisplayName("Setters test with NoArgsConstructor")
    void testSetters() {
        GroupEntity groupEntity = new GroupEntity();

        groupEntity.setId("987e6543-e21b-45d6-c123-556788990000");
        groupEntity.setName("Test Group");

        assertEquals("987e6543-e21b-45d6-c123-556788990000", groupEntity.getId());
        assertEquals("Test Group", groupEntity.getName());
    }
}