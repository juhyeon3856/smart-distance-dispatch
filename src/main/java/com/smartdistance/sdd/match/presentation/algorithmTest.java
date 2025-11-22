package com.smartdistance.sdd.match.presentation;

import com.smartdistance.sdd.match.domain.Delivery;
import com.smartdistance.sdd.match.domain.KdTree;
import com.smartdistance.sdd.match.domain.Location;
import com.smartdistance.sdd.match.domain.MatchResult;
import com.smartdistance.sdd.match.domain.Rider;
import com.smartdistance.sdd.match.domain.service.DeliveryPairingService;
import com.smartdistance.sdd.match.domain.service.GreedyMatchingService;
import com.smartdistance.sdd.match.domain.service.HungarianMatchingService;
import com.smartdistance.sdd.match.domain.service.KdTreeMatchingService;
import com.smartdistance.sdd.match.domain.service.MatchingService;
import java.util.ArrayList;
import java.util.List;

public class algorithmTest {

    public static void main(String[] args) {

        // ---------------------------
        // 1. 샘플 데이터 생성
        // ---------------------------
        List<Rider> riders = buildSampleRiders();
        List<Delivery> deliveries = buildSampleDeliveries();

        // ---------------------------
        // 2. 서비스 준비
        // ---------------------------
        DeliveryPairingService pairingService = new DeliveryPairingService();

        MatchingService greedyService =
                new GreedyMatchingService(pairingService);

        MatchingService hungarianService =
                new HungarianMatchingService(pairingService);

        MatchingService kdTreeService =
                new KdTreeMatchingService(pairingService, new KdTree());

        // ---------------------------
        // 3. 1:1 매칭 테스트
        // ---------------------------
        System.out.println("===== [Greedy] 1:1 Matching =====");
        List<MatchResult> greedySingle = greedyService.matchSingle(riders, deliveries);
        MatchVisualizer.printAsciiMap(riders, deliveries, greedySingle);

        System.out.println("===== [Hungarian] 1:1 Matching =====");
        List<MatchResult> hungarianSingle = hungarianService.matchSingle(riders, deliveries);
        MatchVisualizer.printAsciiMap(riders, deliveries, hungarianSingle);

        System.out.println("===== [KD-Tree] 1:1 Matching =====");
        List<MatchResult> kdSingle = kdTreeService.matchSingle(riders, deliveries);
        MatchVisualizer.printAsciiMap(riders, deliveries, kdSingle);

        // ---------------------------
        // 4. 2:1 매칭 테스트
        // ---------------------------
        System.out.println("===== [Greedy] 2:1 Matching =====");
        List<MatchResult> greedyDual = greedyService.matchDual(riders, deliveries);
        MatchVisualizer.printAsciiMap(riders, deliveries, greedyDual);

        System.out.println("===== [Hungarian] 2:1 Matching =====");
        List<MatchResult> hungarianDual = hungarianService.matchDual(riders, deliveries);
        MatchVisualizer.printAsciiMap(riders, deliveries, hungarianDual);

        System.out.println("===== [KD-Tree] 2:1 Matching =====");
        List<MatchResult> kdDual = kdTreeService.matchDual(riders, deliveries);
        MatchVisualizer.printAsciiMap(riders, deliveries, kdDual);
    }

    // ---------------------------
    // 샘플 데이터 생성 메서드들
    // ---------------------------

    private static List<Rider> buildSampleRiders() {
        List<Rider> riders = new ArrayList<>();

        // TODO: Rider 생성자/빌더에 맞게 수정
        // 예: new Rider(Long riderId, Location location)
        riders.add(new Rider(1L, new Location(0, 0)));
        riders.add(new Rider(2L, new Location(0, 10)));
        riders.add(new Rider(3L, new Location(10, 0)));
        riders.add(new Rider(4L, new Location(10, 10)));

        return riders;
    }

    private static List<Delivery> buildSampleDeliveries() {
        List<Delivery> deliveries = new ArrayList<>();

        // TODO: Delivery 생성자/빌더에 맞게 수정
        // 예: new Delivery(Long id, Location storeLocation, Location customerLocation)

        // Rider 1 근처
        deliveries.add(new Delivery(
                101L,
                new Location(1, 1),   // store
                new Location(2, 2)    // customer
        ));
        deliveries.add(new Delivery(
                102L,
                new Location(2, 1),
                new Location(3, 1)
        ));

        // Rider 2 근처
        deliveries.add(new Delivery(
                103L,
                new Location(1, 9),
                new Location(2, 8)
        ));
        deliveries.add(new Delivery(
                104L,
                new Location(2, 10),
                new Location(3, 9)
        ));

        // Rider 3 근처
        deliveries.add(new Delivery(
                105L,
                new Location(9, 1),
                new Location(8, 2)
        ));
        deliveries.add(new Delivery(
                106L,
                new Location(8, 0),
                new Location(9, 2)
        ));

        // Rider 4 근처
        deliveries.add(new Delivery(
                107L,
                new Location(9, 9),
                new Location(8, 8)
        ));
        deliveries.add(new Delivery(
                108L,
                new Location(10, 8),
                new Location(9, 10)
        ));

        return deliveries;
    }

}
