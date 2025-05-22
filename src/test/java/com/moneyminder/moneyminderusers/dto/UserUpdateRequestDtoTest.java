package com.moneyminder.moneyminderusers.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserUpdateRequestDtoTest {
    private static Validator validator;

    @Test
    @DisplayName("Getters test with Builder")
    void testGettersWithBuilder() {
        UserUpdateRequestDto userUpdate = UserUpdateRequestDto.builder()
                .username("testUser")
                .email("testuser@example.com")
                .oldPassword("oldPass123")
                .newPassword("newPass456")
                .build();

        assertEquals("testUser", userUpdate.getUsername());
        assertEquals("testuser@example.com", userUpdate.getEmail());
        assertEquals("oldPass123", userUpdate.getOldPassword());
        assertEquals("newPass456", userUpdate.getNewPassword());
    }

    @Test
    @DisplayName("Setters test with NoArgsConstructor")
    void testSettersWithNoArgsConstructor() {
        UserUpdateRequestDto userUpdate = new UserUpdateRequestDto();
        userUpdate.setUsername("setterUser");
        userUpdate.setEmail("setteruser@example.com");
        userUpdate.setOldPassword("oldPass789");
        userUpdate.setNewPassword("newPass012");

        assertEquals("setterUser", userUpdate.getUsername());
        assertEquals("setteruser@example.com", userUpdate.getEmail());
        assertEquals("oldPass789", userUpdate.getOldPassword());
        assertEquals("newPass012", userUpdate.getNewPassword());
    }

    @Test
    @DisplayName("User update invalid email format test")
    void testValidationFailWhenEmailIsInvalid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        UserUpdateRequestDto userUpdate = UserUpdateRequestDto.builder()
                .username("userInvalidEmail")
                .email("invalid-email-format")
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();

        Set<ConstraintViolation<UserUpdateRequestDto>> violations = validator.validate(userUpdate);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("El usuario debe tener un email con el formato correcto")));
    }

    @Test
    @DisplayName("User update valid email test")
    void testValidationPassWhenEmailIsValid() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        UserUpdateRequestDto userUpdate = UserUpdateRequestDto.builder()
                .username("validUser")
                .email("validuser@example.com")
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();

        Set<ConstraintViolation<UserUpdateRequestDto>> violations = validator.validate(userUpdate);

        assertTrue(violations.isEmpty());
    }
}