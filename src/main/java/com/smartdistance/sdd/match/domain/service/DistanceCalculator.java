package com.smartdistance.sdd.match.domain.service;

import com.smartdistance.sdd.match.domain.Location;

public class DistanceCalculator {

    // 유클리드 거리
    public static double distance(Location a, Location b) {
        double latDiff = a.getLatitude() - b.getLatitude();
        double lonDiff = a.getLongitude() - b.getLongitude();
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
    }
}
