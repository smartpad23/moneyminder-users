package com.moneyminder.moneyminderusers.processors.authServices;

import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import com.moneyminder.moneyminderusers.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("load user by username test")
    void loadUserByUsernameSuccessTest() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("user");
        userEntity.setPassword("password");

        when(userRepository.findByUsernameOrEmail("user", "user"))
                .thenReturn(Optional.of(userEntity));

        UserDetails userDetails = userService.loadUserByUsername("user");

        assertNotNull(userDetails);
        assertEquals("user", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @DisplayName("load user by username not found test")
    void loadUserByUsernameNotFoundTest() {
        when(userRepository.findByUsernameOrEmail("user", "user"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("user"));
    }

}