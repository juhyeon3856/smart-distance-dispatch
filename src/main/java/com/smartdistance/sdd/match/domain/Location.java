package com.smartdistance.sdd.match.domain;

// 알고리즘 테스트용 : 추후 common의 domain으로 이동하면 좋을듯
public class Location {
    private final double latitude;   // 위도
    private final double longitude;  // 경도

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
