package com.gvkss.patil.doctor_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Doctor Controller for healthcare doctor management
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
public class DoctorController {
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "doctor-service");
        response.put("message", "Healthcare Doctor Service is running");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDoctors() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Doctors endpoint is working");
        response.put("data", "This is a placeholder for doctors data");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDoctorById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Doctor details endpoint is working");
        response.put("data", "Doctor ID: " + id);
        return ResponseEntity.ok(response);
    }
}
