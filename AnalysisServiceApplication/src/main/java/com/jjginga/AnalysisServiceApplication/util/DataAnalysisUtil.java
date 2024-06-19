package com.jjginga.AnalysisServiceApplication.util;

import com.jjginga.AnalysisServiceApplication.entity.LocationPoint;

import java.util.ArrayList;
import java.util.List;

public class DataAnalysisUtil {
    //earth radius
    private static final double EARTH_RADIUS_KM = 6371.0;

    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        //convert degrees to radians
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        //calculate the difference
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        //applying Haversine formula
        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.asin(Math.min(1, Math.sqrt(a)));

        //returns the distance in Kms
        return EARTH_RADIUS_KM * c;
    }

    public static List<LocationPoint> applyMovingAverageFilter(List<LocationPoint> points, int windowSize) {
        List<LocationPoint> smoothedPoints = new ArrayList<>();
        int halfWindow = windowSize / 2;

        for (int i = 0; i < points.size(); i++) {
            double sumLat = 0;
            double sumLon = 0;
            int count = 0;

            for (int j = -halfWindow; j <= halfWindow; j++) {
                int index = i + j;
                if (index >= 0 && index < points.size()) {
                    sumLat += points.get(index).getLatitude();
                    sumLon += points.get(index).getLongitude();
                    count++;
                }
            }

            double avgLat = sumLat / count;
            double avgLon = sumLon / count;

            LocationPoint point = new LocationPoint();
            point.setLatitude(avgLat);
            point.setLongitude(avgLon);
            point.setTimestamp(points.get(i).getTimestamp());
            point.setElevation(points.get(i).getElevation());

            smoothedPoints.add(point);
        }

        return smoothedPoints;
    }


    public static double calculateTotalElevationGain(List<LocationPoint> points) {
        double totalElevationGain = 0.0;
        LocationPoint previousPoint = null;
        for (LocationPoint point : points) {
            if (previousPoint != null && point.getElevation() > previousPoint.getElevation()) {
                totalElevationGain += point.getElevation() - previousPoint.getElevation();
            }
            previousPoint = point;
        }
        return totalElevationGain;
    }
}
