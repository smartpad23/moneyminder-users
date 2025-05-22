package com.moneyminder.moneyminderusers.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneyminder.moneyminderusers.dto.UserResponseDto;
import com.moneyminder.moneyminderusers.dto.UserUpdateRequestDto;
import com.moneyminder.moneyminderusers.models.User;
import com.moneyminder.moneyminderusers.processors.user.DeleteUserProcessor;
import com.moneyminder.moneyminderusers.processors.user.RetrieveUserProcessor;
import com.moneyminder.moneyminderusers.processors.user.SaveUserProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetrieveUserProcessor retrieveUserProcessor;

    @MockBean
    private SaveUserProcessor saveUserProcessor;

    @MockBean
    private DeleteUserProcessor deleteUserProcessor;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Get all users")
    void getUsers() throws Exception {
        User user = new User();
        user.setUsername("user1");

        Mockito.when(retrieveUserProcessor.retrieveUsers())
                .thenReturn(List.of(user));

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"));
    }

    @Test
    @DisplayName("Get user by username")
    void getUserByUsername() throws Exception {
        UserResponseDto userResponse = new UserResponseDto();
        userResponse.setUsername("user1");
        userResponse.setEmail("user1@example.com");

        Mockito.when(retrieveUserProcessor.retrieveUserByUsernameOrEmail("user1"))
                .thenReturn(userResponse);

        mockMvc.perform(get("/user/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"));
    }

    @Test
    @DisplayName("Create a new user")
    void createUser() throws Exception {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("password");
        user.setEmail("user1@example.com");

        Mockito.when(saveUserProcessor.saveUser(any(User.class)))
                .thenReturn("fake-jwt-token");

        mockMvc.perform(post("/user/new-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("fake-jwt-token"));
    }

    @Test
    @DisplayName("Update a user")
    void updateUser() throws Exception {
        User user = new User();
        user.setUsername("user1");
        user.setPassword("password");
        user.setEmail("user1@example.com");

        Mockito.when(saveUserProcessor.updateUser(any(User.class)))
                .thenReturn(user);

        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"));
    }

    @Test
    @DisplayName("Update user data")
    void updateUserData() throws Exception {
        UserUpdateRequestDto userUpdate = new UserUpdateRequestDto();
        userUpdate.setEmail("newemail@example.com");
        userUpdate.setOldPassword("oldPassword");
        userUpdate.setNewPassword("newPassword");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setUsername("user1");
        responseDto.setEmail("newemail@example.com");

        Mockito.when(saveUserProcessor.updateUserData(any(UserUpdateRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/user/update-user-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("newemail@example.com"));
    }

    @Test
    @DisplayName("Delete a user")
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/user/user1"))
                .andExpect(status().isOk());

        Mockito.verify(deleteUserProcessor).deleteUser("user1");
    }

}