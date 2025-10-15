package com.gvkss.patil.claim_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Claim Controller for healthcare insurance claims
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/claims")
@CrossOrigin(origins = "*")
public class ClaimController {
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "claim-service");
        response.put("message", "Healthcare Claim Service is running");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllClaims() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Claims endpoint is working");
        response.put("data", "This is a placeholder for claims data");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createClaim(@RequestBody Map<String, Object> claimData) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Claim creation endpoint is working");
        response.put("data", claimData);
        return ResponseEntity.ok(response);
    }
}
