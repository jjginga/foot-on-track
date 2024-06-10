package com.jjginga.TrainingPlan.controller;

import com.jjginga.TrainingPlan.entity.TrainingPlan;
import com.jjginga.TrainingPlan.entity.TrainingSession;
import com.jjginga.TrainingPlan.service.TrainingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/training-plans")
public class TrainingPlanController {

    @Autowired
    private TrainingPlanService trainingPlanService;

    //endpoints for managing training plans
    @GetMapping("/get-all")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<List<TrainingPlan>> getAllTrainingPlans() {
        return ResponseEntity.ok(trainingPlanService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<TrainingPlan> getTrainingPlanById(@PathVariable Long id) {
        return trainingPlanService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<TrainingPlan> createTrainingPlan(@RequestBody TrainingPlan trainingPlan) {
        TrainingPlan savedPlan = trainingPlanService.save(trainingPlan);
        return ResponseEntity.ok(savedPlan);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<Void> deleteTrainingPlan(@PathVariable Long id) {
        trainingPlanService.delete(id);
        return ResponseEntity.ok().build();
    }

    //endpoints for managing training sessions within a training plan
    @PostMapping("/training-sessions")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<TrainingSession> addTrainingSession(@RequestBody TrainingSession session) {
        return ResponseEntity.ok(trainingPlanService.saveSession(session));
    }

    @PutMapping("/training-sessions/{sessionId}")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<TrainingSession> updateTrainingSession(@PathVariable Long sessionId, @RequestBody TrainingSession session) {
        return ResponseEntity.ok(trainingPlanService.updateSession(sessionId, session));
    }

    @DeleteMapping("/training-sessions/{sessionId}")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<Void> deleteTrainingSession(@PathVariable Long sessionId) {
        trainingPlanService.deleteSession(sessionId);
        return ResponseEntity.ok().build();
    }
}