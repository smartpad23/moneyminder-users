package com.moneyminder.moneyminderusers.mappers;

import com.moneyminder.moneyminderusers.models.Group;
import com.moneyminder.moneyminderusers.persistence.entities.GroupEntity;
import com.moneyminder.moneyminderusers.persistence.entities.GroupRequestEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupMapperTest {

    private final GroupMapper mapper = Mappers.getMapper(GroupMapper.class);

    @Test
    @DisplayName("Entity to Model mapping test")
    void testEntityToModel() {
        GroupEntity entity = new GroupEntity();
        entity.setId("group-id-123");
        entity.setName("Test Group");

        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");

        UserEntity user2 = new UserEntity();
        user2.setUsername("user2");

        GroupRequestEntity request1 = new GroupRequestEntity();
        request1.setId("request1");

        entity.setUsers(List.of(user1, user2));
        entity.setGroupRequests(List.of(request1));

        Group model = mapper.toModel(entity);

        assertEquals("group-id-123", model.getId());
        assertEquals("Test Group", model.getName());
        assertEquals(List.of("user1", "user2"), model.getUsers());
        assertEquals(List.of("request1"), model.getGroupRequests());
    }

    @Test
    @DisplayName("Model to Entity mapping test")
    void testModelToEntity() {
        Group model = Group.builder()
                .id("group-id-456")
                .name("Another Group")
                .users(List.of("userA", "userB"))
                .groupRequests(List.of("requestX"))
                .build();

        GroupEntity entity = mapper.toEntity(model);

        assertEquals("group-id-456", entity.getId());
        assertEquals("Another Group", entity.getName());
        assertEquals(2, entity.getUsers().size());
        assertEquals("userA", entity.getUsers().get(0).getUsername());
        assertEquals(1, entity.getGroupRequests().size());
        assertEquals("requestX", entity.getGroupRequests().get(0).getId());
    }

    @Test
    @DisplayName("Map Users to Usernames")
    void testMapUsersToUsernames() {
        UserEntity user1 = new UserEntity();
        user1.setUsername("user1");
        UserEntity user2 = new UserEntity();
        user2.setUsername("user2");

        List<String> usernames = mapper.mapUsersToUsernames(List.of(user1, user2));

        assertEquals(List.of("user1", "user2"), usernames);
    }

    @Test
    @DisplayName("Map Usernames to Users")
    void testMapUsernamesToUsers() {
        List<UserEntity> users = mapper.mapUsernamesToUsers(List.of("user1", "user2"));

        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
    }

    @Test
    @DisplayName("Map GroupRequests to IDs")
    void testMapGroupRequestsToIds() {
        GroupRequestEntity req1 = new GroupRequestEntity();
        req1.setId("req1");
        GroupRequestEntity req2 = new GroupRequestEntity();
        req2.setId("req2");

        List<String> ids = mapper.mapGroupRequestsToIds(List.of(req1, req2));

        assertEquals(List.of("req1", "req2"), ids);
    }

    @Test
    @DisplayName("Map IDs to GroupRequests")
    void testMapIdsToGroupRequests() {
        List<GroupRequestEntity> groupRequests = mapper.mapIdsToGroupRequests(List.of("id1", "id2"));

        assertEquals(2, groupRequests.size());
        assertEquals("id1", groupRequests.get(0).getId());
        assertEquals("id2", groupRequests.get(1).getId());
    }

}