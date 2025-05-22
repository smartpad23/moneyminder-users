package com.moneyminder.moneyminderusers.processors.groupRequest;

import com.moneyminder.moneyminderusers.mappers.GroupRequestMapper;
import com.moneyminder.moneyminderusers.persistence.repositories.GroupRequestRepository;
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

class DeleteGroupRequestProcessorTest {
    @Mock
    private GroupRequestRepository groupRequestRepository;

    @Mock
    private GroupRequestMapper groupRequestMapper;

    @InjectMocks
    private DeleteGroupRequestProcessor deleteGroupRequestProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("delete group request test")
    void deleteGroupRequestTest() {
        String id = UUID.randomUUID().toString();

        when(groupRequestRepository.existsById(id)).thenReturn(true);

        deleteGroupRequestProcessor.deleteGroupRequest(id);

        verify(groupRequestRepository).existsById(id);
        verify(groupRequestRepository).deleteById(id);
    }

    @Test
    @DisplayName("delete group request not found test")
    void deleteGroupRequestNotFoundTest() {
        String id = UUID.randomUUID().toString();

        when(groupRequestRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                deleteGroupRequestProcessor.deleteGroupRequest(id));

        assertEquals("GroupRequest not found", exception.getMessage());
        verify(groupRequestRepository).existsById(id);
        verify(groupRequestRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("delete group request list test")
    void deleteGroupRequestListTest() {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        List<String> ids = List.of(id1, id2);

        when(groupRequestRepository.existsById(id1)).thenReturn(true);
        when(groupRequestRepository.existsById(id2)).thenReturn(true);

        deleteGroupRequestProcessor.deleteGroupRequestList(ids);

        verify(groupRequestRepository).existsById(id1);
        verify(groupRequestRepository).existsById(id2);
        verify(groupRequestRepository).deleteById(id1);
        verify(groupRequestRepository).deleteById(id2);
    }

}