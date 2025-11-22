package com.smartdistance.sdd.match.domain.service;

import com.smartdistance.sdd.match.domain.Delivery;
import com.smartdistance.sdd.match.domain.DeliveryPair;
import com.smartdistance.sdd.match.domain.MatchResult;
import com.smartdistance.sdd.match.domain.Rider;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// 복잡도: O(D × R)
//구현 단순, 빠름, 최적 보장 X
public class GreedyMatchingService implements MatchingService {

    private static final int K_NEIGHBORS = 10;

    private final DeliveryPairingService pairingService;

    public GreedyMatchingService(DeliveryPairingService pairingService) {
        this.pairingService = pairingService;
    }

    // 1:1 match
    @Override
    public List<MatchResult> matchSingle(List<Rider> riders,
                                   List<Delivery> deliveries) {

        List<MatchResult> results = new ArrayList<>();

        // 이미 사용된 배달원 체크 (1:1 매칭 가정)
        Set<Long> usedRiders = new HashSet<>();

        for (Delivery delivery : deliveries) {
            Rider bestRider = null;
            double bestCost = Double.MAX_VALUE;

            for (Rider rider : riders) {
                if (usedRiders.contains(rider.getRiderId())) {
                    continue;
                }

                // 예시: 배달원 → 식당 거리만 비용으로 사용
                double cost = DistanceCalculator.distance(
                        rider.getLocation(),
                        delivery.getStoreLocation()
                );

                if (cost < bestCost) {
                    bestCost = cost;
                    bestRider = rider;
                }
            }

            if (bestRider != null) {
                usedRiders.add(bestRider.getRiderId());
                results.add(
                        new MatchResult(delivery.getDeliveryId(),
                                bestRider.getRiderId(),
                                bestCost)
                );
            }
        }

        return results;
    }

    // 2:1 매칭 (세트 기준)
    @Override
    public List<MatchResult> matchDual(List<Rider> riders,
                                       List<Delivery> deliveries) {

        List<DeliveryPair> pairs = pairingService.buildPairs(deliveries, K_NEIGHBORS);

        List<MatchResult> results = new ArrayList<>();
        Set<Long> usedRiders = new HashSet<>();

        for (DeliveryPair pair : pairs) {
            Rider bestRider = null;
            double bestCost = Double.MAX_VALUE;

            for (Rider rider : riders) {
                if (usedRiders.contains(rider.getRiderId())) continue;

                double cost = DistanceCalculator.distance(
                        rider.getLocation(),
                        pair.getRepresentative()
                );
                if (cost < bestCost) {
                    bestCost = cost;
                    bestRider = rider;
                }
            }

            if (bestRider == null) continue;
            usedRiders.add(bestRider.getRiderId());

            // 세트 안의 배달들을 모두 이 rider에게 할당
            Delivery d1 = pair.getFirst();
            results.add(new MatchResult(
                    d1.getDeliveryId(),
                    bestRider.getRiderId(),
                    DistanceCalculator.distance(
                            bestRider.getLocation(),
                            d1.getStoreLocation())
            ));

            Delivery d2 = pair.getSecond();
            if (d2 != null) {
                results.add(new MatchResult(
                        d2.getDeliveryId(),
                        bestRider.getRiderId(),
                        DistanceCalculator.distance(
                                bestRider.getLocation(),
                                d2.getStoreLocation())
                ));
            }
        }

        return results;
    }
}
