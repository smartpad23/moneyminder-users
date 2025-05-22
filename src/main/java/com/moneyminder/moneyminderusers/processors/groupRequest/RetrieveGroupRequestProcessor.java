package com.moneyminder.moneyminderusers.processors.groupRequest;

import com.moneyminder.moneyminderusers.dto.GroupRequestWithBudgetNameDto;
import com.moneyminder.moneyminderusers.feignClients.BudgetFeignClient;
import com.moneyminder.moneyminderusers.mappers.GroupRequestMapper;
import com.moneyminder.moneyminderusers.models.GroupRequest;
import com.moneyminder.moneyminderusers.persistence.repositories.GroupRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class RetrieveGroupRequestProcessor {
    private final GroupRequestRepository groupRequestRepository;
    private final GroupRequestMapper groupRequestMapper;
    private final BudgetFeignClient budgetFeignClient;

    public List<GroupRequest> retrieveGroupRequest() {
        return this.groupRequestMapper.toModelList(StreamSupport.stream(this.groupRequestRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()));
    }

    public List<GroupRequestWithBudgetNameDto> retrieveGroupRequestWithBudgetNameByUsername(String username) {
        final List<GroupRequestWithBudgetNameDto> groupsList = new ArrayList<>();

        final List<GroupRequest> groups = this.groupRequestMapper.toModelList(this.groupRequestRepository.findAllByRequestingUser_Username(username));
        groups.addAll(this.groupRequestMapper.toModelList(this.groupRequestRepository.findAllByRequestedUser_Username(username)));

        groups.forEach(group -> {
            final GroupRequestWithBudgetNameDto groupRequestWithBudgetName = new GroupRequestWithBudgetNameDto();
            groupRequestWithBudgetName.groupToWithBudgetName(group);

            final String budgetName = budgetFeignClient.getBudgetNameByGroupId(group.getGroup());
            groupRequestWithBudgetName.setBudgetName(budgetName);

            groupsList.add(groupRequestWithBudgetName);
        });

        return groupsList;
    }

    public GroupRequest retrieveGroupRequestById(String id) {
        return this.groupRequestMapper.toModel(groupRequestRepository.findById(id).orElseThrow());
    }
}
