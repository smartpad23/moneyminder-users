package com.moneyminder.moneyminderusers.models;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {
    private static Validator validator;

    @Test
    @DisplayName("Getters test")
    void testGettersWithBuilder() {
        Session session = Session.builder()
                .id("session1")
                .token("abc123token")
                .creationDate(LocalDate.now())
                .expirationDate(LocalDate.now().plusDays(7))
                .user("userId1")
                .build();

        assertEquals("session1", session.getId());
        assertEquals("abc123token", session.getToken());
        assertNotNull(session.getCreationDate());
        assertEquals(LocalDate.now().plusDays(7), session.getExpirationDate());
        assertEquals("userId1", session.getUser());
    }

    @Test
    @DisplayName("Setters test")
    void testSettersWithNoArgsConstructor() {
        Session session = new Session();
        session.setId("session2");
        session.setToken("def456token");
        session.setCreationDate(LocalDate.of(2025, 4, 26));
        session.setExpirationDate(LocalDate.of(2025, 5, 3));
        session.setUser("userId2");

        assertEquals("session2", session.getId());
        assertEquals("def456token", session.getToken());
        assertEquals(LocalDate.of(2025, 4, 26), session.getCreationDate());
        assertEquals(LocalDate.of(2025, 5, 3), session.getExpirationDate());
        assertEquals("userId2", session.getUser());
    }

    @Test
    @DisplayName("Session token blank test")
    void testSessionSessionFailWhenTokenIsBlank() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Session session = Session.builder()
                .id("session3")
                .token(" ")
                .creationDate(LocalDate.now())
                .expirationDate(LocalDate.now().plusDays(7))
                .user("userId3")
                .build();

        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("El token no debe ser nullo")));
    }

    @Test
    @DisplayName("Session expiration date blank test")
    void testSessionValidationFailWhenExpirationDateIsNull() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Session session = Session.builder()
                .id("session4")
                .token("validToken123")
                .creationDate(LocalDate.now())
                .expirationDate(null)
                .user("userId4")
                .build();

        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("El token debe tener una fecha de caducidad")));
    }

    @Test
    @DisplayName("Session user blank test")
    void testSessionValidationFailWhenUserIsBlank() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Session session = Session.builder()
                .id("session5")
                .token("anotherToken456")
                .creationDate(LocalDate.now())
                .expirationDate(LocalDate.now().plusDays(5))
                .user(" ")
                .build();

        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("El token debe estar asociado a un usuario")));
    }

    @Test
    @DisplayName("All mandatory fields not blank test")
     void testSessionValidationPassWhenAllFieldsAreValid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Session session = Session.builder()
                .id("session6")
                .token("perfectToken789")
                .creationDate(LocalDate.now())
                .expirationDate(LocalDate.now().plusDays(10))
                .user("userId5")
                .build();

        Set<ConstraintViolation<Session>> violations = validator.validate(session);

        assertTrue(violations.isEmpty());
    }


}