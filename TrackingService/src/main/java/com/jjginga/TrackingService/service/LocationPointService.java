package com.jjginga.TrackingService.service;

import com.jjginga.TrackingService.ElevationUpdateTask;
import com.jjginga.TrackingService.client.ElevationClient;
import com.jjginga.TrackingService.entity.LocationPoint;
import com.jjginga.TrackingService.model.ElevationResponse;
import com.jjginga.TrackingService.repository.LocationPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
public class LocationPointService {


    @Autowired
    private LocationPointRepository locationPointRepository;

    @Autowired
    private ElevationClient elevationClient;

    @Autowired
    private ExecutorService executorService;

    public LocationPoint saveLocationPoint(LocationPoint locationPoint) {
        locationPoint.setElevation(0.0);
        return locationPointRepository.save(locationPoint);
    }

    public List<LocationPoint> findAllBySessionId(Long sessionId) {
        return locationPointRepository.findAllByRunningSessionId(sessionId);
    }

    public void updateElevationsForSession(Long sessionId) {
        ElevationUpdateTask elevationUpdateTask = new ElevationUpdateTask(sessionId, locationPointRepository, elevationClient);
        executorService.submit(elevationUpdateTask);
    }



}
