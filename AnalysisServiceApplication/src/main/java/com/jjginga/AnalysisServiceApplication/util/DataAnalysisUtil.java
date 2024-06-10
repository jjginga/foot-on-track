package com.jjginga.AnalysisServiceApplication.util;

import com.jjginga.AnalysisServiceApplication.entity.LocationPoint;

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
