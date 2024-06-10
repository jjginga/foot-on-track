package com.jjginga.TrackingService.controller;

import com.jjginga.TrackingService.entity.LocationPoint;
import com.jjginga.TrackingService.service.LocationPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationPointController {

    @Autowired
    private LocationPointService locationPointService;

    @PostMapping
    public ResponseEntity<LocationPoint> addLocationPoint(@RequestBody LocationPoint locationPoint) {
        LocationPoint savedLocationPoint = locationPointService.saveLocationPoint(locationPoint);
        return ResponseEntity.ok(savedLocationPoint);
    }

    @GetMapping("/session/{sessionId}")
    @PreAuthorize("@apiSecurity.hasUserRole()")
    public ResponseEntity<List<LocationPoint>> getLocationPointsBySession(@PathVariable Long sessionId) {
        List<LocationPoint> points = locationPointService.findAllBySessionId(sessionId);
        return !points.isEmpty() ? ResponseEntity.ok(points) : ResponseEntity.noContent().build();
    }
}
