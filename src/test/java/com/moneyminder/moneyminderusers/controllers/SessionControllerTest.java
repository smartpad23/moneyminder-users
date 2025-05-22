package com.moneyminder.moneyminderusers.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneyminder.moneyminderusers.models.Session;
import com.moneyminder.moneyminderusers.processors.session.DeleteSessionProcessor;
import com.moneyminder.moneyminderusers.processors.session.RetrieveSessionProcessor;
import com.moneyminder.moneyminderusers.processors.session.SaveSessionProcessor;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WebMvcTest(SessionController.class)
@AutoConfigureMockMvc(addFilters = false)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetrieveSessionProcessor retrieveSessionProcessor;

    @MockBean
    private SaveSessionProcessor saveSessionProcessor;

    @MockBean
    private DeleteSessionProcessor deleteSessionProcessor;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("get all sessions test")
    void getSessions() throws Exception {
        Session session = new Session();
        session.setId("session1");
        session.setToken("token123");

        Mockito.when(retrieveSessionProcessor.retrieveSessions())
                .thenReturn(List.of(session));

        mockMvc.perform(get("/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("session1"))
                .andExpect(jsonPath("$[0].token").value("token123"));
    }

    @Test
    @DisplayName("get session by id test")
    void getSessionById() throws Exception {
        Session session = new Session();
        session.setId("session1");
        session.setToken("token123");

        Mockito.when(retrieveSessionProcessor.retrieveSessionById("session1"))
                .thenReturn(session);

        mockMvc.perform(get("/session/session1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("session1"))
                .andExpect(jsonPath("$.token").value("token123"));
    }

    @Test
    @DisplayName("create a session test")
    void createSession() throws Exception {
        Session session = new Session();
        session.setId("session1");
        session.setToken("token123");
        session.setUser("user1");
        session.setExpirationDate(LocalDate.now().plusDays(7));

        Mockito.when(saveSessionProcessor.saveSession(any(Session.class)))
                .thenReturn(session);

        mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(session)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("session1"))
                .andExpect(jsonPath("$.token").value("token123"));
    }

    @Test
    @DisplayName("delete a session test")
    void deleteSession() throws Exception {
        mockMvc.perform(delete("/session/session1"))
                .andExpect(status().isOk());

        Mockito.verify(deleteSessionProcessor).deleteSession("session1");
    }

}