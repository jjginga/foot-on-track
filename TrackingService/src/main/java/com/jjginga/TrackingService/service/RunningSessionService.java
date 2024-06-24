package com.jjginga.TrackingService.service;

import com.jjginga.TrackingService.entity.RunningSession;
import com.jjginga.TrackingService.repository.RunningSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RunningSessionService {

    @Autowired
    private RunningSessionRepository repository;

    public RunningSession save(RunningSession session) {
        return repository.save(session);
    }

    public RunningSession findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<RunningSession> findAllSessionsByUserId(String userId) {
        return repository.findAllByUserId(userId);
    }

    public boolean hasOngoingSession(String userId) {
        return repository.existsByUserIdAndStatus(userId, RunningSession.STARTED);
    }
}
