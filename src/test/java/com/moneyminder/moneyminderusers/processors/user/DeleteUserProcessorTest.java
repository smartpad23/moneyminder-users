package com.moneyminder.moneyminderusers.processors.user;

import com.moneyminder.moneyminderusers.mappers.UserMapper;
import com.moneyminder.moneyminderusers.persistence.entities.GroupEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import com.moneyminder.moneyminderusers.persistence.repositories.GroupRepository;
import com.moneyminder.moneyminderusers.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteUserProcessorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private DeleteUserProcessor deleteUserProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("delete user test")
    void deleteUserSuccessfullyTest() {
        String username = "testUser";

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setUsers(new ArrayList<>(List.of(userEntity)));
        userEntity.setGroups(List.of(groupEntity));

        when(userRepository.existsByUsername(username)).thenReturn(true);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));

        deleteUserProcessor.deleteUser(username);

        verify(userRepository).deleteByUsername(username);
    }


    @Test
    @DisplayName("delete user not found test")
    void deleteUserNotFoundTest() {
        String username = "nonExistingUser";

        when(userRepository.existsByUsername(username)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> deleteUserProcessor.deleteUser(username));

        assertEquals("Username does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("delete user list test")
    void deleteUserListSuccessfullyTest() {
        String username1 = "user1";
        String username2 = "user2";

        DeleteUserProcessor spyProcessor = spy(deleteUserProcessor);
        doNothing().when(spyProcessor).deleteUser(anyString());

        List<String> usernames = List.of(username1, username2);

        spyProcessor.deleteUserList(usernames);

        verify(spyProcessor).deleteUser(username1);
        verify(spyProcessor).deleteUser(username2);
    }

}