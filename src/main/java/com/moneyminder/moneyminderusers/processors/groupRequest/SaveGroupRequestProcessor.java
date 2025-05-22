package com.moneyminder.moneyminderusers.processors.groupRequest;

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
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaveGroupRequestProcessor {
    private final GroupRequestRepository groupRequestRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupRequestMapper groupRequestMapper;
    private final RetrieveUserProcessor retrieveUserProcessor;
    private final BudgetFeignClient budgetFeignClient;

    public GroupRequestWithBudgetNameDto saveGroupRequest(final GroupRequest groupRequest) {
        this.checkGroupRequestAttributes(groupRequest);
        Assert.notNull(groupRequest.getGroup(), "Group cannot be null");
        Assert.hasLength(groupRequest.getGroup(), "Group cannot be empty");

        final GroupRequestEntity groupRequestEntity = this.checkValues(groupRequest);

        groupRequestEntity.setDate(LocalDate.now());

        final GroupRequestWithBudgetNameDto groupRequestWithBudgetName = new GroupRequestWithBudgetNameDto();
        final GroupRequest groupReq = this.groupRequestMapper.toModel(this.groupRequestRepository.save(groupRequestEntity));

        groupRequestWithBudgetName.groupToWithBudgetName(groupReq);
        final String budgetName = budgetFeignClient.getBudgetNameByGroupId(groupReq.getGroup());
        groupRequestWithBudgetName.setBudgetName(budgetName);
        return groupRequestWithBudgetName;
    }

    public List<GroupRequestWithBudgetNameDto> saveGroupRequestList(final List<GroupRequest> groupRequestList) {
        List<GroupRequestWithBudgetNameDto> savedGroupRequestList = new ArrayList<>();

        if (groupRequestList != null && !groupRequestList.isEmpty()) {
            groupRequestList.forEach(groupRequest -> savedGroupRequestList.add(this.saveGroupRequest(groupRequest)));
        }

        return savedGroupRequestList;
    }

    public GroupRequestWithBudgetNameDto updateGroupRequest(final String id, final GroupRequest groupRequest) {
        Assert.isTrue(groupRequest.getId().equals(id), "Group request don't match");
        this.checkGroupRequestAttributes(groupRequest);

        final GroupRequestEntity groupRequestEntityFromDb = this.groupRequestRepository.findById(id).orElseThrow();
        groupRequest.setGroup(groupRequestEntityFromDb.getGroup().getId());

        final GroupRequestEntity groupRequestEntity = this.checkValues(groupRequest);

        groupRequestEntity.setDate(groupRequestEntityFromDb.getDate());

        if (groupRequest.getAccepted() && groupRequestEntityFromDb.getAccepted() == null) {
            final GroupEntity requestGroup = groupRequestEntity.getGroup();
            final UserEntity requestedUser = this.retrieveUserProcessor.retrieveCompleteUserByUsernameOrEmail(groupRequest.getRequestedUser());
            requestGroup.getUsers().add(requestedUser);
            groupRequestEntity.setResolvedDate(LocalDate.now());
            this.groupRepository.save(requestGroup);
        }

        final GroupRequestWithBudgetNameDto groupRequestWithBudgetName = new GroupRequestWithBudgetNameDto();
        final GroupRequest groupReq = this.groupRequestMapper.toModel(this.groupRequestRepository.save(groupRequestEntity));

        groupRequestWithBudgetName.groupToWithBudgetName(groupReq);
        final String budgetName = budgetFeignClient.getBudgetNameByGroupId(groupReq.getGroup());
        groupRequestWithBudgetName.setBudgetName(budgetName);
        return groupRequestWithBudgetName;
    }

    public List<GroupRequestWithBudgetNameDto> updateGroupRequestList(final List<GroupRequest> groupRequestList) {
        List<GroupRequestWithBudgetNameDto> updatedGroupRequestList = new ArrayList<>();

        if (groupRequestList != null && !groupRequestList.isEmpty()) {
            groupRequestList.forEach(groupRequest -> updatedGroupRequestList.add(this.updateGroupRequest(groupRequest.getId(), groupRequest)));
        }

        return updatedGroupRequestList;
    }

    private void checkGroupRequestAttributes(final GroupRequest groupRequest) {
        Assert.notNull(groupRequest, "GroupRequest cannot be null");
        Assert.hasLength(groupRequest.getRequestedUser(), "RequestedUser cannot be empty");
        Assert.notNull(groupRequest.getRequestedUser(), "RequestedUser cannot be null");
        Assert.notNull(groupRequest.getRequestingUser(), "RequestingUser cannot be null");
        Assert.hasLength(groupRequest.getRequestingUser(), "RequestingUser cannot be empty");
    }

    private GroupRequestEntity checkValues(final GroupRequest groupRequest) {
        final GroupRequestEntity groupRequestEntity = this.groupRequestMapper.toEntity(groupRequest);

        final GroupEntity groupEntity = this.groupRepository.findById(groupRequest.getGroup()).orElse(null);
        Assert.notNull(groupEntity, "Group not found");

        final UserEntity requestingUserEntity = this.userRepository.findByUsernameOrEmail(groupRequest
                .getRequestingUser(), groupRequest.getRequestingUser()).orElse(null);
        Assert.notNull(requestingUserEntity, "Requesting user not found");

        final UserEntity requestedUserEntity = this.userRepository.findByUsernameOrEmail(groupRequest
                .getRequestedUser(), groupRequest.getRequestedUser()).orElse(null);
        Assert.notNull(requestedUserEntity, "Requested user not found");

        groupRequestEntity.setGroup(groupEntity);
        groupRequestEntity.setRequestingUser(requestingUserEntity);
        groupRequestEntity.setRequestedUser(requestedUserEntity);

        return groupRequestEntity;
    }
}
