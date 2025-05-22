package com.moneyminder.moneyminderusers.processors.groupRequest;

import com.moneyminder.moneyminderusers.dto.GroupRequestWithBudgetNameDto;
import com.moneyminder.moneyminderusers.feignClients.BudgetFeignClient;
import com.moneyminder.moneyminderusers.mappers.GroupRequestMapper;
import com.moneyminder.moneyminderusers.models.GroupRequest;
import com.moneyminder.moneyminderusers.persistence.entities.GroupRequestEntity;
import com.moneyminder.moneyminderusers.persistence.repositories.GroupRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RetrieveGroupRequestProcessorTest {
    @Mock
    private GroupRequestRepository groupRequestRepository;

    @Mock
    private GroupRequestMapper groupRequestMapper;

    @Mock
    private BudgetFeignClient budgetFeignClient;

    @InjectMocks
    private RetrieveGroupRequestProcessor retrieveGroupRequestProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("retrieve group requests test")
    void retrieveGroupRequestTest() {
        List<GroupRequestEntity> groupRequestEntities = List.of(new GroupRequestEntity());
        List<GroupRequest> groupRequests = List.of(new GroupRequest());

        when(groupRequestRepository.findAll()).thenReturn(groupRequestEntities);
        when(groupRequestMapper.toModelList(groupRequestEntities)).thenReturn(groupRequests);

        List<GroupRequest> result = retrieveGroupRequestProcessor.retrieveGroupRequest();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(groupRequestRepository).findAll();
        verify(groupRequestMapper).toModelList(groupRequestEntities);
    }

    @Test
    @DisplayName("retrieve group request by id test")
    void retrieveGroupRequestByIdTest() {
        String id = UUID.randomUUID().toString();
        GroupRequestEntity groupRequestEntity = new GroupRequestEntity();
        GroupRequest groupRequest = new GroupRequest();

        when(groupRequestRepository.findById(id)).thenReturn(java.util.Optional.of(groupRequestEntity));
        when(groupRequestMapper.toModel(groupRequestEntity)).thenReturn(groupRequest);

        GroupRequest result = retrieveGroupRequestProcessor.retrieveGroupRequestById(id);

        assertNotNull(result);
        verify(groupRequestRepository).findById(id);
        verify(groupRequestMapper).toModel(groupRequestEntity);
    }

    @Test
    @DisplayName("retrieve group request by id not found test")
    void retrieveGroupRequestByIdNotFoundTest() {
        String id = UUID.randomUUID().toString();
        when(groupRequestRepository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThrows(NoSuchElementException.class, () -> retrieveGroupRequestProcessor.retrieveGroupRequestById(id));
        verify(groupRequestRepository).findById(id);
        verifyNoInteractions(groupRequestMapper);
    }

    @Test
    @DisplayName("retrieve group requests with budget name by username test")
    void retrieveGroupRequestWithBudgetNameByUsernameTest() {
        String username = "testUser";

        List<GroupRequestEntity> requestingList = List.of(new GroupRequestEntity());
        List<GroupRequestEntity> requestedList = List.of(new GroupRequestEntity());
        List<GroupRequest> groupRequestsRequesting = new ArrayList<>(List.of(GroupRequest.builder().group("group1").build()));
        List<GroupRequest> groupRequestsRequested = new ArrayList<>(List.of(GroupRequest.builder().group("group2").build()));

        when(groupRequestRepository.findAllByRequestingUser_Username(username)).thenReturn(requestingList);
        when(groupRequestRepository.findAllByRequestedUser_Username(username)).thenReturn(requestedList);
        when(groupRequestMapper.toModelList(requestingList)).thenReturn(groupRequestsRequesting);
        when(groupRequestMapper.toModelList(requestedList)).thenReturn(groupRequestsRequested);
        when(budgetFeignClient.getBudgetNameByGroupId(anyString())).thenReturn("Test Budget");

        List<GroupRequestWithBudgetNameDto> result = retrieveGroupRequestProcessor.retrieveGroupRequestWithBudgetNameByUsername(username);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(groupRequestRepository).findAllByRequestingUser_Username(username);
        verify(groupRequestRepository).findAllByRequestedUser_Username(username);
        verify(groupRequestMapper, times(2)).toModelList(anyList());
    }

}