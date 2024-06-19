package com.jjginga.TrackingService;

import com.jjginga.TrackingService.client.ElevationClient;
import com.jjginga.TrackingService.entity.LocationPoint;
import com.jjginga.TrackingService.model.ElevationResponse;
import com.jjginga.TrackingService.repository.LocationPointRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ElevationUpdateTask implements Runnable {
    private final Long sessionId;
    private final LocationPointRepository locationPointRepository;
    private final ElevationClient elevationClient;

    private final int BATCH_SIZE = 100;

    public ElevationUpdateTask(Long sessionId, LocationPointRepository locationPointRepository, ElevationClient elevationClient) {
        this.sessionId = sessionId;
        this.locationPointRepository = locationPointRepository;
        this.elevationClient = elevationClient;
    }

    @Override
    public void run() {
        List<LocationPoint> locationPoints = locationPointRepository.findAllByRunningSessionId(sessionId);
        if (locationPoints.isEmpty()) {
            System.out.println("Empty location points list. Skipping elevation update.");
            return;
        }

        //fetch elevations for all points in batches of 100
        for (int i = 0; i < locationPoints.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, locationPoints.size());
            List<LocationPoint> batch = locationPoints.subList(i, end);

            StringBuilder locations = new StringBuilder();
            for (LocationPoint point : batch)
                locations.append(String.format("%.6f,%.6f|", point.getLatitude(), point.getLongitude()));

            //remove the last pipe
            if (locations.length() > 0)
                locations.setLength(locations.length() - 1);

            ElevationResponse elevationResponse = elevationClient.getElevation(locations.toString());
            List<ElevationResponse.Elevation> elevations = elevationResponse.getElevations();

            for (int j = 0; j < elevations.size(); j++) {
                batch.get(j).setElevation(elevations.get(j).getElevation());
                locationPointRepository.save(batch.get(j));
            }

            try {
                //wait for 1 second before the next batch
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
