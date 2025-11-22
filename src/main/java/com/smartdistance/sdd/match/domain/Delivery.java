package com.smartdistance.sdd.match.domain;

// 알고리즘 테스트용 : 추후 delivery의 domain으로 이동하면 좋을듯
public class Delivery {
    private final Long deliveryId;
    private final Location storeLocation;     // 식당 위치
    private final Location customerLocation;  // 주문자 위치

    public Delivery(Long deliveryId,
                         Location storeLocation,
                         Location customerLocation) {
        this.deliveryId = deliveryId;
        this.storeLocation = storeLocation;
        this.customerLocation = customerLocation;
    }

    public Long getDeliveryId() { return deliveryId; }
    public Location getStoreLocation() { return storeLocation; }
    public Location getCustomerLocation() { return customerLocation; }
}
