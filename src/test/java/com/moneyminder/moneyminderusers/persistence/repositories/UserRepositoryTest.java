package com.moneyminder.moneyminderusers.persistence.repositories;

import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    void setup() {
        user1 = new UserEntity();
        user1.setUsername("user1");
        user1.setEmail("user1@test.com");
        user1.setPassword("password1");
        user1 = userRepository.save(user1);

        user2 = new UserEntity();
        user2.setUsername("user2");
        user2.setEmail("user2@test.com");
        user2.setPassword("password2");
        user2 = userRepository.save(user2);
    }

    @Test
    @DisplayName("save and find by username test")
    void saveAndFindByUsername() {
        Optional<UserEntity> found = userRepository.findByUsername(user1.getUsername());

        assertTrue(found.isPresent());
        assertEquals(user1.getEmail(), found.get().getEmail());
    }

    @Test
    @DisplayName("exists by username test")
    void existsByUsernameTest() {
        boolean exists = userRepository.existsByUsername("user1");
        boolean doesNotExist = userRepository.existsByUsername("nonexistent");

        assertTrue(exists);
        assertFalse(doesNotExist);
    }

    @Test
    @DisplayName("find by username or email test")
    void findByUsernameOrEmail() {
        Optional<UserEntity> foundByUsername = userRepository.findByUsernameOrEmail("user1", "not_existing_email@test.com");
        Optional<UserEntity> foundByEmail = userRepository.findByUsernameOrEmail("not_existing_user", "user2@test.com");

        assertTrue(foundByUsername.isPresent());
        assertEquals("user1", foundByUsername.get().getUsername());

        assertTrue(foundByEmail.isPresent());
        assertEquals("user2@test.com", foundByEmail.get().getEmail());
    }

    @Test
    @DisplayName("find all by username in or email in test")
    void findAllByUsernameInOrEmailInTest() {
        List<String> usernames = List.of("user1");
        List<String> emails = List.of("user2@test.com");

        List<UserEntity> users = userRepository.findAllByUsernameInOrEmailIn(usernames, emails);

        assertEquals(2, users.size());

        boolean hasUser1 = users.stream().anyMatch(u -> "user1".equals(u.getUsername()));
        boolean hasUser2 = users.stream().anyMatch(u -> "user2".equals(u.getUsername()));

        assertTrue(hasUser1);
        assertTrue(hasUser2);
    }

    @Test
    @DisplayName("delete by username test")
    void deleteByUsernameTest() {
        userRepository.deleteByUsername("user1");

        Optional<UserEntity> deleted = userRepository.findByUsername("user1");

        assertFalse(deleted.isPresent());
    }
}