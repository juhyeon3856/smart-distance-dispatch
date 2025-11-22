package com.smartdistance.sdd.match.domain;

public class DeliveryPair {

    private final Delivery first;
    private final Delivery second;          // null이면 싱글 세트
    private final Location representative;  // 세트 대표 위치 (가게 위치 평균)

    public DeliveryPair(Delivery first, Delivery second) {
        this.first = first;
        this.second = second;
        this.representative = computeRepresentative(first, second);
    }

    private Location computeRepresentative(Delivery d1, Delivery d2) {
        if (d2 == null) {
            return d1.getStoreLocation();
        }
        double lat = (d1.getStoreLocation().getLatitude()
                + d2.getStoreLocation().getLatitude()) / 2.0;
        double lon = (d1.getStoreLocation().getLongitude()
                + d2.getStoreLocation().getLongitude()) / 2.0;
        return new Location(lat, lon);
    }

    public Delivery getFirst() {
        return first;
    }

    public Delivery getSecond() {
        return second;
    }

    public Location getRepresentative() {
        return representative;
    }
}
