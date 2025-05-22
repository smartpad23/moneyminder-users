package com.moneyminder.moneyminderusers.persistence.repositories;

import com.moneyminder.moneyminderusers.persistence.entities.GroupEntity;
import com.moneyminder.moneyminderusers.persistence.entities.GroupRequestEntity;
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
class GroupRequestRepositoryTest {

    @Autowired
    private GroupRequestRepository groupRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    private UserEntity requestingUser;
    private UserEntity requestedUser;
    private GroupEntity group;

    @BeforeEach
    void setup() {
        requestingUser = new UserEntity();
        requestingUser.setUsername("requester");
        requestingUser.setEmail("requester@test.com");
        requestingUser.setPassword("password");
        requestingUser = userRepository.save(requestingUser);

        requestedUser = new UserEntity();
        requestedUser.setUsername("requested");
        requestedUser.setEmail("requested@test.com");
        requestedUser.setPassword("password");
        requestedUser = userRepository.save(requestedUser);

        group = new GroupEntity();
        group.setName("Test Group");
        group = groupRepository.save(group);
    }

    @Test
    @DisplayName("save and find by ID test")
    void saveAndFindById() {
        GroupRequestEntity request = new GroupRequestEntity();
        request.setGroup(group);
        request.setRequestingUser(requestingUser);
        request.setRequestedUser(requestedUser);
        request.setDate(LocalDate.now());

        GroupRequestEntity saved = groupRequestRepository.save(request);

        Optional<GroupRequestEntity> found = groupRequestRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(requestingUser.getUsername(), found.get().getRequestingUser().getUsername());
        assertEquals(requestedUser.getUsername(), found.get().getRequestedUser().getUsername());
        assertEquals(group.getId(), found.get().getGroup().getId());
    }

    @Test
    @DisplayName("find all by requesting username test")
    void findAllByRequestedUsername() {
        GroupRequestEntity request = new GroupRequestEntity();
        request.setGroup(group);
        request.setRequestingUser(requestingUser);
        request.setRequestedUser(requestedUser);
        request.setDate(LocalDate.now());
        groupRequestRepository.save(request);

        List<GroupRequestEntity> requests = groupRequestRepository.findAllByRequestedUser_Username(requestedUser.getUsername());

        assertFalse(requests.isEmpty());
        assertEquals(1, requests.size());
        assertEquals(requestedUser.getUsername(), requests.get(0).getRequestedUser().getUsername());
    }

    @Test
    @DisplayName("find all by requesting username test")
    void findAllByRequestingUsername() {
        GroupRequestEntity request = new GroupRequestEntity();
        request.setGroup(group);
        request.setRequestingUser(requestingUser);
        request.setRequestedUser(requestedUser);
        request.setDate(LocalDate.now());
        groupRequestRepository.save(request);

        List<GroupRequestEntity> requests = groupRequestRepository.findAllByRequestingUser_Username(requestingUser.getUsername());

        assertFalse(requests.isEmpty());
        assertEquals(1, requests.size());
        assertEquals(requestingUser.getUsername(), requests.get(0).getRequestingUser().getUsername());
    }

    @Test
    @DisplayName("find all by id in test")
    void findAllByIdInTest() {
        GroupRequestEntity request1 = new GroupRequestEntity();
        request1.setGroup(group);
        request1.setRequestingUser(requestingUser);
        request1.setRequestedUser(requestedUser);
        request1.setDate(LocalDate.now());
        groupRequestRepository.save(request1);

        GroupRequestEntity request2 = new GroupRequestEntity();
        request2.setGroup(group);
        request2.setRequestingUser(requestingUser);
        request2.setRequestedUser(requestedUser);
        request2.setDate(LocalDate.now());
        groupRequestRepository.save(request2);

        List<String> ids = List.of(request1.getId(), request2.getId());

        List<GroupRequestEntity> foundRequests = groupRequestRepository.findAllByIdIn(ids);

        assertEquals(2, foundRequests.size());
    }

}