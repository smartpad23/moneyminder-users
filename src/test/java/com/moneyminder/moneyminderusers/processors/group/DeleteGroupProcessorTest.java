package com.moneyminder.moneyminderusers.processors.group;

import com.moneyminder.moneyminderusers.mappers.GroupMapper;
import com.moneyminder.moneyminderusers.persistence.repositories.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteGroupProcessorTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMapper groupMapper;

    @InjectMocks
    private DeleteGroupProcessor deleteGroupProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("delete group test")
    void deleteGroupTest() {
        String groupId = UUID.randomUUID().toString();

        when(groupRepository.existsById(groupId)).thenReturn(true);

        deleteGroupProcessor.deleteGroup(groupId);

        verify(groupRepository).existsById(groupId);
        verify(groupRepository).deleteById(groupId);
    }

    @Test
    @DisplayName("delete group not found test")
    void deleteGroupNotFoundTest() {
        String groupId = UUID.randomUUID().toString();

        when(groupRepository.existsById(groupId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                deleteGroupProcessor.deleteGroup(groupId)
        );

        assertEquals("Group not found", exception.getMessage());
        verify(groupRepository).existsById(groupId);
        verify(groupRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("delete group list test")
    void deleteGroupListTest() {
        String groupId1 = UUID.randomUUID().toString();
        String groupId2 = UUID.randomUUID().toString();
        List<String> groupIds = List.of(groupId1, groupId2);

        when(groupRepository.existsById(groupId1)).thenReturn(true);
        when(groupRepository.existsById(groupId2)).thenReturn(true);

        deleteGroupProcessor.deleteGroupList(groupIds);

        verify(groupRepository).existsById(groupId1);
        verify(groupRepository).existsById(groupId2);
        verify(groupRepository).deleteById(groupId1);
        verify(groupRepository).deleteById(groupId2);
    }

}