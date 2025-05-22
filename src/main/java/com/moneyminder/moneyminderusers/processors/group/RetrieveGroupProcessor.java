package com.moneyminder.moneyminderusers.processors.group;

import com.moneyminder.moneyminderusers.mappers.GroupMapper;
import com.moneyminder.moneyminderusers.models.Group;
import com.moneyminder.moneyminderusers.persistence.entities.GroupEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import com.moneyminder.moneyminderusers.persistence.repositories.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class RetrieveGroupProcessor {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public List<Group> retrieveGroups() {
        return this.groupMapper.toModelList(StreamSupport.stream(this.groupRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()));
    }

    public Group retrieveGroupById(String groupId) {
        return this.groupMapper.toModel(this.groupRepository.findById(groupId).orElseThrow());
    }

    public List<String> retrieveGroupIdsByUsername(String username) {
        return this.groupRepository.findAllByUsers_Username(username).stream().map(GroupEntity::getId).collect(Collectors.toList());
    }

    public List<String> retrieveUsernameOfGroup(String groupId) {
        final GroupEntity groupEntity = this.groupRepository.findById(groupId).orElseThrow();
        return groupEntity.getUsers().stream().map(UserEntity::getUsername).collect(Collectors.toList());
    }
}
