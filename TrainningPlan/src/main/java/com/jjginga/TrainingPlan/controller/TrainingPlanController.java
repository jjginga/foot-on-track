package com.jjginga.TrainingPlan.controller;

import com.jjginga.TrainingPlan.entity.TrainingPlan;
import com.jjginga.TrainingPlan.entity.TrainingSession;
import com.jjginga.TrainingPlan.service.TrainingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-plans")
public class TrainingPlanController {

    @Autowired
    private TrainingPlanService trainingPlanService;

    //endpoints for managing training plans
    @GetMapping("/training-plans")
    public ResponseEntity<List<TrainingPlan>> getAllTrainingPlans() {
        return ResponseEntity.ok(trainingPlanService.findAll());
    }

    @GetMapping("/training-plans/{id}")
    public ResponseEntity<TrainingPlan> getTrainingPlanById(@PathVariable Long id) {
        return trainingPlanService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/training-plans")
    public ResponseEntity<TrainingPlan> createTrainingPlan(@RequestBody TrainingPlan trainingPlan) {
        TrainingPlan savedPlan = trainingPlanService.save(trainingPlan);
        return ResponseEntity.ok(savedPlan);
    }

    @DeleteMapping("/training-plans/{id}")
    public ResponseEntity<Void> deleteTrainingPlan(@PathVariable Long id) {
        trainingPlanService.delete(id);
        return ResponseEntity.ok().build();
    }

    //endpoints for managing training sessions within a training plan
    @PostMapping("/training-sessions")
    public ResponseEntity<TrainingSession> addTrainingSession(@RequestBody TrainingSession session) {
        return ResponseEntity.ok(trainingPlanService.saveSession(session));
    }

    @PutMapping("/training-sessions/{sessionId}")
    public ResponseEntity<TrainingSession> updateTrainingSession(@PathVariable Long sessionId, @RequestBody TrainingSession session) {
        return ResponseEntity.ok(trainingPlanService.updateSession(sessionId, session));
    }

    @DeleteMapping("/training-sessions/{sessionId}")
    public ResponseEntity<Void> deleteTrainingSession(@PathVariable Long sessionId) {
        trainingPlanService.deleteSession(sessionId);
        return ResponseEntity.ok().build();
    }
}