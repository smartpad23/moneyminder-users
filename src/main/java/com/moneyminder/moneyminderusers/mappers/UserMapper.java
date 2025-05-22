package com.moneyminder.moneyminderusers.mappers;

import com.moneyminder.moneyminderusers.models.User;
import com.moneyminder.moneyminderusers.persistence.entities.GroupEntity;
import com.moneyminder.moneyminderusers.persistence.entities.GroupRequestEntity;
import com.moneyminder.moneyminderusers.persistence.entities.SessionEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "groups", target = "groups", qualifiedByName = "mapGroupsToIds")
    @Mapping(source = "sessions", target = "sessions", qualifiedByName = "mapSessionsToIds")
    @Mapping(source = "requestingGroups", target = "requestingGroups", qualifiedByName = "mapRequestGroupsToIds")
    @Mapping(source = "requestedGroups", target = "requestedGroups", qualifiedByName = "mapRequestGroupsToIds")
    User toModel(UserEntity entity);

    @Mapping(source = "groups", target = "groups", qualifiedByName = "mapIdsToGroups")
    @Mapping(source = "sessions", target = "sessions", qualifiedByName = "mapIdsToSessions")
    @Mapping(source = "requestingGroups", target = "requestingGroups", qualifiedByName = "mapIdsToRequestGroups")
    @Mapping(source = "requestedGroups", target = "requestedGroups", qualifiedByName = "mapIdsToRequestGroups")
    UserEntity toEntity(User model);

    List<User> toModelList(List<UserEntity> entities);

    List<UserEntity> toEntityList(List<User> models);

    @Named("mapGroupsToIds")
    default List<String> mapGroupsToIds(List<GroupEntity> groups) {
        return groups.stream().map(GroupEntity::getId).collect(Collectors.toList());
    }

    @Named("mapIdsToGroups")
    default List<GroupEntity> mapIdsToGroups(List<String> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            return new ArrayList<>();
        }

        return groupIds.stream().map(id -> {
            GroupEntity group = new GroupEntity();
            group.setId(id);
            return group;
        }).collect(Collectors.toList());
    }

    @Named("mapSessionsToIds")
    default List<String> mapSessionsToIds(List<SessionEntity> sessions) {
        return sessions.stream().map(SessionEntity::getId).collect(Collectors.toList());
    }

    @Named("mapIdsToSessions")
    default List<SessionEntity> mapIdsToSessions(List<String> sessionIds) {
        if (sessionIds == null || sessionIds.isEmpty()) {
            return new ArrayList<>();
        }

        return sessionIds.stream().map(id -> {
            SessionEntity session = new SessionEntity();
            session.setId(id);
            return session;
        }).collect(Collectors.toList());
    }

    @Named("mapRequestGroupsToIds")
    default List<String> mapRequestGroupsToIds(List<GroupRequestEntity> requestingGroups) {
        return requestingGroups.stream().map(GroupRequestEntity::getId).collect(Collectors.toList());
    }

    @Named("mapIdsToRequestGroups")
    default List<GroupRequestEntity> mapIdsToRequestGroups(List<String> requestingGroupIds) {
        if (requestingGroupIds == null || requestingGroupIds.isEmpty()) {
            return new ArrayList<>();
        }

        return requestingGroupIds.stream().map(id -> {
            GroupRequestEntity groupRequest = new GroupRequestEntity();
            groupRequest.setId(id);
            return groupRequest;
        }).collect(Collectors.toList());
    }
}
