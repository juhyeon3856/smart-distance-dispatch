package com.smartdistance.sdd.match.domain.service;

import com.smartdistance.sdd.match.domain.*;
import java.util.*;

// 복잡도: O(D log R)
// 매우 빠름, 근사 최적해(실제 서비스에 많이 사용)
public class KdTreeMatchingService implements MatchingService {


    private static final int K_NEIGHBORS = 10;

    private final DeliveryPairingService pairingService;
    private final KdTree kdTree;

    public KdTreeMatchingService(DeliveryPairingService pairingService,
                                 KdTree kdTree) {
        this.pairingService = pairingService;
        this.kdTree = kdTree;
    }

    // 1:1 KD-Tree 기반
    @Override
    public List<MatchResult> matchSingle(List<Rider> riders,
                                         List<Delivery> deliveries) {

        kdTree.build(riders);
        Set<Long> used = new HashSet<>();
        List<MatchResult> results = new ArrayList<>();

        for (Delivery delivery : deliveries) {
            Rider nearest = kdTree.findNearest(delivery.getStoreLocation(), used);
            if (nearest == null) continue;
            used.add(nearest.getRiderId());

            results.add(new MatchResult(
                    delivery.getDeliveryId(),
                    nearest.getRiderId(),
                    DistanceCalculator.distance(
                            nearest.getLocation(),
                            delivery.getStoreLocation())
            ));
        }

        return results;
    }

    // 2:1 KD-Tree 기반 (세트 기준)
    @Override
    public List<MatchResult> matchDual(List<Rider> riders,
                                       List<Delivery> deliveries) {

        List<DeliveryPair> pairs = pairingService.buildPairs(deliveries, K_NEIGHBORS);
        kdTree.build(riders);

        Set<Long> used = new HashSet<>();
        List<MatchResult> results = new ArrayList<>();

        for (DeliveryPair pair : pairs) {
            Rider nearest = kdTree.findNearest(pair.getRepresentative(), used);
            if (nearest == null) continue;
            used.add(nearest.getRiderId());

            Delivery d1 = pair.getFirst();
            results.add(new MatchResult(
                    d1.getDeliveryId(),
                    nearest.getRiderId(),
                    DistanceCalculator.distance(
                            nearest.getLocation(),
                            d1.getStoreLocation())
            ));

            Delivery d2 = pair.getSecond();
            if (d2 != null) {
                results.add(new MatchResult(
                        d2.getDeliveryId(),
                        nearest.getRiderId(),
                        DistanceCalculator.distance(
                                nearest.getLocation(),
                                d2.getStoreLocation())
                ));
            }
        }

        return results;
    }
}
