package com.jjginga.TrackingService.controller;

import com.jjginga.TrackingService.entity.LocationPoint;
import com.jjginga.TrackingService.entity.RunningSession;
import com.jjginga.TrackingService.service.LocationPointService;
import com.jjginga.TrackingService.service.RunningSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/running-sessions")
public class RunningSessionController {

    @Autowired
    private RunningSessionService service;

    @Autowired
    private LocationPointService locationPointService;

    @PostMapping("/start")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<RunningSession> startSession(@RequestBody RunningSession session) {
        RunningSession newSession = service.save(session);
        return ResponseEntity.ok(newSession);
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<LocationPoint> addLocationPoint(@PathVariable Long sessionId, @RequestBody LocationPoint locationPoint) {
        RunningSession runningSession = service.findById(sessionId);
        if (runningSession==null) {
            return ResponseEntity.notFound().build();
        }

        locationPoint.setRunningSession(runningSession);
        LocationPoint savedLocationPoint = locationPointService.saveLocationPoint(locationPoint);
        return ResponseEntity.ok(savedLocationPoint);
    }

    @PostMapping("/{id}/stop")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<RunningSession> stopSession(@PathVariable Long id) {
        RunningSession session = service.findById(id);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        session.setStatus(RunningSession.ENDED);
        service.save(session);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/{id}/pause")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<RunningSession> pauseSession(@PathVariable Long id) {
        RunningSession session = service.findById(id);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        session.setStatus(RunningSession.PAUSED);
        service.save(session);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/{id}/resume")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<RunningSession> resumeSession(@PathVariable Long id) {
        RunningSession session = service.findById(id);
        if (session == null && !session.getStatus().equals(RunningSession.PAUSED)) {
            return ResponseEntity.badRequest().body(null);
        }

        session.setStatus(RunningSession.STARTED);
        service.save(session);
        return ResponseEntity.ok(session);
    }

}
