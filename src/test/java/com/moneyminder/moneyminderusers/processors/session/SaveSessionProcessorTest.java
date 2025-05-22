package com.moneyminder.moneyminderusers.processors.session;

import com.moneyminder.moneyminderusers.mappers.SessionMapper;
import com.moneyminder.moneyminderusers.models.Session;
import com.moneyminder.moneyminderusers.persistence.entities.SessionEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import com.moneyminder.moneyminderusers.persistence.repositories.SessionRepository;
import com.moneyminder.moneyminderusers.persistence.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaveSessionProcessorTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SaveSessionProcessor saveSessionProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("save session test")
    void saveSessionTest() {
        Session session = new Session("1", "token123", LocalDate.now(), LocalDate.now().plusDays(1), "testUser");
        SessionEntity sessionEntity = new SessionEntity();
        SessionEntity savedEntity = new SessionEntity();
        UserEntity userEntity = new UserEntity();
        Session mappedSession = new Session();

        when(sessionMapper.toEntity(session)).thenReturn(sessionEntity);
        when(userRepository.findByUsernameOrEmail(session.getUser(), session.getUser())).thenReturn(Optional.of(userEntity));
        when(sessionRepository.save(sessionEntity)).thenReturn(savedEntity);
        when(sessionMapper.toModel(savedEntity)).thenReturn(mappedSession);

        Session result = saveSessionProcessor.saveSession(session);

        assertNotNull(result);
        verify(sessionMapper).toEntity(session);
        verify(userRepository).findByUsernameOrEmail(session.getUser(), session.getUser());
        verify(sessionRepository).save(sessionEntity);
        verify(sessionMapper).toModel(savedEntity);
    }

    @Test
    @DisplayName("save session list test")
    void saveSessionListTest() {
        Session session = new Session("1", "token123", LocalDate.now(), LocalDate.now().plusDays(1), "testUser");

        SaveSessionProcessor spyProcessor = spy(saveSessionProcessor);
        doReturn(new Session()).when(spyProcessor).saveSession(any(Session.class));

        List<Session> result = spyProcessor.saveSessionList(List.of(session));

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(spyProcessor).saveSession(any(Session.class));
    }

    @Test
    @DisplayName("update session test")
    void updateSessionTest() {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, "token123", LocalDate.now(), LocalDate.now().plusDays(1), "testUser");

        SaveSessionProcessor spyProcessor = spy(saveSessionProcessor);
        doReturn(new Session()).when(spyProcessor).saveSession(session);

        Session result = spyProcessor.updateSession(sessionId, session);

        assertNotNull(result);
        verify(spyProcessor).saveSession(session);
    }

    @Test
    @DisplayName("update session id mismatch test")
    void updateSessionIdMismatchTest() {
        String id = UUID.randomUUID().toString();
        String differentId = UUID.randomUUID().toString();
        Session session = new Session(differentId, "token123", LocalDate.now(), LocalDate.now().plusDays(1), "testUser");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveSessionProcessor.updateSession(id, session));

        assertEquals("Session not found", exception.getMessage());
    }

    @Test
    @DisplayName("update session list test")
    void updateSessionListTest() {
        Session session = new Session(UUID.randomUUID().toString(), "token123", LocalDate.now(), LocalDate.now().plusDays(1), "testUser");

        SaveSessionProcessor spyProcessor = spy(saveSessionProcessor);
        doReturn(new Session()).when(spyProcessor).updateSession(anyString(), any(Session.class));

        List<Session> result = spyProcessor.updateSessionList(List.of(session));

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(spyProcessor).updateSession(anyString(), any(Session.class));
    }

    // ---- Test checkSessionAttributes ----
    @Test
    @DisplayName("save session with null session test")
    void saveSessionWithNullSessionShouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveSessionProcessor.saveSession(null));

        assertTrue(exception.getMessage().contains("Session cannot be null"));
    }

    @Test
    @DisplayName("save session with null token test")
    void saveSessionWithNullTokenShouldThrowException() {
        Session session = new Session();
        session.setUser("testUser");
        session.setExpirationDate(LocalDate.now());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveSessionProcessor.saveSession(session));

        assertTrue(exception.getMessage().contains("Token cannot be null"));
    }

    @Test
    @DisplayName("save session with null user test")
    void saveSessionWithNullUserShouldThrowException() {
        Session session = new Session();
        session.setToken("token123");
        session.setExpirationDate(LocalDate.now());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveSessionProcessor.saveSession(session));

        assertTrue(exception.getMessage().contains("User cannot be null"));
    }

    @Test
    @DisplayName("save session with null expiration date test")
    void saveSessionWithNullExpirationDateShouldThrowException() {
        Session session = new Session();
        session.setToken("token123");
        session.setUser("testUser");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveSessionProcessor.saveSession(session));

        assertTrue(exception.getMessage().contains("Expiration date cannot be null"));
    }

    @Test
    @DisplayName("save session with non existing user test")
    void saveSessionWithNonExistingUserShouldThrowException() {
        Session session = new Session("1", "token123", LocalDate.now(), LocalDate.now().plusDays(1), "testUser");

        when(sessionMapper.toEntity(session)).thenReturn(new SessionEntity());
        when(userRepository.findByUsernameOrEmail(session.getUser(), session.getUser())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saveSessionProcessor.saveSession(session));

        assertTrue(exception.getMessage().contains("User not found"));
    }

}