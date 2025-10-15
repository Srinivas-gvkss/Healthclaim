package com.gvkss.patil.notification_service.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.*;

/**
 * Notification Controller
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "notification-service");
        response.put("timestamp", new Date());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getNotifications() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Notification service is running");
        response.put("data", new ArrayList<>());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createNotification(@RequestBody Map<String, Object> notification) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Notification created successfully");
        response.put("data", notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getNotification(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Notification retrieved successfully");
        response.put("data", Map.of("id", id, "message", "Sample notification"));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateNotification(@PathVariable Long id, @RequestBody Map<String, Object> notification) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Notification updated successfully");
        response.put("data", notification);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteNotification(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Notification deleted successfully");
        return ResponseEntity.ok(response);
    }
}
