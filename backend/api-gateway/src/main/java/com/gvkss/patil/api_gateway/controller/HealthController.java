package com.gvkss.patil.api_gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Health check controller for API Gateway
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@RestController
public class HealthController {
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "api-gateway",
            "timestamp", LocalDateTime.now().toString()
        ));
    }
}
