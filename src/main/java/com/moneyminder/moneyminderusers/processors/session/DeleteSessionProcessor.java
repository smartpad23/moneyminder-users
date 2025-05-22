package com.moneyminder.moneyminderusers.processors.session;

import com.moneyminder.moneyminderusers.mappers.SessionMapper;
import com.moneyminder.moneyminderusers.persistence.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteSessionProcessor {
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;

    public void deleteSession(String sessionId) {
        Assert.isTrue(sessionRepository.existsById(sessionId), "Session not found");
        this.sessionRepository.deleteById(sessionId);
    }

    public void deleteSessionList(List<String> ids) {
        ids.forEach(this::deleteSession);
    }
}
