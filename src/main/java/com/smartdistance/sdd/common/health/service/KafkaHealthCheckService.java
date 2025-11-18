package com.smartdistance.sdd.common.health.service;

import com.smartdistance.sdd.common.health.dto.KafkaHealthResponse;
import com.smartdistance.sdd.common.health.dto.KafkaTestResponse;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaHealthCheckService {
    private final KafkaAdmin kafkaAdmin;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TEST_TOPIC = "health-check-test";

    // 헬스체크
    public KafkaHealthResponse checkKafkaHealth() {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            // Kafka 연결 테스트 (타임아웃 3초)
            adminClient.describeCluster().nodes().get(3, TimeUnit.SECONDS);

            log.info("Kafka health check: UP");
            return KafkaHealthResponse.up();

        } catch (Exception e) {
            log.error("Kafka health check failed: {}", e.getMessage());
            return KafkaHealthResponse.down(e.getMessage());
        }
    }

    // 메시지 전송
    public KafkaTestResponse sendTestMessage(String message) {
        try {
            // 테스트 토픽으로 메시지 전송
            kafkaTemplate.send(TEST_TOPIC, message);

            log.info("Test message sent to topic '{}': {}", TEST_TOPIC, message);
            return KafkaTestResponse.success(TEST_TOPIC);

        } catch (Exception e) {
            log.error("Failed to send test message: {}", e.getMessage());
            return KafkaTestResponse.fail(e.getMessage());
        }
    }
}
