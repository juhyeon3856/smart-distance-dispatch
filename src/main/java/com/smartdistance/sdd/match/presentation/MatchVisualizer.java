package com.smartdistance.sdd.match.presentation;

import com.smartdistance.sdd.match.domain.Delivery;
import com.smartdistance.sdd.match.domain.Location;
import com.smartdistance.sdd.match.domain.MatchResult;
import com.smartdistance.sdd.match.domain.Rider;

import java.util.*;

public class MatchVisualizer {

    public static void printAsciiMap(
            List<Rider> riders,
            List<Delivery> deliveries,
            List<MatchResult> matches
    ) {
        // -------- 1. 좌표 범위 계산 --------
        double maxX = 0;
        double maxY = 0;

        for (Rider r : riders) {
            Location loc = r.getLocation();
            maxX = Math.max(maxX, loc.getLatitude());
            maxY = Math.max(maxY, loc.getLongitude());
        }
        for (Delivery d : deliveries) {
            Location s = d.getStoreLocation();
            Location c = d.getCustomerLocation();
            maxX = Math.max(maxX, Math.max(s.getLatitude(), c.getLatitude()));
            maxY = Math.max(maxY, Math.max(s.getLongitude(), c.getLongitude()));
        }

        int width = (int) Math.ceil(maxX) + 3;
        int height = (int) Math.ceil(maxY) + 3;

        // -------- 2. 기본 지도 초기화 --------
        String[][] grid = new String[height][width];
        for (int y = 0; y < height; y++) {
            Arrays.fill(grid[y], ".");
        }

        // ID → 객체 맵
        Map<Long, Rider> riderMap = new HashMap<>();
        for (Rider r : riders) riderMap.put(r.getRiderId(), r);

        Map<Long, Delivery> deliveryMap = new HashMap<>();
        for (Delivery d : deliveries) deliveryMap.put(d.getDeliveryId(), d);

        // -------- 3. 매칭 세트 번호 부여 (2:1 대응) --------
        // 세트 번호는 riderId 기준으로 그룹핑
        Map<Long, Integer> riderSetNo = new HashMap<>();
        Map<Long, Integer> deliverySetNo = new HashMap<>();

        int set = 1;
        Map<Long, List<MatchResult>> byRider = new HashMap<>();

        // riderId → List<MatchResult>
        for (MatchResult m : matches) {
            byRider.computeIfAbsent(m.getRiderId(), k -> new ArrayList<>())
                    .add(m);
        }

        for (Long riderId : byRider.keySet()) {
            riderSetNo.put(riderId, set);
            for (MatchResult m : byRider.get(riderId)) {
                deliverySetNo.put(m.getDeliveryId(), set);
            }
            set++;
        }

        // -------- 4. 배달원 라벨 찍기 (R(set)) --------
        for (Rider r : riders) {
            int x = (int) Math.round(r.getLocation().getLatitude());
            int y = (int) Math.round(r.getLocation().getLongitude());
            int row = height - 1 - y;

            Integer setNo = riderSetNo.get(r.getRiderId());
            String label = (setNo != null) ? "R(" + setNo + ")" : "R";
            putLabel(grid, row, x, label);
        }

        // -------- 5. 음식점/주문자 라벨 찍기 (F(set), C(set)) --------
        for (Delivery d : deliveries) {
            Long id = d.getDeliveryId();
            Integer setNo = deliverySetNo.get(id);

            int sx = (int) Math.round(d.getStoreLocation().getLatitude());
            int sy = (int) Math.round(d.getStoreLocation().getLongitude());
            int sRow = height - 1 - sy;
            String fLabel = (setNo != null) ? "F(" + setNo + ")" : "F";
            putLabel(grid, sRow, sx, fLabel);

            int cx = (int) Math.round(d.getCustomerLocation().getLatitude());
            int cy = (int) Math.round(d.getCustomerLocation().getLongitude());
            int cRow = height - 1 - cy;
            String cLabel = (setNo != null) ? "C(" + setNo + ")" : "C";
            putLabel(grid, cRow, cx, cLabel);
        }

        // -------- 6. 매칭 결과 출력 --------
        System.out.println("\n=== Matching Result ===");

        for (Long riderId : byRider.keySet()) {
            int group = riderSetNo.get(riderId);

            System.out.println("\n[SET " + group + "]");
            for (MatchResult m : byRider.get(riderId)) {
                System.out.printf(
                        " Delivery %d -> Rider %d (cost=%.2f)%n",
                        m.getDeliveryId(), m.getRiderId(), m.getCost()
                );
            }
        }

        // -------- 7. 지도 출력 --------
        System.out.println("\n=== Map (R = Rider, F = Store, C = Customer) ===");

        for (int row = 0; row < height; row++) {
            int yVal = height - 1 - row;
            System.out.printf("%2d | ", yVal);

            for (int col = 0; col < width; col++) {
                System.out.printf("%7s", grid[row][col]);
            }
            System.out.println();
        }

        System.out.print("    ");
        for (int col = 0; col < width; col++) {
            System.out.print("-------");
        }
        System.out.println();

        System.out.print("     ");
        for (int col = 0; col < width; col++) {
            System.out.printf("%7d", col);
        }
        System.out.println("\n");
    }

    // 기존과 동일
    private static void putLabel(String[][] grid, int row, int col, String label) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length) {
            return;
        }
        if (".".equals(grid[row][col])) {
            grid[row][col] = label;
        } else {
            grid[row][col] = grid[row][col] + "*";
        }
    }
}
