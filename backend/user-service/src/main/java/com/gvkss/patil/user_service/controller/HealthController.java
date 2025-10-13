package com.gvkss.patil.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Health check controller for monitoring service availability.
 * Provides a simple endpoint to verify the service is running.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@RestController
public class HealthController {
    
    /**
     * Health check endpoint
     * 
     * @return Health status response
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "healthcare-user-service",
            "timestamp", LocalDateTime.now().toString()
        ));
    }
}

