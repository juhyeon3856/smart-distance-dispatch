package com.smartdistance.sdd.common.health.controller;

import com.smartdistance.sdd.common.health.dto.KafkaHealthResponse;
import com.smartdistance.sdd.common.health.service.KafkaHealthCheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health Check", description = "시스템 헬스 체크 API")
@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {
    private final KafkaHealthCheckService kafkaHealthCheckService;

    @Operation(summary = "Kafka 헬스 체크", description = "Kafka 연결 상태 및 클러스터 정보 확인")
    @GetMapping("/kafka")
    public ResponseEntity<KafkaHealthResponse> checkKafkaHealth() {
        KafkaHealthResponse response = kafkaHealthCheckService.checkKafkaHealth();

        // DOWN 상태면 503 반환
        if ("DOWN".equals(response.getStatus())) {
            return ResponseEntity.status(503).body(response);
        }

        return ResponseEntity.ok(response);
    }
}
