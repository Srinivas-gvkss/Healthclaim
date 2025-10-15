package com.gvkss.patil.patient_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Patient Controller for healthcare patient management
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "patient-service");
        response.put("message", "Healthcare Patient Service is running");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPatients() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Patients endpoint is working");
        response.put("data", "This is a placeholder for patients data");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPatientById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Patient details endpoint is working");
        response.put("data", "Patient ID: " + id);
        return ResponseEntity.ok(response);
    }
}
