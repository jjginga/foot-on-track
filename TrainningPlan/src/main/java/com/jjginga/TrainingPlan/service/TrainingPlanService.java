package com.jjginga.TrainingPlan.service;

import com.jjginga.TrainingPlan.entity.TrainingPlan;
import com.jjginga.TrainingPlan.entity.TrainingSession;
import com.jjginga.TrainingPlan.repository.TrainingPlanRepository;
import com.jjginga.TrainingPlan.repository.TrainingSessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingPlanService {

    @Autowired
    private TrainingPlanRepository trainingPlanRepository;

    @Autowired
    private TrainingSessionRepository trainingSessionRepository;

    public List<TrainingPlan> findAll() {
        return trainingPlanRepository.findAll();
    }

    public Optional<TrainingPlan> findById(Long id) {
        return trainingPlanRepository.findById(id);
    }

    @Transactional
    public TrainingPlan save(TrainingPlan trainingPlan) {
        for (TrainingSession session : trainingPlan.getSessions()) {
            session.setTrainingPlan(trainingPlan);  // Set the back reference if not already set
        }
        return trainingPlanRepository.save(trainingPlan);
    }

    public void delete(Long id) {
        trainingPlanRepository.deleteById(id);
    }

    public TrainingSession saveSession(TrainingSession session) {
        return trainingSessionRepository.save(session);
    }

    public void deleteSession(Long sessionId) {
        trainingSessionRepository.deleteById(sessionId);
    }

    public TrainingSession updateSession(Long sessionId, TrainingSession updatedSession) {
        return trainingSessionRepository.findById(sessionId)
                .map(session -> {
                    session.setDistance(updatedSession.getDistance());
                    session.setTime(updatedSession.getTime());
                    return trainingSessionRepository.save(session);
                }).orElseThrow(() -> new RuntimeException("Session not found"));
    }
}