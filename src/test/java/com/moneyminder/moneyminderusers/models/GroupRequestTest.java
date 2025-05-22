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

class GroupRequestTest {
    private static Validator validator;

    @Test
    @DisplayName("Getters test")
    void testGettersWithBuilder() {
        GroupRequest request = GroupRequest.builder()
                .id("1")
                .group("groupId123")
                .requestingUser("userA")
                .requestedUser("userB")
                .date(LocalDate.now())
                .accepted(false)
                .build();

        assertEquals("1", request.getId());
        assertEquals("groupId123", request.getGroup());
        assertEquals("userA", request.getRequestingUser());
        assertEquals("userB", request.getRequestedUser());
        assertNotNull(request.getDate());
        assertFalse(request.getAccepted());
    }

    @Test
    @DisplayName("Setters test")
    void testSettersWithNoArgsConstructor() {
        GroupRequest request = new GroupRequest();
        request.setId("2");
        request.setGroup("groupId456");
        request.setRequestingUser("userC");
        request.setRequestedUser("userD");
        request.setDate(LocalDate.of(2025, 4, 26));
        request.setAccepted(true);

        assertEquals("2", request.getId());
        assertEquals("groupId456", request.getGroup());
        assertEquals("userC", request.getRequestingUser());
        assertEquals("userD", request.getRequestedUser());
        assertEquals(LocalDate.of(2025, 4, 26), request.getDate());
        assertTrue(request.getAccepted());
    }

    @Test
    @DisplayName("Requesting user blank test")
    void testGroupRequestValidationFailWhenRequestingUserIsBlank() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        GroupRequest request = GroupRequest.builder()
                .id("3")
                .group("groupId789")
                .requestingUser(" ")
                .requestedUser("user")
                .date(LocalDate.now())
                .accepted(false)
                .build();

        Set<ConstraintViolation<GroupRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("La solicitud debe estar asociada a un solicitante")));
    }

    @Test
    @DisplayName("Requested user blank test")
    void testGroupRequestValidationShouldFailWhenRequestedUserIsBlank() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        GroupRequest request = GroupRequest.builder()
                .id("4")
                .group("groupId101")
                .requestingUser("user")
                .requestedUser(" ")
                .date(LocalDate.now())
                .accepted(true)
                .build();

        Set<ConstraintViolation<GroupRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("La solicitud debe estar asociada a un destinatario")));
    }

    @Test
    @DisplayName("Users not blank test")
    void testGroupRequestValidationPassWhenAllFieldsAreValid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        GroupRequest request = GroupRequest.builder()
                .id("5")
                .group("groupId202")
                .requestingUser("userIng")
                .requestedUser("userEd")
                .date(LocalDate.now())
                .accepted(null)
                .build();

        Set<ConstraintViolation<GroupRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }
}