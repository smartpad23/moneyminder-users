package com.moneyminder.moneyminderusers.processors.group;

import com.moneyminder.moneyminderusers.mappers.GroupMapper;
import com.moneyminder.moneyminderusers.persistence.repositories.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteGroupProcessor {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public void deleteGroup(String groupId) {
        Assert.isTrue(groupRepository.existsById(groupId), "Group not found");
        this.groupRepository.deleteById(groupId);
    }

    public void deleteGroupList(List<String> groupIds) {
        groupIds.forEach(this::deleteGroup);
    }
}
