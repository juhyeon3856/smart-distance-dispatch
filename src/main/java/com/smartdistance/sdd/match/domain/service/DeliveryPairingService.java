package com.smartdistance.sdd.match.domain.service;

import com.smartdistance.sdd.match.domain.Delivery;
import com.smartdistance.sdd.match.domain.DeliveryPair;

import java.util.*;

public class DeliveryPairingService {

    /**
     * 정렬 + 앞뒤 k개만 보는 근사 페어링
     */
    public List<DeliveryPair> buildPairs(List<Delivery> deliveries, int kNeighbors) {
        int n = deliveries.size();
        if (n == 0) {
            return Collections.emptyList();
        }

        // store 위치 기준 정렬 (lat, lon)
        List<Delivery> sorted = new ArrayList<>(deliveries);
        sorted.sort(Comparator
                .comparingDouble((Delivery d) -> d.getStoreLocation().getLatitude())
                .thenComparingDouble(d -> d.getStoreLocation().getLongitude()));

        boolean[] used = new boolean[n];
        List<DeliveryPair> pairs = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (used[i]) continue;

            Delivery base = sorted.get(i);
            int bestIdx = -1;
            double bestScore = Double.MAX_VALUE;

            // i+1 ~ i+kNeighbors 후보만 탐색
            int limit = Math.min(n, i + 1 + kNeighbors);
            for (int j = i + 1; j < limit; j++) {
                if (used[j]) continue;
                Delivery cand = sorted.get(j);

                double score = DistanceCalculator.distance(
                        base.getStoreLocation(),
                        cand.getStoreLocation()
                );
                if (score < bestScore) {
                    bestScore = score;
                    bestIdx = j;
                }
            }

            if (bestIdx != -1) {
                used[i] = true;
                used[bestIdx] = true;
                pairs.add(new DeliveryPair(base, sorted.get(bestIdx)));
            } else {
                used[i] = true;
                pairs.add(new DeliveryPair(base, null)); // 짝 없는 싱글
            }
        }

        return pairs;
    }
}
