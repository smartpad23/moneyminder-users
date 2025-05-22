package com.moneyminder.moneyminderusers.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneyminder.moneyminderusers.models.Group;
import com.moneyminder.moneyminderusers.dto.CreateGroupByUsernameDto;
import com.moneyminder.moneyminderusers.processors.group.DeleteGroupProcessor;
import com.moneyminder.moneyminderusers.processors.group.RetrieveGroupProcessor;
import com.moneyminder.moneyminderusers.processors.group.SaveGroupProcessor;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WebMvcTest(GroupController.class)
@AutoConfigureMockMvc(addFilters = false)
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetrieveGroupProcessor retrieveGroupProcessor;

    @MockBean
    private SaveGroupProcessor saveGroupProcessor;

    @MockBean
    private DeleteGroupProcessor deleteGroupProcessor;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("get all groups test")
    void getGroups() throws Exception {
        Group group = new Group();
        group.setId("group1");
        group.setName("Test Group");

        Mockito.when(retrieveGroupProcessor.retrieveGroups())
                .thenReturn(List.of(group));

        mockMvc.perform(get("/group"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("group1"))
                .andExpect(jsonPath("$[0].name").value("Test Group"));
    }

    @Test
    @DisplayName("get group by id test")
    void getGroupById() throws Exception {
        Group group = new Group();
        group.setId("group1");
        group.setName("Test Group");

        Mockito.when(retrieveGroupProcessor.retrieveGroupById("group1"))
                .thenReturn(group);

        mockMvc.perform(get("/group/group1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("group1"))
                .andExpect(jsonPath("$.name").value("Test Group"));
    }

    @Test
    @DisplayName("get group ids by username test")
    void getGroupIdsByUsername() throws Exception {
        Mockito.when(retrieveGroupProcessor.retrieveGroupIdsByUsername("testuser"))
                .thenReturn(List.of("group1", "group2"));

        mockMvc.perform(get("/group/by-username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("group1"))
                .andExpect(jsonPath("$[1]").value("group2"));
    }

    @Test
    @DisplayName("get usernames of a group test")
    void getUsernamesOfGroup() throws Exception {
        Mockito.when(retrieveGroupProcessor.retrieveUsernameOfGroup("group1"))
                .thenReturn(List.of("user1", "user2"));

        mockMvc.perform(get("/group/usernames-of/group1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("user1"))
                .andExpect(jsonPath("$[1]").value("user2"));
    }

    @Test
    @DisplayName("create a group test")
    void createGroup() throws Exception {
        Group group = new Group();
        group.setId("group1");
        group.setName("New Group");

        Mockito.when(saveGroupProcessor.saveGroup(any(Group.class)))
                .thenReturn(group);

        mockMvc.perform(post("/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(group)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("group1"))
                .andExpect(jsonPath("$.name").value("New Group"));
    }

    @Test
    @DisplayName("create a group for budget test")
    void createGroupForBudget() throws Exception {
        CreateGroupByUsernameDto dto = new CreateGroupByUsernameDto();
        dto.setUsername("user1");
        dto.setGroupName("Budget Group");

        Mockito.when(saveGroupProcessor.createGroupByUsername(any(CreateGroupByUsernameDto.class)))
                .thenReturn("new-group-id");

        mockMvc.perform(post("/group/budget")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("new-group-id"));
    }

    @Test
    @DisplayName("update a group test")
    void updateGroup() throws Exception {
        Group group = new Group();
        group.setId("group1");
        group.setName("Updated Group");

        Mockito.when(saveGroupProcessor.updateGroup(eq("group1"), any(Group.class)))
                .thenReturn(group);

        mockMvc.perform(put("/group/group1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(group)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("group1"))
                .andExpect(jsonPath("$.name").value("Updated Group"));
    }

    @Test
    @DisplayName("delete a group test")
    void deleteGroup() throws Exception {
        mockMvc.perform(delete("/group/group1"))
                .andExpect(status().isOk());

        Mockito.verify(deleteGroupProcessor).deleteGroup("group1");
    }

}