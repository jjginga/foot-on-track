package com.jjginga.TrackingService.service;

import com.jjginga.TrackingService.client.ElevationClient;
import com.jjginga.TrackingService.entity.LocationPoint;
import com.jjginga.TrackingService.model.ElevationResponse;
import com.jjginga.TrackingService.repository.LocationPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationPointService {


    @Autowired
    private LocationPointRepository locationPointRepository;

    @Autowired
    private ElevationClient elevationClient;

    public LocationPoint saveLocationPoint(LocationPoint locationPoint) {
        String locations = String.format("%.6f,%.6f", locationPoint.getLatitude(), locationPoint.getLongitude());
        ElevationResponse elevationResponse = elevationClient.getElevation(locations);
        locationPoint.setElevation(elevationResponse.getElevations().get(0).getElevation());
        return locationPointRepository.save(locationPoint);
    }

    public List<LocationPoint> findAllBySessionId(Long sessionId) {
        return locationPointRepository.findAllByRunningSessionId(sessionId);
    }
}
