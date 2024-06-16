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
import java.util.List;
import java.util.Properties;
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

    public AnalysisResult predictPerformance(Long userId) {
        DataFrame processedData = processData(userId);
        // Assuming 'distance' is the predictor and 'time' is the response
        Formula formula = Formula.lhs("time");  // Only response variable specified, assuming only one predictor: 'distance'

        LinearModel model = OLS.fit(formula, processedData, new Properties());

        double[][] query = {
                {5.0}, {10.0}, {21.0975}, {42.195} // Distances for predictions
        };

        double[] predictions = new double[query.length];
        for (int i = 0; i < query.length; i++) {
            predictions[i] = model.predict(query[i]);
        }

        return new AnalysisResult(predictions[0], predictions[1], predictions[2], predictions[3]);
    }

    private DataFrame processData(Long userId) {
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
        double totalDistance = 0.0;
        for (int i = 1; i < points.size(); i++) {
            LocationPoint prev = points.get(i - 1);
            LocationPoint curr = points.get(i);
            totalDistance += DataAnalysisUtil.haversineDistance(prev.getLatitude(), prev.getLongitude(),
                    curr.getLatitude(), curr.getLongitude());
        }
        return totalDistance;
    }

    public List<RunningSession> getAllSessionsByUserId(Long userId) {
        return runningSessionRepository.findAllByUserId(userId);
    }
}
