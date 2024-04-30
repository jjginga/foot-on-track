package com.jjginga.TrackingService.controller;

import com.jjginga.TrackingService.entity.RunningSession;
import com.jjginga.TrackingService.service.RunningSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/running-sessions")
public class RunningSessionController {

    @Autowired
    private RunningSessionService service;

    @PostMapping("/start")
    public ResponseEntity<RunningSession> startSession(@RequestBody RunningSession session) {
        RunningSession newSession = service.save(session);
        return ResponseEntity.ok(newSession);
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<RunningSession> stopSession(@PathVariable Long id) {
        RunningSession session = service.findById(id);
        if (session != null) {
            session.setStatus(RunningSession.ENDED);
            service.save(session);
            return ResponseEntity.ok(session);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/pause")
    public ResponseEntity<RunningSession> pauseSession(@PathVariable Long id) {
        RunningSession session = service.findById(id);
        if (session != null) {
            session.setStatus(RunningSession.PAUSED);
            service.save(session);
            return ResponseEntity.ok(session);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<RunningSession> resumeSession(@PathVariable Long id) {
        RunningSession session = service.findById(id);
        if (session != null && session.getStatus().equals(RunningSession.PAUSED)) {
            session.setStatus(RunningSession.STARTED);
            service.save(session);
            return ResponseEntity.ok(session);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
