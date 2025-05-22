package com.moneyminder.moneyminderusers.processors.session;

import com.moneyminder.moneyminderusers.mappers.SessionMapper;
import com.moneyminder.moneyminderusers.persistence.repositories.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeleteSessionProcessorTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private DeleteSessionProcessor deleteSessionProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("delete session test")
    void deleteSessionTest() {
        String id = UUID.randomUUID().toString();

        when(sessionRepository.existsById(id)).thenReturn(true);

        deleteSessionProcessor.deleteSession(id);

        verify(sessionRepository).existsById(id);
        verify(sessionRepository).deleteById(id);
    }

    @Test
    @DisplayName("delete session not found test")
    void deleteSessionNotFoundTest() {
        String id = UUID.randomUUID().toString();

        when(sessionRepository.existsById(id)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> deleteSessionProcessor.deleteSession(id));

        assertTrue(exception.getMessage().contains("Session not found"));
    }

    @Test
    @DisplayName("delete session list test")
    void deleteSessionListTest() {
        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        List<String> ids = List.of(id1, id2);

        when(sessionRepository.existsById(id1)).thenReturn(true);
        when(sessionRepository.existsById(id2)).thenReturn(true);

        deleteSessionProcessor.deleteSessionList(ids);

        verify(sessionRepository).existsById(id1);
        verify(sessionRepository).existsById(id2);
        verify(sessionRepository).deleteById(id1);
        verify(sessionRepository).deleteById(id2);
    }

}