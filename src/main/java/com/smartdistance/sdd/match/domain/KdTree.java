package com.smartdistance.sdd.match.domain;

import com.smartdistance.sdd.match.domain.service.DistanceCalculator;

import java.util.Set;

public class KdTree {

    private static class Node {
        Rider rider;
        Node left;
        Node right;
        boolean vertical; // true: x 기준, false: y 기준

        Node(Rider rider, boolean vertical) {
            this.rider = rider;
            this.vertical = vertical;
        }
    }

    private Node root;

    public void build(Iterable<Rider> riders) {
        root = null;
        for (Rider rider : riders) {
            root = insert(root, rider, true);
        }
    }

    private Node insert(Node node, Rider rider, boolean vertical) {
        if (node == null) {
            return new Node(rider, vertical);
        }

        double rv = vertical ? rider.getLocation().getLatitude()
                : rider.getLocation().getLongitude();
        double nv = vertical ? node.rider.getLocation().getLatitude()
                : node.rider.getLocation().getLongitude();

        if (rv < nv) {
            node.left = insert(node.left, rider, !vertical);
        } else {
            node.right = insert(node.right, rider, !vertical);
        }
        return node;
    }

    public Rider findNearest(Location target, Set<Long> usedRiderIds) {
        return nearest(root, target, usedRiderIds, null, Double.MAX_VALUE);
    }

    private Rider nearest(Node node,
                          Location target,
                          Set<Long> used,
                          Rider best,
                          double bestDist) {

        if (node == null) return best;

        // 1) 현재 노드의 rider가 후보인지 확인
        if (!used.contains(node.rider.getRiderId())) {
            double dist = DistanceCalculator.distance(node.rider.getLocation(), target);
            if (dist < bestDist) {
                bestDist = dist;
                best = node.rider;
            }
        }

        // 2) 분할 기준에 따라 우선 탐색 방향 선택
        double cmp = node.vertical
                ? target.getLatitude() - node.rider.getLocation().getLatitude()
                : target.getLongitude() - node.rider.getLocation().getLongitude();

        Node first = (cmp < 0) ? node.left : node.right;
        Node second = (cmp < 0) ? node.right : node.left;

        // 3) 우선 서브트리 탐색
        best = nearest(first, target, used, best, bestDist);

        // bestDist 업데이트
        if (best != null) {
            bestDist = DistanceCalculator.distance(best.getLocation(), target);
        }

        // 4) 두 번째 서브트리 검사 필요성
        double splitDist = Math.abs(cmp);
        if (splitDist < bestDist) {
            best = nearest(second, target, used, best, bestDist);
        }

        return best;
    }

}
