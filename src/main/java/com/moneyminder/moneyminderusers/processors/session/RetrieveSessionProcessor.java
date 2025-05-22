package com.moneyminder.moneyminderusers.processors.session;

import com.moneyminder.moneyminderusers.mappers.SessionMapper;
import com.moneyminder.moneyminderusers.models.Session;
import com.moneyminder.moneyminderusers.persistence.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class RetrieveSessionProcessor {
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;

    public List<Session> retrieveSessions() {
        return this.sessionMapper.toModelList(StreamSupport.stream(this.sessionRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()));
    }

    public Session retrieveSessionById(String sessionId) {
        return this.sessionMapper.toModel(sessionRepository.findById(sessionId).orElseThrow());
    }
}
