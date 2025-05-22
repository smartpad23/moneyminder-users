package com.moneyminder.moneyminderusers.processors.groupRequest;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.moneyminder.moneyminderusers.dto.GroupRequestWithBudgetNameDto;
import com.moneyminder.moneyminderusers.feignClients.BudgetFeignClient;
import com.moneyminder.moneyminderusers.mappers.GroupRequestMapper;
import com.moneyminder.moneyminderusers.models.GroupRequest;
import com.moneyminder.moneyminderusers.persistence.entities.GroupEntity;
import com.moneyminder.moneyminderusers.persistence.entities.GroupRequestEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import com.moneyminder.moneyminderusers.persistence.repositories.GroupRepository;
import com.moneyminder.moneyminderusers.persistence.repositories.GroupRequestRepository;
import com.moneyminder.moneyminderusers.persistence.repositories.UserRepository;
import com.moneyminder.moneyminderusers.processors.user.RetrieveUserProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.*;

class SaveGroupRequestProcessorTest {

    @Mock
    private GroupRequestRepository groupRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupRequestMapper groupRequestMapper;

    @Mock
    private RetrieveUserProcessor retrieveUserProcessor;

    @Mock
    private BudgetFeignClient budgetFeignClient;

    @InjectMocks
    private SaveGroupRequestProcessor saveGroupRequestProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("save group request test")
    void saveGroupRequestTest() {
        GroupRequest groupRequest = GroupRequest.builder()
                .id(UUID.randomUUID().toString())
                .group(UUID.randomUUID().toString())
                .requestingUser("requestingUser")
                .requestedUser("requestedUser")
                .build();

        GroupRequestEntity groupRequestEntity = new GroupRequestEntity();
        GroupRequestEntity savedEntity = new GroupRequestEntity();
        GroupRequest savedModel = new GroupRequest();

        when(groupRequestMapper.toEntity(groupRequest)).thenReturn(groupRequestEntity);
        when(groupRepository.findById(groupRequest.getGroup())).thenReturn(Optional.of(new GroupEntity()));
        when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(new UserEntity()));
        when(groupRequestRepository.save(any(GroupRequestEntity.class))).thenReturn(savedEntity);
        when(groupRequestMapper.toModel(savedEntity)).thenReturn(savedModel);
        when(budgetFeignClient.getBudgetNameByGroupId(anyString())).thenReturn("Test Budget");

        GroupRequestWithBudgetNameDto result = saveGroupRequestProcessor.saveGroupRequest(groupRequest);

