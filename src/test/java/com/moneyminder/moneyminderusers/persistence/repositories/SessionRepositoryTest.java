package com.moneyminder.moneyminderusers.persistence.repositories;

import com.moneyminder.moneyminderusers.persistence.entities.SessionEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@DataJpaTest
class SessionRepositoryTest {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;

    @BeforeEach
    void setup() {
        user = new UserEntity();
        user.setUsername("testuser");
        user.setEmail("testuser@test.com");
        user.setPassword("password");
        user = userRepository.save(user);
    }

    @Test
    @DisplayName("save and find by ID test")
    void saveAndFindById() {
        SessionEntity session = new SessionEntity();
        session.setToken("some-token");
        session.setCreationDate(LocalDate.now());
        session.setExpirationDate(LocalDate.now().plusDays(7));
        session.setUser(user);

        SessionEntity saved = sessionRepository.save(session);

        Optional<SessionEntity> found = sessionRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(user.getUsername(), found.get().getUser().getUsername());
        assertEquals("some-token", found.get().getToken());
    }

    @Test
    @DisplayName("exists by ID test")
    void existsByIdTest() {
        SessionEntity session = new SessionEntity();
        session.setToken("another-token");
        session.setCreationDate(LocalDate.now());
        session.setExpirationDate(LocalDate.now().plusDays(7));
        session.setUser(user);

        SessionEntity saved = sessionRepository.save(session);

        boolean exists = sessionRepository.existsById(saved.getId());

        assertTrue(exists);
    }

    @Test
    @DisplayName("find all by IDs test")
    void findAllByIdInTest() {
        SessionEntity session1 = new SessionEntity();
        session1.setToken("token-1");
        session1.setCreationDate(LocalDate.now());
        session1.setExpirationDate(LocalDate.now().plusDays(7));
        session1.setUser(user);
        sessionRepository.save(session1);

        SessionEntity session2 = new SessionEntity();
        session2.setToken("token-2");
        session2.setCreationDate(LocalDate.now());
        session2.setExpirationDate(LocalDate.now().plusDays(7));
        session2.setUser(user);
        sessionRepository.save(session2);

        List<String> ids = List.of(session1.getId(), session2.getId());

        List<SessionEntity> sessions = sessionRepository.findAllByIdIn(ids);

        assertEquals(2, sessions.size());
    }

}