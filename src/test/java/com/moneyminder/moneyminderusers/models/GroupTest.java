package com.moneyminder.moneyminderusers.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {
    private static Validator validator;

    @Test
    @DisplayName("Getters test")
    void testGettersWithBuilder() {
        Group group = Group.builder()
                .id("123")
                .name("Developers")
                .users(List.of("user1", "user2"))
                .groupRequests(List.of("request1"))
                .build();

        assertEquals("123", group.getId());
        assertEquals("Developers", group.getName());
        assertEquals(2, group.getUsers().size());
        assertEquals(1, group.getGroupRequests().size());
    }

    @Test
    @DisplayName("Setters test")
    void testSettersWithNoArgsConstructor() {
        Group group = new Group();
        group.setId("456");
        group.setName("Test Group");
        group.setUsers(List.of("user3"));
        group.setGroupRequests(List.of("request2", "request3"));

        assertEquals("456", group.getId());
        assertEquals("Test Group", group.getName());
        assertEquals(1, group.getUsers().size());
        assertEquals(2, group.getGroupRequests().size());
    }

    @Test
    @DisplayName("Group name blank test")
    void testGroupValidationFailWhenNameIsBlank() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Group group = Group.builder()
                .id("789")
                .name(" ")
                .users(List.of("user4"))
                .groupRequests(List.of("request4"))
                .build();

        Set<ConstraintViolation<Group>> violations = validator.validate(group);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El grupo debe tener un nombre")));
    }

    @Test
    @DisplayName("Group name not blank test")
    void testGroupValidationPassWhenNameIsValid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Group group = Group.builder()
                .id("101")
                .name("Marketing")
                .users(List.of())
                .groupRequests(List.of())
                .build();

        Set<ConstraintViolation<Group>> violations = validator.validate(group);

        assertTrue(violations.isEmpty());
    }

}