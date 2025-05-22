package com.moneyminder.moneyminderusers.mappers;

import com.moneyminder.moneyminderusers.models.User;
import com.moneyminder.moneyminderusers.persistence.entities.GroupEntity;
import com.moneyminder.moneyminderusers.persistence.entities.GroupRequestEntity;
import com.moneyminder.moneyminderusers.persistence.entities.SessionEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("Entity to Model mapping test")
    void testEntityToModel() {
        GroupEntity group = new GroupEntity();
        group.setId("groupId1");

        SessionEntity session = new SessionEntity();
        session.setId("sessionId1");

        GroupRequestEntity requestingGroup = new GroupRequestEntity();
        requestingGroup.setId("requestingGroupId1");

        GroupRequestEntity requestedGroup = new GroupRequestEntity();
        requestedGroup.setId("requestedGroupId1");

        UserEntity entity = new UserEntity();
        entity.setUsername("testUser");
        entity.setEmail("testuser@example.com");
        entity.setPassword("password123");
        entity.setGroups(List.of(group));
        entity.setSessions(List.of(session));
        entity.setRequestingGroups(List.of(requestingGroup));
        entity.setRequestedGroups(List.of(requestedGroup));

        User model = mapper.toModel(entity);

        assertEquals("testUser", model.getUsername());
        assertEquals("testuser@example.com", model.getEmail());
        assertEquals("password123", model.getPassword());
        assertEquals(List.of("groupId1"), model.getGroups());
        assertEquals(List.of("sessionId1"), model.getSessions());
        assertEquals(List.of("requestingGroupId1"), model.getRequestingGroups());
        assertEquals(List.of("requestedGroupId1"), model.getRequestedGroups());
    }

    @Test
    @DisplayName("Model to Entity mapping test")
    void testModelToEntity() {
        User model = User.builder()
                .username("modelUser")
                .email("modeluser@example.com")
                .password("modelPassword")
                .groups(List.of("groupId2"))
                .sessions(List.of("sessionId2"))
                .requestingGroups(List.of("requestingGroupId2"))
                .requestedGroups(List.of("requestedGroupId2"))
                .build();

        UserEntity entity = mapper.toEntity(model);

        assertEquals("modelUser", entity.getUsername());
        assertEquals("modeluser@example.com", entity.getEmail());
        assertEquals("modelPassword", entity.getPassword());
        assertEquals(1, entity.getGroups().size());
        assertEquals("groupId2", entity.getGroups().get(0).getId());
        assertEquals(1, entity.getSessions().size());
        assertEquals("sessionId2", entity.getSessions().get(0).getId());
        assertEquals(1, entity.getRequestingGroups().size());
        assertEquals("requestingGroupId2", entity.getRequestingGroups().get(0).getId());
        assertEquals(1, entity.getRequestedGroups().size());
        assertEquals("requestedGroupId2", entity.getRequestedGroups().get(0).getId());
    }

    @Test
    @DisplayName("Map Groups to IDs")
    void testMapGroupsToIds() {
        GroupEntity group1 = new GroupEntity();
        group1.setId("group1");
        GroupEntity group2 = new GroupEntity();
        group2.setId("group2");

        List<String> ids = mapper.mapGroupsToIds(List.of(group1, group2));

        assertEquals(List.of("group1", "group2"), ids);
    }

    @Test
    @DisplayName("Map IDs to Groups")
    void testMapIdsToGroups() {
        List<GroupEntity> groups = mapper.mapIdsToGroups(List.of("id1", "id2"));

        assertEquals(2, groups.size());
        assertEquals("id1", groups.get(0).getId());
        assertEquals("id2", groups.get(1).getId());
    }

    @Test
    @DisplayName("Map Sessions to IDs")
    void testMapSessionsToIds() {
        SessionEntity session1 = new SessionEntity();
        session1.setId("session1");
        SessionEntity session2 = new SessionEntity();
        session2.setId("session2");

        List<String> ids = mapper.mapSessionsToIds(List.of(session1, session2));

        assertEquals(List.of("session1", "session2"), ids);
    }

    @Test
    @DisplayName("Map IDs to Sessions")
    void testMapIdsToSessions() {
        List<SessionEntity> sessions = mapper.mapIdsToSessions(List.of("sess1", "sess2"));

        assertEquals(2, sessions.size());
        assertEquals("sess1", sessions.get(0).getId());
        assertEquals("sess2", sessions.get(1).getId());
    }

    @Test
    @DisplayName("Map RequestGroups to IDs")
    void testMapRequestGroupsToIds() {
        GroupRequestEntity req1 = new GroupRequestEntity();
        req1.setId("req1");
        GroupRequestEntity req2 = new GroupRequestEntity();
        req2.setId("req2");

        List<String> ids = mapper.mapRequestGroupsToIds(List.of(req1, req2));

        assertEquals(List.of("req1", "req2"), ids);
    }

    @Test
    @DisplayName("Map IDs to RequestGroups")
    void testMapIdsToRequestGroups() {
        List<GroupRequestEntity> requests = mapper.mapIdsToRequestGroups(List.of("r1", "r2"));

        assertEquals(2, requests.size());
        assertEquals("r1", requests.get(0).getId());
        assertEquals("r2", requests.get(1).getId());
    }

}