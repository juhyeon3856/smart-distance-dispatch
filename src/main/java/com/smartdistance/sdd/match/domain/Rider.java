package com.smartdistance.sdd.match.domain;

// 알고리즘 테스트용 : 추후 rider의 domain으로 이동하면 좋을듯
public class Rider {
    private final Long riderId;
    private final Location location;   // 배달원의 현재 위치

    public Rider(Long riderId, Location location) {
        this.riderId = riderId;
        this.location = location;
    }

    public Long getRiderId() { return riderId; }
    public Location getLocation() { return location; }
}
