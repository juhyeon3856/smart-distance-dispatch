package com.smartdistance.sdd.match.domain.service;

import com.smartdistance.sdd.match.domain.Delivery;
import com.smartdistance.sdd.match.domain.MatchResult;
import com.smartdistance.sdd.match.domain.Rider;
import java.util.List;

public interface MatchingService {

    /**
     * 배달과 배달원 1:1매칭
     * @param riders    배달원 위치 리스트
     * @param deliveries 배달(주문) 리스트
     * @return 각 배달에 대해 어느 배달원에게 배정했는지 리스트
     */
    List<MatchResult> matchSingle(List<Rider> riders,
                            List<Delivery> deliveries);

    /**
     * 배달과 배달원 2:1매칭
     * @param riders    배달원 위치 리스트
     * @param deliveries 배달(주문) 리스트
     * @return 각 배달에 대해 어느 배달원에게 배정했는지 리스트
     */
    List<MatchResult> matchDual(List<Rider> riders,
                                  List<Delivery> deliveries);
}
