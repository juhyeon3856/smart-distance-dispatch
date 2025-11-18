package com.smartdistance.sdd.common.health.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KafkaTestResponse {
    private boolean success;         // 전송 성공 여부
    private String message;          // 응답 메시지
    private String topic;            // 전송된 토픽 이름
    private LocalDateTime timestamp; // 전송 시간

    public static KafkaTestResponse success(String topic) {
        return new KafkaTestResponse(
                true,
                "Test message sent successfully",
                topic,
                LocalDateTime.now()
        );
    }

    public static KafkaTestResponse fail(String errorMessage) {
        return new KafkaTestResponse(
                false,
                "Failed to send test message: " + errorMessage,
                null,
                LocalDateTime.now()
        );
    }
}
