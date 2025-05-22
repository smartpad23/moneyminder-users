package com.moneyminder.moneyminderusers.processors.group;

import com.moneyminder.moneyminderusers.dto.CreateGroupByUsernameDto;
import com.moneyminder.moneyminderusers.mappers.GroupMapper;
import com.moneyminder.moneyminderusers.models.Group;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaveGroupProcessorTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupMapper groupMapper;

    @InjectMocks
    private SaveGroupProcessor saveGroupProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("save group test")
    void saveGroupTest() {
        Group group = Group.builder()
                .id(UUID.randomUUID().toString())
                .name("Test Group")
                .users(List.of("user1"))
                .build();

        GroupEntity groupEntity = new GroupEntity();
        GroupEntity savedEntity = new GroupEntity();
        Group savedGroup = new Group();

        when(groupMapper.toEntity(group)).thenReturn(groupEntity);
        when(userRepository.findAllByUsernameInOrEmailIn(group.getUsers(), group.getUsers()))
                .thenReturn(List.of(new UserEntity()));
        when(groupRepository.save(groupEntity)).thenReturn(savedEntity);
        when(groupMapper.toModel(savedEntity)).thenReturn(savedGroup);

        Group result = saveGroupProcessor.saveGroup(group);

        assertNotNull(result);
        verify(groupMapper).toEntity(group);
        verify(userRepository).findAllByUsernameInOrEmailIn(group.getUsers(), group.getUsers());
        verify(groupRepository).save(groupEntity);
        verify(groupMapper).toModel(savedEntity);
    }

    @Test
    @DisplayName("save group with null name test")
    void saveGroupWithNullNameTest() {
        Group group = Group.builder()
                .id(UUID.randomUUID().toString())
                .name(null)
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveGroupProcessor.saveGroup(group));

        assertEquals("Group name cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("save group with empty name Test")
    void saveGroupWithEmptyNameTest() {
        Group group = Group.builder()
                .id(UUID.randomUUID().toString())
                .name("")
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveGroupProcessor.saveGroup(group));

        assertEquals("Group name cannot be empty", exception.getMessage());
    }


    @Test
    @DisplayName("save group list test")
    void saveGroupListTest() {
        Group group1 = Group.builder()
                .id(UUID.randomUUID().toString())
                .name("Group 1")
                .users(List.of("user1"))
                .build();

        Group group2 = Group.builder()
                .id(UUID.randomUUID().toString())
                .name("Group 2")
                .users(List.of("user2"))
                .build();

        SaveGroupProcessor spyProcessor = spy(saveGroupProcessor);

        doReturn(new Group()).when(spyProcessor).saveGroup(any(Group.class));

        List<Group> result = spyProcessor.saveGroupList(List.of(group1, group2));

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(spyProcessor, times(2)).saveGroup(any(Group.class));
    }

    @Test
    @DisplayName("create group by username test")
    void createGroupByUsernameTest() {
        CreateGroupByUsernameDto dto = new CreateGroupByUsernameDto();
        dto.setUsername("user1");
        dto.setGroupName("Test");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("user1");

        when(userRepository.findByUsername(dto.getUsername())).thenReturn(Optional.of(userEntity));
        when(groupRepository.save(any(GroupEntity.class))).thenAnswer(invocation -> {
            GroupEntity savedEntity = invocation.getArgument(0);
            savedEntity.setId(UUID.randomUUID().toString());
            return savedEntity;
        });

        String groupId = saveGroupProcessor.createGroupByUsername(dto);

        assertNotNull(groupId);
        verify(userRepository).findByUsername(dto.getUsername());
        verify(groupRepository).save(any(GroupEntity.class));
    }

    @Test
    @DisplayName("create group by username not found test")
    void createGroupByUsernameUserNotFoundTest() {
        CreateGroupByUsernameDto dto = new CreateGroupByUsernameDto();
        dto.setUsername("user1");
        dto.setGroupName("Test");

        when(userRepository.findByUsername(dto.getUsername())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> saveGroupProcessor.createGroupByUsername(dto));
        verify(userRepository).findByUsername(dto.getUsername());
        verifyNoInteractions(groupRepository);
    }

    @Test
    @DisplayName("update group test")
    void updateGroupTest() {
        String groupId = UUID.randomUUID().toString();
        Group group = Group.builder()
                .id(groupId)
                .name("Updated Group")
                .users(List.of("user1"))
                .build();

        GroupEntity groupEntity = new GroupEntity();
        GroupEntity savedEntity = new GroupEntity();
        Group savedGroup = new Group();

        when(groupMapper.toEntity(group)).thenReturn(groupEntity);
        when(userRepository.findAllByUsernameInOrEmailIn(group.getUsers(), group.getUsers()))
                .thenReturn(List.of(new UserEntity()));
        when(groupRepository.save(groupEntity)).thenReturn(savedEntity);
        when(groupMapper.toModel(savedEntity)).thenReturn(savedGroup);

        Group result = saveGroupProcessor.updateGroup(groupId, group);

        assertNotNull(result);
        verify(groupMapper).toEntity(group);
        verify(userRepository).findAllByUsernameInOrEmailIn(group.getUsers(), group.getUsers());
        verify(groupRepository).save(groupEntity);
        verify(groupMapper).toModel(savedEntity);
    }

    @Test
    @DisplayName("update group id mismatch test")
    void updateGroupIdMismatchTest() {
        String id = UUID.randomUUID().toString();
        String differentId = UUID.randomUUID().toString();
        Group group = Group.builder()
                .id(differentId)
                .name("Group")
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveGroupProcessor.updateGroup(id, group));

        assertEquals("Group id does not match", exception.getMessage());
    }

    @Test
    @DisplayName("update group list test")
    void updateGroupListTest() {
        Group group1 = Group.builder()
                .id(UUID.randomUUID().toString())
                .name("Group 1")
                .users(List.of("user1"))
                .build();

        Group group2 = Group.builder()
                .id(UUID.randomUUID().toString())
                .name("Group 2")
                .users(List.of("user2"))
                .build();

        SaveGroupProcessor spyProcessor = spy(saveGroupProcessor);

        doReturn(new Group()).when(spyProcessor).updateGroup(anyString(), any(Group.class));

        List<Group> result = spyProcessor.updateGroupList(List.of(group1, group2));

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(spyProcessor, times(2)).updateGroup(anyString(), any(Group.class));
    }

}