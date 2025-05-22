package com.moneyminder.moneyminderusers.persistence.repositories;

import com.moneyminder.moneyminderusers.persistence.entities.GroupEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@DataJpaTest
class GroupRepositoryTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("existsById test")
    void testExistsById() {
        GroupEntity group = new GroupEntity();
        group.setName("Test Group");
        GroupEntity savedGroup = groupRepository.save(group);

        boolean exists = groupRepository.existsById(savedGroup.getId());

        assertTrue(exists);
    }

    @Test
    @DisplayName("findAllByIdIn test")
    void testFindAllByIdIn() {
        GroupEntity group1 = new GroupEntity();
        group1.setName("Group One");

        GroupEntity group2 = new GroupEntity();
        group2.setName("Group Two");

        groupRepository.saveAll(List.of(group1, group2));

        List<GroupEntity> groups = groupRepository.findAllByIdIn(List.of(group1.getId(), group2.getId()));

        assertEquals(2, groups.size());
    }

    @Test
    @DisplayName("findAllByUsers_Username test")
    void testFindAllByUsersUsername() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        userRepository.save(user);

        GroupEntity group = new GroupEntity();
        group.setName("Group With User");
        group.setUsers(List.of(user));
        groupRepository.save(group);

        List<GroupEntity> groups = groupRepository.findAllByUsers_Username("testUser");

        assertEquals(1, groups.size());
        assertEquals("Group With User", groups.get(0).getName());
    }


}