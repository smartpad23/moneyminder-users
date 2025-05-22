package com.moneyminder.moneyminderusers.mappers;

import com.moneyminder.moneyminderusers.models.Group;
import com.moneyminder.moneyminderusers.persistence.entities.GroupEntity;
import com.moneyminder.moneyminderusers.persistence.entities.GroupRequestEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    @Mapping(source = "users", target = "users", qualifiedByName = "mapUsersToUsernames")
    @Mapping(source = "groupRequests", target = "groupRequests", qualifiedByName = "mapGroupRequestsToIds")
    Group toModel(GroupEntity entity);

    @Mapping(source = "users", target = "users", qualifiedByName = "mapUsernamesToUsers")
    @Mapping(source = "groupRequests", target = "groupRequests", qualifiedByName = "mapIdsToGroupRequests")
    GroupEntity toEntity(Group model);

    List<Group> toModelList(List<GroupEntity> entities);

    List<GroupEntity> toEntityList(List<Group> models);

    @Named("mapUsersToUsernames")
    default List<String> mapUsersToUsernames(List<UserEntity> users) {
        return users.stream().map(UserEntity::getUsername).collect(Collectors.toList());
    }

    @Named("mapUsernamesToUsers")
    default List<UserEntity> mapUsernamesToUsers(List<String> usernames) {
        if (usernames == null || usernames.isEmpty()) {
            return new ArrayList<>();
        }

        return usernames.stream().map(username -> {
            UserEntity user = new UserEntity();
            user.setUsername(username);
            return user;
        }).collect(Collectors.toList());
    }

    @Named("mapGroupRequestsToIds")
    default List<String> mapGroupRequestsToIds(List<GroupRequestEntity> groupRequests) {
        return groupRequests.stream().map(GroupRequestEntity::getId).collect(Collectors.toList());
    }

    @Named("mapIdsToGroupRequests")
    default List<GroupRequestEntity> mapIdsToGroupRequests(List<String> groupRequestIds) {
        if (groupRequestIds == null || groupRequestIds.isEmpty()) {
            return new ArrayList<>();
        }

        return groupRequestIds.stream().map(id -> {
            GroupRequestEntity groupRequest = new GroupRequestEntity();
            groupRequest.setId(id);
            return groupRequest;
        }).collect(Collectors.toList());
    }
}
