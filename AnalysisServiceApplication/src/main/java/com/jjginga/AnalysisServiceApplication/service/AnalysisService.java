package com.jjginga.AnalysisServiceApplication.service;

import com.jjginga.AnalysisServiceApplication.entity.LocationPoint;
import com.jjginga.AnalysisServiceApplication.entity.RunningSession;
import com.jjginga.AnalysisServiceApplication.model.AnalysisResult;
import com.jjginga.AnalysisServiceApplication.model.CurrentAnalysisResult;
import com.jjginga.AnalysisServiceApplication.repository.LocationPointRepository;
import com.jjginga.AnalysisServiceApplication.repository.RunningSessionRepository;
import com.jjginga.AnalysisServiceApplication.util.DataAnalysisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.data.vector.DoubleVector;
import smile.regression.LinearModel;
import smile.regression.OLS;

import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalysisService {

    @Autowired
    private RunningSessionRepository runningSessionRepository;

    @Autowired
    private LocationPointRepository locationPointRepository;

    public CurrentAnalysisResult analyzeSession(Long sessionId) {
        RunningSession session = runningSessionRepository.findById(sessionId).orElse(null);
        if (session == null) {
            throw new IllegalArgumentException("Session not found with ID: " + sessionId);
        }


        double totalDistance = calculateTotalDistanceForSession(sessionId);
        double totalTime = calculateTotalTimeForSession(session);

        return new CurrentAnalysisResult(totalTime, totalDistance);
    }

    private double calculateTotalTimeForSession(RunningSession session) {
        if (session.getEndTime() == null && session.getStartTime() == null)
            return 0.0;

        long startSeconds = session.getStartTime().toEpochSecond(ZoneOffset.UTC);
        long endSeconds = session.getEndTime() != null ? session.getEndTime().toEpochSecond(ZoneOffset.UTC)
                : locationPointRepository.findAllByRunningSessionId(session.getId())
                .stream().reduce((i,j) -> j).get()
                .getTimestamp().toEpochSecond(ZoneOffset.UTC);
        double totalTimeInMinutes = (endSeconds - startSeconds) / 60.0;
        return totalTimeInMinutes;

    }

    public AnalysisResult predictPerformance(String userId) {
        DataFrame processedData = processData(userId);
        //Assuming 'distance' and elevation are the predictor and 'time' is the response
        Formula formula = Formula.of("time", "distance", "elevationGain");  // Only response variable specified, assuming only one predictor: 'distance'

        LinearModel model = OLS.fit(formula, processedData, new Properties());
        //RandomForest model = RandomForest.fit(formula, processedData);

        double[][] query = {
                {5.0, 0.0}, {10.0, 0.0}, {21.0975, 0.0}, {42.195, 0.0} //distances for predictions
        };

        double[] predictions = new double[query.length];
        for (int i = 0; i < query.length; i++) {
            DataFrame singlePoint = DataFrame.of(new double[][]{query[i]}, "distance", "elevationGain");
            try {
                double[] predictedValues = model.predict(singlePoint);
                predictions[i] = predictedValues[0];
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error during prediction: " + e.getMessage());
            }
        }
        return new AnalysisResult(predictions[0], predictions[1], predictions[2], predictions[3]);
    }

    private DataFrame processData(String userId) {
        List<RunningSession> sessions = runningSessionRepository.findAllByUserId(userId);
        List<double[]> data = sessions.stream()
                .map(session -> new double[] {
                        calculateTotalDistanceForSession(session.getId()),
                        calculateTotalTimeForSession(session),
                        DataAnalysisUtil.calculateTotalElevationGain(locationPointRepository.findAllByRunningSessionId(session.getId()))
                }).collect(Collectors.toList());

        double[] distances = data.stream().mapToDouble(d -> d[0]).toArray();
        double[] time = data.stream().mapToDouble(d -> d[1]).toArray();
        double[] elevationGains = data.stream().mapToDouble(d -> d[2]).toArray();

        return DataFrame.of(
                DoubleVector.of("distance", distances),
                DoubleVector.of("time", time),
                DoubleVector.of("elevationGain", elevationGains)
        );
    }

    public double calculateTotalDistanceForSession(Long sessionId) {
        List<LocationPoint> points = locationPointRepository.findAllByRunningSessionId(sessionId);

        Collections.sort(points, new Comparator<LocationPoint>() {
            @Override
            public int compare(LocationPoint p1, LocationPoint p2) {
                return p1.getTimestamp().compareTo(p2.getTimestamp());
            }
        });

        double totalDistance = 0.0;
        for (int i = 1; i < points.size(); i++) {
            LocationPoint prev = points.get(i - 1);
            LocationPoint curr = points.get(i);
            totalDistance += DataAnalysisUtil.haversineDistance(prev.getLatitude(), prev.getLongitude(),
                    curr.getLatitude(), curr.getLongitude());
        }
        return totalDistance;
    }

    public List<RunningSession> getAllSessionsByUserId(String userId) {
        return runningSessionRepository.findAllByUserId(userId);
    }
}