        assertNotNull(result);
    }

    @Test
    @DisplayName("save group request list test")
    void saveGroupRequestListTest() {
        GroupRequest groupRequest = GroupRequest.builder()
                .id(UUID.randomUUID().toString())
                .group(UUID.randomUUID().toString())
                .requestingUser("requestingUser")
                .requestedUser("requestedUser")
                .build();

        SaveGroupRequestProcessor spyProcessor = spy(saveGroupRequestProcessor);

        doReturn(new GroupRequestWithBudgetNameDto()).when(spyProcessor).saveGroupRequest(any(GroupRequest.class));

        List<GroupRequestWithBudgetNameDto> result = spyProcessor.saveGroupRequestList(List.of(groupRequest));

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(spyProcessor).saveGroupRequest(any(GroupRequest.class));
    }

    @Test
    @DisplayName("update group request with accepted true should add user to group test")
    void updateGroupRequestAcceptedShouldAddUserTest() {
        String id = UUID.randomUUID().toString();
        String groupId = UUID.randomUUID().toString();

        GroupRequest groupRequest = GroupRequest.builder()
                .id(id)
                .group(groupId)
                .requestingUser("requestingUser")
                .requestedUser("requestedUser")
                .accepted(true)
                .build();

        GroupEntity groupEntityDb = new GroupEntity();
        groupEntityDb.setId(groupId);
        groupEntityDb.setUsers(new ArrayList<>());

        GroupRequestEntity dbEntity = new GroupRequestEntity();
        dbEntity.setGroup(groupEntityDb);
        dbEntity.setDate(LocalDate.now());
        dbEntity.setAccepted(null);

        when(groupRequestRepository.findById(id)).thenReturn(Optional.of(dbEntity));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(groupEntityDb));
        when(groupRequestMapper.toEntity(any(GroupRequest.class))).thenReturn(new GroupRequestEntity());
        when(userRepository.findByUsernameOrEmail(anyString(), anyString())).thenReturn(Optional.of(new UserEntity()));
        when(groupRequestRepository.save(any(GroupRequestEntity.class))).thenReturn(new GroupRequestEntity());
        when(groupRequestMapper.toModel(any(GroupRequestEntity.class))).thenReturn(new GroupRequest());
        when(budgetFeignClient.getBudgetNameByGroupId(anyString())).thenReturn("Test Budget");
        when(retrieveUserProcessor.retrieveCompleteUserByUsernameOrEmail(anyString())).thenReturn(new UserEntity());

        GroupRequestWithBudgetNameDto result = saveGroupRequestProcessor.updateGroupRequest(id, groupRequest);

        assertNotNull(result);
        assertEquals(1, groupEntityDb.getUsers().size());
        verify(groupRequestRepository).findById(id);
        verify(groupRequestRepository).save(any(GroupRequestEntity.class));
        verify(groupRepository).save(groupEntityDb);
        verify(retrieveUserProcessor).retrieveCompleteUserByUsernameOrEmail("requestedUser");
    }

    @Test
    @DisplayName("update group request id mismatch test")
    void updateGroupRequestIdMismatchTest() {
        String id = UUID.randomUUID().toString();
        String differentId = UUID.randomUUID().toString();
        GroupRequest groupRequest = GroupRequest.builder()
                .id(differentId)
                .group(UUID.randomUUID().toString())
                .requestingUser("requestingUser")
                .requestedUser("requestedUser")
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveGroupRequestProcessor.updateGroupRequest(id, groupRequest));

        assertEquals("Group request don't match", exception.getMessage());
    }

    @Test
    @DisplayName("save group request with null groupRequest test")
    void saveGroupRequestWithNullGroupRequestShouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveGroupRequestProcessor.saveGroupRequest(null));

        assertEquals("GroupRequest cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("save group request with null requestedUser test")
    void saveGroupRequestWithNullRequestedUserShouldThrowException() {
        GroupRequest groupRequest = GroupRequest.builder()
                .group(UUID.randomUUID().toString())
                .requestingUser("requestingUser")
                .requestedUser(null)
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveGroupRequestProcessor.saveGroupRequest(groupRequest));

        assertEquals("RequestedUser cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("save group request with empty requestedUser test")
    void saveGroupRequestWithEmptyRequestedUserShouldThrowException() {
        GroupRequest groupRequest = GroupRequest.builder()
                .group(UUID.randomUUID().toString())
                .requestingUser("requestingUser")
                .requestedUser("")
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveGroupRequestProcessor.saveGroupRequest(groupRequest));

        assertEquals("RequestedUser cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("save group request with null requestingUser test")
    void saveGroupRequestWithNullRequestingUserShouldThrowException() {
        GroupRequest groupRequest = GroupRequest.builder()
                .group(UUID.randomUUID().toString())
                .requestingUser(null)
                .requestedUser("requestedUser")
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveGroupRequestProcessor.saveGroupRequest(groupRequest));

        assertEquals("RequestingUser cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("save group request with empty requestingUser test")
    void saveGroupRequestWithEmptyRequestingUserShouldThrowException() {
        GroupRequest groupRequest = GroupRequest.builder()
                .group(UUID.randomUUID().toString())
                .requestingUser("")
                .requestedUser("requestedUser")
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveGroupRequestProcessor.saveGroupRequest(groupRequest));

        assertEquals("RequestingUser cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("save group request with non existing group test")
    void saveGroupRequestWithNonExistingGroupShouldThrowException() {
        GroupRequest groupRequest = GroupRequest.builder()
                .id(UUID.randomUUID().toString())
                .group(UUID.randomUUID().toString())
                .requestingUser("requestingUser")
                .requestedUser("requestedUser")
                .build();

        when(groupRequestMapper.toEntity(groupRequest)).thenReturn(new GroupRequestEntity());
        when(groupRepository.findById(groupRequest.getGroup())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveGroupRequestProcessor.saveGroupRequest(groupRequest));

        assertEquals("Group not found", exception.getMessage());
    }

    @Test
    @DisplayName("save group request with non existing requesting user test")
    void saveGroupRequestWithNonExistingRequestingUserShouldThrowException() {
        GroupRequest groupRequest = GroupRequest.builder()
                .id(UUID.randomUUID().toString())
                .group(UUID.randomUUID().toString())
                .requestingUser("requestingUser")
                .requestedUser("requestedUser")
                .build();

        when(groupRequestMapper.toEntity(groupRequest)).thenReturn(new GroupRequestEntity());
        when(groupRepository.findById(groupRequest.getGroup())).thenReturn(Optional.of(new GroupEntity()));
        when(userRepository.findByUsernameOrEmail(groupRequest.getRequestingUser(), groupRequest.getRequestingUser()))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveGroupRequestProcessor.saveGroupRequest(groupRequest));

        assertEquals("Requesting user not found", exception.getMessage());
    }

    @Test
    @DisplayName("save group request with non existing requested user test")
    void saveGroupRequestWithNonExistingRequestedUserShouldThrowException() {
        GroupRequest groupRequest = GroupRequest.builder()
                .id(UUID.randomUUID().toString())
                .group(UUID.randomUUID().toString())
                .requestingUser("requestingUser")
                .requestedUser("requestedUser")
                .build();

        when(groupRequestMapper.toEntity(groupRequest)).thenReturn(new GroupRequestEntity());
        when(groupRepository.findById(groupRequest.getGroup())).thenReturn(Optional.of(new GroupEntity()));
        when(userRepository.findByUsernameOrEmail(groupRequest.getRequestingUser(), groupRequest.getRequestingUser()))
                .thenReturn(Optional.of(new UserEntity()));
        when(userRepository.findByUsernameOrEmail(groupRequest.getRequestedUser(), groupRequest.getRequestedUser()))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveGroupRequestProcessor.saveGroupRequest(groupRequest));

        assertEquals("Requested user not found", exception.getMessage());
    }

}