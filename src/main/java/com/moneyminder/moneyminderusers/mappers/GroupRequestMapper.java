package com.moneyminder.moneyminderusers.mappers;

import com.moneyminder.moneyminderusers.models.GroupRequest;
import com.moneyminder.moneyminderusers.persistence.entities.GroupEntity;
import com.moneyminder.moneyminderusers.persistence.entities.GroupRequestEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupRequestMapper {
    @Mapping(source = "group.id", target = "group")
    @Mapping(source = "requestingUser.username", target = "requestingUser")
    @Mapping(source = "requestedUser.username", target = "requestedUser")
    GroupRequest toModel(GroupRequestEntity entity);

    @Mapping(source = "group", target = "group", qualifiedByName = "mapGroupIdToGroup")
    @Mapping(source = "requestingUser", target = "requestingUser", qualifiedByName = "mapUsernameToUser")
    @Mapping(source = "requestedUser", target = "requestedUser", qualifiedByName = "mapUsernameToUser")
    GroupRequestEntity toEntity(GroupRequest model);

    List<GroupRequest> toModelList(List<GroupRequestEntity> entities);

    List<GroupRequestEntity> toEntityList(List<GroupRequest> models);

    @Named("mapGroupIdToGroup")
    default GroupEntity mapGroupIdToGroup(String groupId) {
        GroupEntity group = new GroupEntity();
        group.setId(groupId);
        return group;
    }

    @Named("mapUsernameToUser")
    default UserEntity mapUsernameToUser(String username) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        return user;
    }
}
