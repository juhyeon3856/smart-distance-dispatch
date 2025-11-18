package com.smartdistance.sdd.common.health.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KafkaHealthResponse {
    private String status;          // UP, DOWN
    private String message;         // 상태 메시지
    private LocalDateTime timestamp; // 체크 시간

    public static KafkaHealthResponse up() {
        return new KafkaHealthResponse(
                "UP",
                "Kafka is connected",
                LocalDateTime.now()
        );
    }

    public static KafkaHealthResponse down(String errorMessage) {
        return new KafkaHealthResponse(
                "DOWN",
                "Kafka connection failed: " + errorMessage,
                LocalDateTime.now()
        );
    }
}
