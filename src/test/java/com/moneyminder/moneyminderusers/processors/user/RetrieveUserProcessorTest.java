package com.moneyminder.moneyminderusers.processors.user;

import com.moneyminder.moneyminderusers.dto.UserResponseDto;
import com.moneyminder.moneyminderusers.mappers.UserMapper;
import com.moneyminder.moneyminderusers.models.User;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import com.moneyminder.moneyminderusers.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RetrieveUserProcessorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private RetrieveUserProcessor retrieveUserProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("retrieve users test")
    void retrieveUsersSuccessfullyTest() {
        List<UserEntity> userEntities = List.of(new UserEntity());
        List<User> users = List.of(new User());

        when(userRepository.findAll()).thenReturn(userEntities);
        when(userMapper.toModelList(userEntities)).thenReturn(users);

        List<User> result = retrieveUserProcessor.retrieveUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository).findAll();
        verify(userMapper).toModelList(userEntities);
    }

    @Test
    @DisplayName("retrieve user by username or email test")
    void retrieveUserByUsernameOrEmailSuccessfullyTest() {
        String id = "testId";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setEmail("test@example.com");

        when(userRepository.findByUsernameOrEmail(id, id)).thenReturn(Optional.of(userEntity));

        UserResponseDto result = retrieveUserProcessor.retrieveUserByUsernameOrEmail(id);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findByUsernameOrEmail(id, id);
    }

    @Test
    @DisplayName("retrieve user by username or email not found test")
    void retrieveUserByUsernameOrEmailNotFoundTest() {
        String id = "nonExistingUser";

        when(userRepository.findByUsernameOrEmail(id, id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> retrieveUserProcessor.retrieveUserByUsernameOrEmail(id));
    }

    @Test
    @DisplayName("retrieve complete user by username or email test")
    void retrieveCompleteUserByUsernameOrEmailSuccessfullyTest() {
        String id = "testId";
        UserEntity userEntity = new UserEntity();

        when(userRepository.findByUsernameOrEmail(id, id)).thenReturn(Optional.of(userEntity));

        UserEntity result = retrieveUserProcessor.retrieveCompleteUserByUsernameOrEmail(id);

        assertNotNull(result);
        verify(userRepository).findByUsernameOrEmail(id, id);
    }

    @Test
    @DisplayName("retrieve complete user by username or email not found test")
    void retrieveCompleteUserByUsernameOrEmailNotFoundTest() {
        String id = "nonExistingUser";

        when(userRepository.findByUsernameOrEmail(id, id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> retrieveUserProcessor.retrieveCompleteUserByUsernameOrEmail(id));
    }

}