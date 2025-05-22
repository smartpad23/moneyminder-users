package com.moneyminder.moneyminderusers.mappers;

import com.moneyminder.moneyminderusers.models.Session;
import com.moneyminder.moneyminderusers.persistence.entities.SessionEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    @Mapping(source = "user.username", target = "user")
    Session toModel(SessionEntity entity);

    @Mapping(source = "user", target = "user", qualifiedByName = "mapUsernameToUser")
    SessionEntity toEntity(Session model);

    List<Session> toModelList(List<SessionEntity> entities);

    List<SessionEntity> toEntityList(List<Session> models);

    @Named("mapUsernameToUser")
    default UserEntity mapUsernameToUser(String username) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        return user;
    }
}
