package com.moneyminder.moneyminderusers.processors.session;

import com.moneyminder.moneyminderusers.mappers.SessionMapper;
import com.moneyminder.moneyminderusers.models.Session;
import com.moneyminder.moneyminderusers.persistence.entities.SessionEntity;
import com.moneyminder.moneyminderusers.persistence.entities.UserEntity;
import com.moneyminder.moneyminderusers.persistence.repositories.SessionRepository;
import com.moneyminder.moneyminderusers.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaveSessionProcessor {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SessionMapper sessionMapper;

    public Session saveSession(final Session session) {
        this.checkSessionAttributes(session);

        final SessionEntity sessionEntity = this.sessionMapper.toEntity(session);

        final UserEntity userEntity = this.userRepository.findByUsernameOrEmail(session.getUser(), session.getUser())
                                        .orElse(null);
        Assert.notNull(userEntity, "User not found");

        sessionEntity.setUser(userEntity);

        return this.sessionMapper.toModel(sessionRepository.save(sessionEntity));
    }

    public List<Session> saveSessionList(final List<Session> sessionList) {
        List<Session> savedSessions = new ArrayList<>();

        if (sessionList != null && !sessionList.isEmpty()) {
            sessionList.forEach(session -> savedSessions.add(this.saveSession(session)));
        }

        return savedSessions;
    }

    public Session updateSession(final String id, final Session session) {
        Assert.isTrue(session.getId().equals(id), "Session not found");
        return this.saveSession(session);
    }

    public List<Session> updateSessionList(final List<Session> sessionList) {
        List<Session> updatedSessions = new ArrayList<>();

        if (sessionList != null && !sessionList.isEmpty()) {
            sessionList.forEach(session -> updatedSessions.add(this.updateSession(session.getId(), session)));
        }

        return updatedSessions;
    }

    private void checkSessionAttributes(final Session session) {
        Assert.notNull(session, "Session cannot be null");
        Assert.notNull(session.getToken(), "Token cannot be null");
        Assert.hasLength(session.getToken(), "Token cannot be empty");
        Assert.notNull(session.getUser(), "User cannot be null");
        Assert.hasLength(session.getToken(), "Token cannot be empty");
        Assert.notNull(session.getExpirationDate(), "Expiration date cannot be null");
    }
}
