package com.moneyminder.moneyminderusers.mappers;

import com.moneyminder.moneyminderusers.models.GroupRequest;
import com.moneyminder.moneyminderusers.persistence.entities.GroupEntity;
import com.moneyminder.moneyminderusers.persistence.entities.GroupRequestEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GroupRequestMapperTest {
    private final GroupRequestMapper mapper = Mappers.getMapper(GroupRequestMapper.class);

    @Test
    @DisplayName("Entity to Model mapping test")
    void testEntityToModel() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId("group-uuid-123");

        UserEntity requestingUser = new UserEntity();
        requestingUser.setUsername("userRequesting");

        UserEntity requestedUser = new UserEntity();
        requestedUser.setUsername("userRequested");

        GroupRequestEntity entity = new GroupRequestEntity();
        entity.setId("request-uuid-456");
        entity.setGroup(groupEntity);
        entity.setRequestingUser(requestingUser);
        entity.setRequestedUser(requestedUser);
        entity.setDate(LocalDate.of(2025, 4, 26));
        entity.setResolvedDate(LocalDate.of(2025, 5, 1)); // ESTE no se mapea al modelo directamente
        entity.setAccepted(true);

        GroupRequest model = mapper.toModel(entity);

        assertEquals("request-uuid-456", model.getId());
        assertEquals("group-uuid-123", model.getGroup()); // ahora sí: model.group es ID
        assertEquals("userRequesting", model.getRequestingUser());
        assertEquals("userRequested", model.getRequestedUser());
        assertEquals(LocalDate.of(2025, 4, 26), model.getDate());
        assertTrue(model.getAccepted());
    }

    @Test
    @DisplayName("Model to Entity mapping test")
    void testModelToEntity() {
        GroupRequest model = GroupRequest.builder()
                .id("model-uuid-789")
                .group("group-uuid-456")
                .requestingUser("userReq")
                .requestedUser("userRes")
                .date(LocalDate.of(2025, 6, 15))
                .accepted(false)
                .build();

        GroupRequestEntity entity = mapper.toEntity(model);

        assertEquals("model-uuid-789", entity.getId());
        assertEquals("group-uuid-456", entity.getGroup().getId());
        assertEquals("userReq", entity.getRequestingUser().getUsername());
        assertEquals("userRes", entity.getRequestedUser().getUsername());
        assertEquals(LocalDate.of(2025, 6, 15), entity.getDate());
        assertNull(entity.getResolvedDate());
        assertFalse(entity.getAccepted());
    }

    @Test
    @DisplayName("Map GroupId to GroupEntity")
    void testMapGroupIdToGroup() {
        GroupEntity group = mapper.mapGroupIdToGroup("groupIdTest");

        assertNotNull(group);
        assertEquals("groupIdTest", group.getId());
    }

    @Test
    @DisplayName("Map Username to UserEntity")
    void testMapUsernameToUser() {
        UserEntity user = mapper.mapUsernameToUser("usernameTest");

        assertNotNull(user);
        assertEquals("usernameTest", user.getUsername());
    }

}