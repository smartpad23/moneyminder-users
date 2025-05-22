package com.moneyminder.moneyminderusers.processors.group;

import com.moneyminder.moneyminderusers.dto.CreateGroupByUsernameDto;
import com.moneyminder.moneyminderusers.mappers.GroupMapper;
import com.moneyminder.moneyminderusers.models.Group;
import com.moneyminder.moneyminderusers.persistence.entities.GroupEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import com.moneyminder.moneyminderusers.persistence.repositories.GroupRepository;
import com.moneyminder.moneyminderusers.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaveGroupProcessor {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMapper groupMapper;

    public Group saveGroup(final Group group) {
        this.checkGroupAttributes(group);
        final GroupEntity groupEntity = this.groupMapper.toEntity(group);

        if (group.getUsers() != null && !group.getUsers().isEmpty()) {
            groupEntity.setUsers(this.userRepository.findAllByUsernameInOrEmailIn(group.getUsers(), group.getUsers()));
        }

        return this.groupMapper.toModel(this.groupRepository.save(groupEntity));
    }

    public List<Group> saveGroupList(List<Group> groups) {
        final List<Group> savedGroups = new ArrayList<>();

        groups.forEach(group -> savedGroups.add(this.saveGroup(group)));

        return savedGroups;
    }

    public String createGroupByUsername(final CreateGroupByUsernameDto groupData) {
        final UserEntity user = this.userRepository.findByUsername(groupData.getUsername()).orElseThrow();
        final GroupEntity newGroup = new GroupEntity();
        newGroup.setName(groupData.getGroupName() + " group");
        newGroup.setUsers(List.of(user));
        newGroup.setGroupRequests(new ArrayList<>());

        this.groupRepository.save(newGroup);

        return newGroup.getId();
    }

    public Group updateGroup(final String id, final Group group) {
        Assert.isTrue(group.getId().equals(id), "Group id does not match");
        this.checkGroupAttributes(group);
        final GroupEntity groupEntity = this.groupMapper.toEntity(group);

        if (group.getUsers() != null && !group.getUsers().isEmpty()) {
            groupEntity.setUsers(this.userRepository.findAllByUsernameInOrEmailIn(group.getUsers(), group.getUsers()));
        }

        return this.groupMapper.toModel(this.groupRepository.save(groupEntity));
    }

    public List<Group> updateGroupList(List<Group> groups) {
        final List<Group> savedGroups = new ArrayList<>();

        groups.forEach(group -> savedGroups.add(this.updateGroup(group.getId(), group)));

        return savedGroups;
    }

    private void checkGroupAttributes(Group group) {
        Assert.notNull(group.getName(), "Group name cannot be null");
        Assert.hasLength(group.getName(), "Group name cannot be empty");
    }
}
