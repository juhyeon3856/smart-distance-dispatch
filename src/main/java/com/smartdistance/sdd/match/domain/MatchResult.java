package com.smartdistance.sdd.match.domain;

// 한 건의 매칭 결과
public class MatchResult {
    private final Long deliveryId;   // 어떤 배달 주문인지
    private final Long riderId;      // 어떤 배달원에게 배정됐는지
    private final double cost;       // 거리/시간 등 비용(테스트용)

    public MatchResult(Long deliveryId, Long riderId, double cost) {
        this.deliveryId = deliveryId;
        this.riderId = riderId;
        this.cost = cost;
    }

    public Long getDeliveryId() { return deliveryId; }
    public Long getRiderId() { return riderId; }
    public double getCost() { return cost; }
}
