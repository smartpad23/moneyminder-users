package com.moneyminder.moneyminderusers.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneyminder.moneyminderusers.models.GroupRequest;
import com.moneyminder.moneyminderusers.processors.groupRequest.DeleteGroupRequestProcessor;
import com.moneyminder.moneyminderusers.processors.groupRequest.RetrieveGroupRequestProcessor;
import com.moneyminder.moneyminderusers.processors.groupRequest.SaveGroupRequestProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import com.moneyminder.moneyminderusers.dto.GroupRequestWithBudgetNameDto;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WebMvcTest(GroupRequestController.class)
@AutoConfigureMockMvc(addFilters = false)
class GroupRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetrieveGroupRequestProcessor retrieveGroupRequestProcessor;

    @MockBean
    private SaveGroupRequestProcessor saveGroupRequestProcessor;

    @MockBean
    private DeleteGroupRequestProcessor deleteGroupRequestProcessor;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("get all group requests test")
    void getGroupRequests() throws Exception {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId("gr1");

        Mockito.when(retrieveGroupRequestProcessor.retrieveGroupRequest())
                .thenReturn(List.of(groupRequest));

        mockMvc.perform(get("/group-request"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("gr1"));
    }

    @Test
    @DisplayName("get group request by id test")
    void getGroupRequestById() throws Exception {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId("gr1");

        Mockito.when(retrieveGroupRequestProcessor.retrieveGroupRequestById("gr1"))
                .thenReturn(groupRequest);

        mockMvc.perform(get("/group-request/gr1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("gr1"));
    }

    @Test
    @DisplayName("get group requests by username test")
    void getGroupRequestsByUsername() throws Exception {
        GroupRequestWithBudgetNameDto dto = new GroupRequestWithBudgetNameDto();
        dto.setId("gr1");
        dto.setBudgetName("Budget 1");

        Mockito.when(retrieveGroupRequestProcessor.retrieveGroupRequestWithBudgetNameByUsername("user1"))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/group-request/by-username/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("gr1"))
                .andExpect(jsonPath("$[0].budgetName").value("Budget 1"));
    }

    @Test
    @DisplayName("create a group request test")
    void createGroupRequest() throws Exception {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId("gr1");
        groupRequest.setRequestingUser("user1");
        groupRequest.setRequestedUser("user2");
        groupRequest.setGroup("groupId123");

        GroupRequestWithBudgetNameDto dto = new GroupRequestWithBudgetNameDto();
        dto.setId("gr1");
        dto.setBudgetName("Budget 1");

        Mockito.when(saveGroupRequestProcessor.saveGroupRequest(any(GroupRequest.class)))
                .thenReturn(dto);

        mockMvc.perform(post("/group-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("gr1"))
                .andExpect(jsonPath("$.budgetName").value("Budget 1"));
    }

    @Test
    @DisplayName("update a group request test")
    void updateGroupRequest() throws Exception {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId("gr1");
        groupRequest.setRequestingUser("user1");
        groupRequest.setRequestedUser("user2");
        groupRequest.setGroup("groupId123");

        GroupRequestWithBudgetNameDto dto = new GroupRequestWithBudgetNameDto();
        dto.setId("gr1");
        dto.setBudgetName("Updated Budget");

        Mockito.when(saveGroupRequestProcessor.updateGroupRequest(eq("gr1"), any(GroupRequest.class)))
                .thenReturn(dto);

        mockMvc.perform(put("/group-request/gr1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("gr1"))
                .andExpect(jsonPath("$.budgetName").value("Updated Budget"));
    }

    @Test
    @DisplayName("delete a group request test")
    void deleteGroupRequest() throws Exception {
        mockMvc.perform(delete("/group-request/gr1"))
                .andExpect(status().isOk());

        Mockito.verify(deleteGroupRequestProcessor).deleteGroupRequest("gr1");
    }

}