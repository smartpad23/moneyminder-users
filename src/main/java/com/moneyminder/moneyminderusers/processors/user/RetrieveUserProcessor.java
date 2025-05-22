package com.moneyminder.moneyminderusers.processors.user;

import com.moneyminder.moneyminderusers.dto.UserResponseDto;
import com.moneyminder.moneyminderusers.mappers.UserMapper;
import com.moneyminder.moneyminderusers.models.User;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import com.moneyminder.moneyminderusers.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class RetrieveUserProcessor {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<User> retrieveUsers() {
        return this.userMapper.toModelList(StreamSupport.stream(this.userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()));
    }

    public UserResponseDto retrieveUserByUsernameOrEmail(String id) {
        UserEntity user = this.userRepository.findByUsernameOrEmail(id, id).orElseThrow();
        UserResponseDto userResponse = new UserResponseDto();
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }

    public UserEntity retrieveCompleteUserByUsernameOrEmail(String id) {
        return this.userRepository.findByUsernameOrEmail(id, id).orElseThrow();
    }
}
