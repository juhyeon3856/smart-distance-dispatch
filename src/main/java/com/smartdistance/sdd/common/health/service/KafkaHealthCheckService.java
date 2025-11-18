package com.smartdistance.sdd.common.health.service;

import com.smartdistance.sdd.common.health.dto.KafkaHealthResponse;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaHealthCheckService {
    private final KafkaAdmin kafkaAdmin;

    public KafkaHealthResponse checkKafkaHealth() {
        try (AdminClient adminClient =
                     AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            // Kafka 연결 테스트 (타임아웃 3초)
            adminClient.describeCluster().nodes().get(3, TimeUnit.SECONDS);

            log.info("Kafka health check: UP");
            return KafkaHealthResponse.up();

        } catch (Exception e) {
            log.error("Kafka health check failed: {}", e.getMessage());
            return KafkaHealthResponse.down(e.getMessage());
        }
    }
}
