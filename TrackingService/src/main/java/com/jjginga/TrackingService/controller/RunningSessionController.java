package com.jjginga.TrackingService.controller;

import com.jjginga.TrackingService.entity.LocationPoint;
import com.jjginga.TrackingService.entity.RunningSession;
import com.jjginga.TrackingService.entity.SimpleRunningSession;
import com.jjginga.TrackingService.service.LocationPointService;
import com.jjginga.TrackingService.service.RunningSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


@RestController
@RequestMapping("/running-sessions")
public class RunningSessionController {

    @Autowired
    private RunningSessionService service;

    @Autowired
    private LocationPointService locationPointService;


    @PostMapping("/start")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<?> startSession(@RequestBody SimpleRunningSession session) {
        //System.out.println("RunningSession before save: " + session);

        if (session.getUserId() == null) {
            return ResponseEntity.badRequest().body("Please provide userId");
        }

        if (!isValidDateTimeFormat(session.getTime())) {
            return ResponseEntity.badRequest().body("Invalid date-time format");
        }

        if (service.hasOngoingSession(session.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already has an ongoing session");
        }

        RunningSession newSession = new RunningSession();
        newSession.setUserId(session.getUserId());

        newSession.setStartTime(getLocalDateTime(session.getTime()));

        newSession.setStatus(RunningSession.STARTED);
        newSession.setEndTime(null);
        //System.out.println("RunningSession before save: " + newSession);
        newSession = service.save(newSession);
        //System.out.println("RunningSession after save: " + newSession);
        return ResponseEntity.ok(newSession);
    }



    @PostMapping("/{sessionId}/update")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<?> addLocationPoint(@PathVariable Long sessionId, @RequestBody LocationPoint locationPoint) {
        RunningSession runningSession = service.findById(sessionId);
        if (runningSession==null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already has an ongoing session");
        }

        if (!(runningSession.getStatus().equals(RunningSession.STARTED))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The session number is not valid.");
        }

        locationPoint.setRunningSession(runningSession);
        //System.out.println("LocationPoint before save: " + locationPoint);
        LocationPoint savedLocationPoint = locationPointService.saveLocationPoint(locationPoint);
        return ResponseEntity.ok(savedLocationPoint);
    }

    @PostMapping("/{id}/stop")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<?> stopSession(@PathVariable Long id, @RequestBody SimpleRunningSession endSession) {
        RunningSession session = service.findById(id);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        if (!(session.getStatus().equals(RunningSession.STARTED)
                || session.getStatus().equals(RunningSession.PAUSED))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The session is not paused.");
        }
        session.setStatus(RunningSession.ENDED);
        session.setEndTime(getLocalDateTime(endSession.getTime()));
        service.save(session);

        locationPointService.updateElevationsForSession(session.getId());
        return ResponseEntity.ok(session);
    }

    @PostMapping("/{id}/pause")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<?> pauseSession(@PathVariable Long id) {
        RunningSession session = service.findById(id);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        if (!(session.getStatus().equals(RunningSession.STARTED))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The session number is not valid.");
        }

        session.setStatus(RunningSession.PAUSED);
        service.save(session);
        return ResponseEntity.ok(session);
    }

    @PostMapping("/{id}/resume")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<?> resumeSession(@PathVariable Long id) {
        RunningSession session = service.findById(id);
        if (session == null && !session.getStatus().equals(RunningSession.PAUSED)) {
            return ResponseEntity.badRequest().body(null);
        }
        if (!(session.getStatus().equals(RunningSession.PAUSED))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The session is not paused.");
        }
        session.setStatus(RunningSession.STARTED);
        service.save(session);
        return ResponseEntity.ok(session);
    }

    private boolean isValidDateTimeFormat(String dateTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime.parse(dateTime, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private LocalDateTime getLocalDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(dateTime, formatter);
    }

}
