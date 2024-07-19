package com.volunnear.utils;

public class DistanceCalculator {

    private static final int EARTH_RADIUS = 6371;

    public static double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double latitudeDistance = Math.toRadians(latitude2 - latitude1);
        double longitudeDistance = Math.toRadians(longitude2 - longitude1);

        double a = Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2)
                + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2))
                * Math.sin(longitudeDistance / 2) * Math.sin(longitudeDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
