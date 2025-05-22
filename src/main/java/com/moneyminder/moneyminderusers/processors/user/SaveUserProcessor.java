package com.moneyminder.moneyminderusers.processors.user;

import com.moneyminder.moneyminderusers.dto.LoginRequestDto;
import com.moneyminder.moneyminderusers.dto.UserResponseDto;
import com.moneyminder.moneyminderusers.dto.UserUpdateRequestDto;
import com.moneyminder.moneyminderusers.mappers.UserMapper;
import com.moneyminder.moneyminderusers.models.User;
import com.moneyminder.moneyminderusers.persistence.entities.GroupEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import com.moneyminder.moneyminderusers.persistence.repositories.GroupRepository;
import com.moneyminder.moneyminderusers.persistence.repositories.UserRepository;
import com.moneyminder.moneyminderusers.processors.authServices.AuthenticationService;
import com.moneyminder.moneyminderusers.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaveUserProcessor {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserMapper userMapper;
    private final AuthenticationService authenticationService;

    private final PasswordEncoder passwordEncoder;

    public String saveUser(User user) {
        this.checkUserAttributes(user);

        final UserEntity userEntity = this.userMapper.toEntity(user);

        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getGroups() != null && !user.getGroups().isEmpty()) {
            userEntity.setGroups(this.groupRepository.findAllByIdIn(user.getGroups()));
        }

        userRepository.save(userEntity);
        System.out.println("saved user: " + userEntity.toString());

        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword(user.getPassword());

        try {
            return authenticationService.authenticationMethod(loginRequest);
        } catch (Exception e) {
            System.out.println("Falla la autenticación");
            throw new RuntimeException("Error durante el login del usuario creado", e);
        }
    }

    @Transactional
    public User updateUser(User user) {
        Assert.isTrue(this.userRepository.existsByUsername(user.getUsername()), "User with username or email don't exists");
        this.checkUserAttributes(user);

        final UserEntity dbUserEntity = this.userRepository.findByUsername(user.getUsername()).orElse(null);
        final UserEntity userEntity = this.userMapper.toEntity(user);

        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));

        userEntity.setGroups(new ArrayList<>());

        List<GroupEntity> groups = this.groupRepository.findAllByIdIn(user.getGroups());
        userEntity.setGroups(groups);

        final boolean modifiedGroupsConditions = dbUserEntity.getGroups() != null ||
                dbUserEntity.getGroups().size() != user.getGroups().size() ||
                dbUserEntity.getGroups().stream().map(GroupEntity::getId).toList().stream().anyMatch(user.getGroups()::contains);

        if (modifiedGroupsConditions && (user.getGroups() == null || user.getGroups().isEmpty())) {
            groups.forEach(group -> {
                group.setUsers(group.getUsers().stream().filter(userInd -> !userInd.getUsername()
                        .equals(userEntity.getUsername())).collect(Collectors.toList()));
                this.groupRepository.save(group);
            });
            userEntity.setGroups(new ArrayList<>());
        }

        if (modifiedGroupsConditions && user.getGroups() != null && !user.getGroups().isEmpty()) {
            groups.forEach(group -> {
                group.setUsers(group.getUsers().stream().filter(userInd -> !userInd.getUsername()
                        .equals(userEntity.getUsername())).collect(Collectors.toList()));
                group.getUsers().add(userEntity);
            });
            userEntity.setGroups(groups);
        }

        return this.userMapper.toModel(userRepository.save(userEntity));
    }

    public UserResponseDto updateUserData(UserUpdateRequestDto user) {
        Assert.notNull(user, "User cannot be null");
        Assert.notNull(user.getEmail(), "Email cannot be null");
        Assert.hasLength(user.getEmail(), "Email cannot be empty");

        String username = AuthUtils.getUsername();


        UserEntity userOldData = this.userRepository.findByUsername(username).orElse(null);
        Assert.notNull(userOldData, "User with username don't exists");

        userOldData.setEmail(user.getEmail());

        if (user.getNewPassword() != null && !user.getNewPassword().isEmpty()) {
            boolean passwordMatches = passwordEncoder.matches(user.getOldPassword(), userOldData.getPassword());
            Assert.isTrue(passwordMatches, "Passwords don't match");

            String newPassword = passwordEncoder.encode(user.getNewPassword());
            userOldData.setPassword(newPassword);

        }

        this.userRepository.save(userOldData);
        UserResponseDto userUpdateResponseDto = new UserResponseDto();
        userUpdateResponseDto.setUsername(userOldData.getUsername());
        userUpdateResponseDto.setEmail(userOldData.getEmail());

        return userUpdateResponseDto;
    }

    public List<User> updateUserList(final List<User> userList) {
        List<User> updatedUsers = new ArrayList<>();

        if (userList != null && !userList.isEmpty()) {
            userList.forEach(user -> updatedUsers.add(this.updateUser(user)));
        }

        return updatedUsers;
    }

    private void checkUserAttributes(User user) {
        Assert.notNull(user, "User cannot be null");
        Assert.notNull(user.getUsername(), "Username cannot be null");
        Assert.hasLength(user.getUsername(), "Username cannot be empty");
        Assert.notNull(user.getPassword(), "Password cannot be null");
        Assert.hasLength(user.getPassword(), "Password cannot be empty");
        Assert.notNull(user.getEmail(), "Email cannot be null");
        Assert.hasLength(user.getEmail(), "Email cannot be empty");
    }
}
