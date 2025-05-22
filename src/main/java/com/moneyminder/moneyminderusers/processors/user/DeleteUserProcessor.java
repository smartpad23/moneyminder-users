package com.moneyminder.moneyminderusers.processors.user;

import com.moneyminder.moneyminderusers.mappers.UserMapper;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import com.moneyminder.moneyminderusers.persistence.repositories.GroupRepository;
import com.moneyminder.moneyminderusers.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeleteUserProcessor {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserMapper userMapper;

    @Transactional
    public void deleteUser(String username) {
        Assert.isTrue(userRepository.existsByUsername(username), "Username does not exist");
        final UserEntity userEntity = this.userRepository.findByUsername(username).orElse(new UserEntity());

        userEntity.getGroups().forEach(group -> {
            group.setUsers(group.getUsers().stream().filter(userInd ->
                    !userInd.getUsername().equals(userEntity.getUsername())).collect(Collectors.toList()));
            this.groupRepository.save(group);
        });

        this.userRepository.deleteByUsername(username);
    }

    public void deleteUserList(List<String> ids) {
        ids.forEach(this::deleteUser);
    }
}
