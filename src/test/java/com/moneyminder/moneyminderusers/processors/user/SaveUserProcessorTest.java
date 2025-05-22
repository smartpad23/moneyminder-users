package com.moneyminder.moneyminderusers.processors.user;

import com.moneyminder.moneyminderusers.utils.AuthUtils;
import com.moneyminder.moneyminderusers.dto.UserUpdateRequestDto;
import com.moneyminder.moneyminderusers.mappers.UserMapper;
import com.moneyminder.moneyminderusers.models.User;
import com.moneyminder.moneyminderusers.persistence.entities.GroupEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import com.moneyminder.moneyminderusers.persistence.repositories.GroupRepository;
import com.moneyminder.moneyminderusers.persistence.repositories.UserRepository;
import com.moneyminder.moneyminderusers.processors.authServices.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SaveUserProcessorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SaveUserProcessor saveUserProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("save user successfully test")
    void saveUserSuccessfullyTest() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPass");
        user.setEmail("test@example.com");
        user.setGroups(List.of("groupId"));

        UserEntity userEntity = new UserEntity();
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setUsers(new ArrayList<>());

        when(userMapper.toEntity(user)).thenReturn(userEntity);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPass");
        when(groupRepository.findAllByIdIn(user.getGroups())).thenReturn(List.of(groupEntity));
        when(authenticationService.authenticationMethod(any())).thenReturn("token");

        String token = saveUserProcessor.saveUser(user);

        assertNotNull(token);
        verify(userMapper).toEntity(user);
        verify(passwordEncoder).encode(user.getPassword());
        verify(groupRepository).findAllByIdIn(user.getGroups());
        verify(userRepository).save(userEntity);
        verify(authenticationService).authenticationMethod(any());
    }

    @Test
    @DisplayName("save user authentication failure test")
    void saveUserAuthenticationFailureTest() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPass");
        user.setEmail("test@example.com");

        UserEntity userEntity = new UserEntity();

        when(userMapper.toEntity(user)).thenReturn(userEntity);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPass");
        when(authenticationService.authenticationMethod(any())).thenThrow(new RuntimeException("Authentication failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> saveUserProcessor.saveUser(user));
        assertEquals("Error durante el login del usuario creado", exception.getMessage());
    }

    @Test
    @DisplayName("check user attributes null test")
    void checkUserAttributesNullTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveUserProcessor.saveUser(null));
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("check user attributes empty username test")
    void checkUserAttributesEmptyUsernameTest() {
        User user = new User();
        user.setUsername("");
        user.setPassword("password");
        user.setEmail("email@example.com");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveUserProcessor.saveUser(user));
        assertEquals("Username cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("check user attributes empty password test")
    void checkUserAttributesEmptyPasswordTest() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("");
        user.setEmail("email@example.com");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveUserProcessor.saveUser(user));
        assertEquals("Password cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("check user attributes empty email test")
    void checkUserAttributesEmptyEmailTest() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        user.setEmail("");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveUserProcessor.saveUser(user));
        assertEquals("Email cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("update user successfully test")
    void updateUserSuccessfullyTest() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPass");
        user.setEmail("test@example.com");
        user.setGroups(List.of("groupId"));

        UserEntity dbUserEntity = new UserEntity();
        dbUserEntity.setGroups(new ArrayList<>());

        UserEntity userEntity = new UserEntity();

        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setUsers(new ArrayList<>());

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(dbUserEntity));
        when(userMapper.toEntity(user)).thenReturn(userEntity);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPass");
        when(groupRepository.findAllByIdIn(user.getGroups())).thenReturn(List.of(groupEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toModel(userEntity)).thenReturn(user);

        User result = saveUserProcessor.updateUser(user);

        assertNotNull(result);
        verify(userRepository).existsByUsername(user.getUsername());
        verify(userRepository).findByUsername(user.getUsername());
        verify(userMapper).toEntity(user);
        verify(passwordEncoder).encode(user.getPassword());
        verify(userRepository).save(userEntity);
    }

    @Test
    @DisplayName("update user not existing test")
    void updateUserNotExistingThrowsTest() {
        User user = new User();
        user.setUsername("nonExistingUser");

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveUserProcessor.updateUser(user));
        assertEquals("User with username or email don't exists", exception.getMessage());
    }

    @Test
    @DisplayName("update user data user not found test")
    void updateUserDataUserNotFoundTest() {
        String username = "testUser";
        UserUpdateRequestDto userUpdateRequest = new UserUpdateRequestDto();
        userUpdateRequest.setEmail("new@example.com");

        try (MockedStatic<AuthUtils> mocked = mockStatic(AuthUtils.class)) {
            mocked.when(AuthUtils::getUsername).thenReturn(username);
            when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> saveUserProcessor.updateUserData(userUpdateRequest));
            assertEquals("User with username don't exists", exception.getMessage());
        }
    }

    @Test
    @DisplayName("update user list test")
    void updateUserListTest() {
        User user = new User();
        SaveUserProcessor spyProcessor = spy(saveUserProcessor);

        doReturn(user).when(spyProcessor).updateUser(any(User.class));

        List<User> result = spyProcessor.updateUserList(List.of(user));

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(spyProcessor).updateUser(any(User.class));
    }

}