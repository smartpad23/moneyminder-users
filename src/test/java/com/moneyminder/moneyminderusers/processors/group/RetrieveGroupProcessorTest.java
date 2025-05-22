package com.moneyminder.moneyminderusers.processors.group;

import com.moneyminder.moneyminderusers.mappers.GroupMapper;
import com.moneyminder.moneyminderusers.models.Group;
import com.moneyminder.moneyminderusers.persistence.entities.GroupEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import com.moneyminder.moneyminderusers.persistence.repositories.GroupRepository;
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

class RetrieveGroupProcessorTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMapper groupMapper;

    @InjectMocks
    private RetrieveGroupProcessor retrieveGroupProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("retrieve groups test")
    void retrieveGroupsTest() {
        List<GroupEntity> groupEntities = List.of(new GroupEntity());
        List<Group> groups = List.of(new Group());

        when(groupRepository.findAll()).thenReturn(groupEntities);
        when(groupMapper.toModelList(groupEntities)).thenReturn(groups);

        List<Group> result = retrieveGroupProcessor.retrieveGroups();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(groupRepository).findAll();
        verify(groupMapper).toModelList(groupEntities);
    }

    @Test
    @DisplayName("retrieve group by id test")
    void retrieveGroupByIdTest() {
        String groupId = UUID.randomUUID().toString();
        GroupEntity groupEntity = new GroupEntity();
        Group group = new Group();

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(groupEntity));
        when(groupMapper.toModel(groupEntity)).thenReturn(group);

        Group result = retrieveGroupProcessor.retrieveGroupById(groupId);

        assertNotNull(result);
        verify(groupRepository).findById(groupId);
        verify(groupMapper).toModel(groupEntity);
    }

    @Test
    @DisplayName("retrieve group by id not found test")
    void retrieveGroupByIdThrowWhenGroupNotFoundTest() {
        String groupId = UUID.randomUUID().toString();
        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> retrieveGroupProcessor.retrieveGroupById(groupId));
        verify(groupRepository).findById(groupId);
        verifyNoInteractions(groupMapper);
    }

    @Test
    @DisplayName("retrieve group Ids by username test")
    void retrieveGroupIdsByUsernameTest() {
        String username = "testUser";
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(UUID.randomUUID().toString());

        when(groupRepository.findAllByUsers_Username(username)).thenReturn(List.of(groupEntity));

        List<String> result = retrieveGroupProcessor.retrieveGroupIdsByUsername(username);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(groupEntity.getId(), result.get(0));
        verify(groupRepository).findAllByUsers_Username(username);
    }

    @Test
    @DisplayName("retrieve usernames of group test")
    void retrieveUsernameOfGroupTest() {
        String groupId = UUID.randomUUID().toString();
        GroupEntity groupEntity = new GroupEntity();
        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");
        UserEntity user2 = new UserEntity();
        user2.setUsername("user2");
        groupEntity.setUsers(List.of(user1, user2));

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(groupEntity));

        List<String> result = retrieveGroupProcessor.retrieveUsernameOfGroup(groupId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("user1"));
        assertTrue(result.contains("user2"));
        verify(groupRepository).findById(groupId);
    }

    @Test
    @DisplayName("retrieve usernames of group group not found test")
    void retrieveUsernameOfGroupThrowWhenGroupNotFoundTest() {
        String groupId = UUID.randomUUID().toString();
        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> retrieveGroupProcessor.retrieveUsernameOfGroup(groupId));
        verify(groupRepository).findById(groupId);
    }
}