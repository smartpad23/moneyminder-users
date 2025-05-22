package com.moneyminder.moneyminderusers.mappers;

import com.moneyminder.moneyminderusers.models.Session;
import com.moneyminder.moneyminderusers.persistence.entities.SessionEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SessionMapperTest {

    private final SessionMapper mapper = Mappers.getMapper(SessionMapper.class);

    @Test
    @DisplayName("Entity to Model mapping test")
    void testEntityToModel() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");

        SessionEntity entity = new SessionEntity();
        entity.setId("session-uuid-123");
        entity.setToken("securetoken123");
        entity.setCreationDate(LocalDate.of(2025, 4, 26));
        entity.setExpirationDate(LocalDate.of(2025, 5, 26));
        entity.setUser(userEntity);

        Session model = mapper.toModel(entity);

        assertEquals("session-uuid-123", model.getId());
        assertEquals("securetoken123", model.getToken());
        assertEquals(LocalDate.of(2025, 4, 26), model.getCreationDate());
        assertEquals(LocalDate.of(2025, 5, 26), model.getExpirationDate());
        assertEquals("testUser", model.getUser());
    }

    @Test
    @DisplayName("Model to Entity mapping test")
    void testModelToEntity() {
        Session model = Session.builder()
                .id("session-uuid-456")
                .token("othertoken456")
                .creationDate(LocalDate.of(2025, 6, 1))
                .expirationDate(LocalDate.of(2025, 7, 1))
                .user("anotherUser")
                .build();

        SessionEntity entity = mapper.toEntity(model);

        assertEquals("session-uuid-456", entity.getId());
        assertEquals("othertoken456", entity.getToken());
        assertEquals(LocalDate.of(2025, 6, 1), entity.getCreationDate());
        assertEquals(LocalDate.of(2025, 7, 1), entity.getExpirationDate());
        assertNotNull(entity.getUser());
        assertEquals("anotherUser", entity.getUser().getUsername());
    }

    @Test
    @DisplayName("Map Username to UserEntity")
    void testMapUsernameToUser() {
        UserEntity user = mapper.mapUsernameToUser("usernameTest");

        assertNotNull(user);
        assertEquals("usernameTest", user.getUsername());
    }

}