package com.smartdistance.sdd.match.domain.service;

import com.smartdistance.sdd.match.domain.Delivery;
import com.smartdistance.sdd.match.domain.DeliveryPair;
import com.smartdistance.sdd.match.domain.MatchResult;
import com.smartdistance.sdd.match.domain.Rider;
import java.util.*;

// 복잡도 O(n³)
// “전역 최적해” 보장, 큰 n에서 느림
public class HungarianMatchingService implements MatchingService {

    private static final int K_NEIGHBORS = 10;

    private final DeliveryPairingService pairingService;

    public HungarianMatchingService(DeliveryPairingService pairingService) {
        this.pairingService = pairingService;
    }

    // 1:1 헝가리안
    @Override
    public List<MatchResult> matchSingle(List<Rider> riders, List<Delivery> deliveries) {

        int n = Math.max(riders.size(), deliveries.size());
        double[][] cost = new double[n][n];

        // 비용행렬 생성 (없는 rider/delivery는 큰값)
        for (int i = 0; i < deliveries.size(); i++) {
            for (int j = 0; j < riders.size(); j++) {
                cost[i][j] = DistanceCalculator.distance(
                        riders.get(j).getLocation(),
                        deliveries.get(i).getStoreLocation()
                );
            }
        }
        // 나머지는 큰 비용
        for (int i = deliveries.size(); i < n; i++)
            Arrays.fill(cost[i], 1e9);
        for (int j = riders.size(); j < n; j++)
            for (int i = 0; i < n; i++) cost[i][j] = 1e9;

        // 헝가리안 알고리즘 실행
        int[] assignment = hungarian(cost);

        // 결과 생성
        List<MatchResult> results = new ArrayList<>();
        for (int i = 0; i < deliveries.size(); i++) {
            int riderIdx = assignment[i];
            if (riderIdx >= 0 && riderIdx < riders.size()) {
                results.add(
                        new MatchResult(
                                deliveries.get(i).getDeliveryId(),
                                riders.get(riderIdx).getRiderId(),
                                cost[i][riderIdx]
                        )
                );
            }
        }
        return results;
    }


    // 2:1 헝가리안 (세트 vs rider)
    @Override
    public List<MatchResult> matchDual(List<Rider> riders,
                                       List<Delivery> deliveries) {

        List<DeliveryPair> pairs = pairingService.buildPairs(deliveries, K_NEIGHBORS);

        int P = pairs.size();
        int R = riders.size();
        int n = Math.max(P, R);

        double[][] cost = new double[n][n];

        for (int i = 0; i < P; i++) {
            for (int j = 0; j < R; j++) {
                cost[i][j] = DistanceCalculator.distance(
                        riders.get(j).getLocation(),
                        pairs.get(i).getRepresentative()
                );
            }
        }
        for (int i = P; i < n; i++) {
            Arrays.fill(cost[i], 1e9);
        }
        for (int j = R; j < n; j++) {
            for (int i = 0; i < n; i++) cost[i][j] = 1e9;
        }

        int[] assignment = hungarian(cost); // i(pair) -> j(rider)

        List<MatchResult> results = new ArrayList<>();

        for (int i = 0; i < P; i++) {
            int riderIdx = assignment[i];
            if (riderIdx < 0 || riderIdx >= R) continue;

            Rider rider = riders.get(riderIdx);
            DeliveryPair pair = pairs.get(i);

            Delivery d1 = pair.getFirst();
            results.add(new MatchResult(
                    d1.getDeliveryId(),
                    rider.getRiderId(),
                    DistanceCalculator.distance(
                            rider.getLocation(),
                            d1.getStoreLocation())
            ));

            Delivery d2 = pair.getSecond();
            if (d2 != null) {
                results.add(new MatchResult(
                        d2.getDeliveryId(),
                        rider.getRiderId(),
                        DistanceCalculator.distance(
                                rider.getLocation(),
                                d2.getStoreLocation())
                ));
            }
        }
        return results;
    }

    // 헝가리안 구현
    private int[] hungarian(double[][] cost) {
        int n = cost.length;
        double[] u = new double[n + 1];
        double[] v = new double[n + 1];
        int[] p = new int[n + 1];
        int[] way = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            p[0] = i;
            double[] minv = new double[n + 1];
            Arrays.fill(minv, Double.MAX_VALUE);
            boolean[] used = new boolean[n + 1];
            int j0 = 0;

            do {
                used[j0] = true;
                int i0 = p[j0], j1 = 0;
                double delta = Double.MAX_VALUE;

                for (int j = 1; j <= n; j++) {
                    if (!used[j]) {
                        double cur = cost[i0 - 1][j - 1] - u[i0] - v[j];
                        if (cur < minv[j]) {
                            minv[j] = cur;
                            way[j] = j0;
                        }
                        if (minv[j] < delta) {
                            delta = minv[j];
                            j1 = j;
                        }
                    }
                }

                for (int j = 0; j <= n; j++) {
                    if (used[j]) {
                        u[p[j]] += delta;
                        v[j] -= delta;
                    } else {
                        minv[j] -= delta;
                    }
                }
                j0 = j1;
            } while (p[j0] != 0);

            do {
                int j1 = way[j0];
                p[j0] = p[j1];
                j0 = j1;
            } while (j0 != 0);
        }

        int[] res = new int[n];
        for (int j = 1; j <= n; j++) {
            res[p[j] - 1] = j - 1;
        }
        return res;
    }

}
