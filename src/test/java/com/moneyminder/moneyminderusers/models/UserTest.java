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

class UserTest {

    private static Validator validator;

    @Test
    @DisplayName("Getters test")
    void testGettersWithBuilder() {
        User user = User.builder()
                .username("testuser")
                .email("testuser@example.com")
                .password("securePassword123")
                .groups(List.of("group1", "group2"))
                .sessions(List.of("session1"))
                .requestingGroups(List.of("requestingGroup1"))
                .requestedGroups(List.of("requestedGroup1"))
                .build();

        assertEquals("testuser", user.getUsername());
        assertEquals("testuser@example.com", user.getEmail());
        assertEquals("securePassword123", user.getPassword());
        assertEquals(2, user.getGroups().size());
        assertEquals(1, user.getSessions().size());
        assertEquals(1, user.getRequestingGroups().size());
        assertEquals(1, user.getRequestedGroups().size());
    }

    @Test
    @DisplayName("Setters test")
    void testSettersWithNoArgsConstructor() {
        User user = new User();
        user.setUsername("setteruser");
        user.setEmail("setteruser@example.com");
        user.setPassword("setterPassword456");
        user.setGroups(List.of("group3"));
        user.setSessions(List.of("session2"));
        user.setRequestingGroups(List.of("requestingGroup2"));
        user.setRequestedGroups(List.of("requestedGroup2"));

        assertEquals("setteruser", user.getUsername());
        assertEquals("setteruser@example.com", user.getEmail());
        assertEquals("setterPassword456", user.getPassword());
        assertEquals(1, user.getGroups().size());
        assertEquals(1, user.getSessions().size());
        assertEquals(1, user.getRequestingGroups().size());
        assertEquals(1, user.getRequestedGroups().size());
    }

    @Test
    @DisplayName("Username blank test")
    void testUserValidationFailWhenUsernameIsBlank() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        User user = User.builder()
                .username(" ")
                .email("validemail@example.com")
                .password("somePassword")
                .groups(List.of())
                .sessions(List.of())
                .requestingGroups(List.of())
                .requestedGroups(List.of())
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("El usuario debe tener un nombre de usuario")));
    }

    @Test
    @DisplayName("Email format test")
    void testUserValidationFailWhenEmailIsInvalid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        User user = User.builder()
                .username("validUser")
                .email("invalid-email")
                .password("anotherPassword")
                .groups(List.of())
                .sessions(List.of())
                .requestingGroups(List.of())
                .requestedGroups(List.of())
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("El usuario debe tener un email con el formato correcto")));
    }

    @Test
    @DisplayName("Password blank test")
    void testUserValidationFailWhenPasswordIsBlank() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        User user = User.builder()
                .username("userWithNoPassword")
                .email("user@example.com")
                .password(" ")
                .groups(List.of())
                .sessions(List.of())
                .requestingGroups(List.of())
                .requestedGroups(List.of())
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("El usuario debe tener tener una contraseña")));
    }

    @Test
    @DisplayName("All mandatory fields not blank test")
    void testUserValidationPassWhenAllFieldsAreValid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        User user = User.builder()
                .username("completeUser")
                .email("completeuser@example.com")
                .password("completePassword")
                .groups(List.of("groupX"))
                .sessions(List.of("sessionX"))
                .requestingGroups(List.of("reqGroupX"))
                .requestedGroups(List.of("resGroupX"))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty());
    }

}