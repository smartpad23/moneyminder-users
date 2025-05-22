package com.moneyminder.moneyminderusers.processors.session;

import com.moneyminder.moneyminderusers.mappers.SessionMapper;
import com.moneyminder.moneyminderusers.models.Session;
import com.moneyminder.moneyminderusers.persistence.entities.SessionEntity;
import com.moneyminder.moneyminderusers.persistence.repositories.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RetrieveSessionProcessorTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private RetrieveSessionProcessor retrieveSessionProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("retrieve sessions test")
    void retrieveSessionsTest() {
        List<SessionEntity> sessionEntities = List.of(new SessionEntity());
        List<Session> sessions = List.of(new Session());

        when(sessionRepository.findAll()).thenReturn(sessionEntities);
        when(sessionMapper.toModelList(sessionEntities)).thenReturn(sessions);

        List<Session> result = retrieveSessionProcessor.retrieveSessions();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sessionRepository).findAll();
        verify(sessionMapper).toModelList(sessionEntities);
    }

    @Test
    @DisplayName("retrieve session by id test")
    void retrieveSessionByIdTest() {
        String sessionId = UUID.randomUUID().toString();
        SessionEntity sessionEntity = new SessionEntity();
        Session session = new Session();

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(sessionEntity));
        when(sessionMapper.toModel(sessionEntity)).thenReturn(session);

        Session result = retrieveSessionProcessor.retrieveSessionById(sessionId);

        assertNotNull(result);
        verify(sessionRepository).findById(sessionId);
        verify(sessionMapper).toModel(sessionEntity);
    }

    @Test
    @DisplayName("retrieve session by id not found test")
    void retrieveSessionByIdNotFoundTest() {
        String sessionId = UUID.randomUUID().toString();

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> retrieveSessionProcessor.retrieveSessionById(sessionId));
        verify(sessionRepository).findById(sessionId);
        verifyNoInteractions(sessionMapper);
    }

}