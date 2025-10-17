package com.gvkss.patil.patient_service.controller;

import com.gvkss.patil.patient_service.dto.*;
import com.gvkss.patil.patient_service.entity.Patient;
import com.gvkss.patil.patient_service.service.PatientService;
import com.gvkss.patil.patient_service.service.MedicalRecordService;
import com.gvkss.patil.patient_service.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Patient Management", description = "Healthcare patient management APIs")
public class PatientController {
    
    private final PatientService patientService;
    private final MedicalRecordService medicalRecordService;
    private final AppointmentService appointmentService;
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the patient service is running")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "patient-service");
        response.put("message", "Healthcare Patient Service is running");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get patient by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID", description = "Retrieve a specific patient by their ID")
    public ResponseEntity<Map<String, Object>> getPatientById(
            @Parameter(description = "Patient ID") @PathVariable Long id) {
        try {
            PatientResponse patient = patientService.getPatientById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Patient retrieved successfully");
            response.put("data", patient);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving patient: {}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve patient: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get patient by user ID
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get patient by user ID", description = "Retrieve a patient by their user ID")
    public ResponseEntity<Map<String, Object>> getPatientByUserId(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        try {
            PatientResponse patient = patientService.getPatientByUserId(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Patient retrieved successfully");
            response.put("data", patient);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving patient by user ID: {}", userId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve patient: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get all patients with pagination
     */
    @GetMapping
    @Operation(summary = "Get all patients", description = "Retrieve all patients with pagination and sorting")
    public ResponseEntity<Map<String, Object>> getAllPatients(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<PatientResponse> patients = patientService.getAllPatients(page, size, sortBy, sortDir);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Patients retrieved successfully");
            response.put("data", patients.getContent());
            response.put("totalElements", patients.getTotalElements());
            response.put("totalPages", patients.getTotalPages());
            response.put("currentPage", patients.getNumber());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving patients", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve patients: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get patients by doctor ID
     */
    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get patients by doctor ID", description = "Retrieve all patients for a specific doctor")
    public ResponseEntity<Map<String, Object>> getPatientsByDoctorId(
            @Parameter(description = "Doctor ID") @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<PatientResponse> patients = patientService.getPatientsByDoctorId(doctorId, page, size, sortBy, sortDir);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Doctor patients retrieved successfully");
            response.put("data", patients.getContent());
            response.put("totalElements", patients.getTotalElements());
            response.put("totalPages", patients.getTotalPages());
            response.put("currentPage", patients.getNumber());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving patients for doctor: {}", doctorId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve doctor patients: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Update patient
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update patient", description = "Update an existing patient's information")
    public ResponseEntity<Map<String, Object>> updatePatient(
            @Parameter(description = "Patient ID") @PathVariable Long id,
            @Valid @RequestBody UpdatePatientRequest request) {
        try {
            PatientResponse patient = patientService.updatePatient(id, request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Patient updated successfully");
            response.put("data", patient);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating patient: {}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to update patient: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Create patient profile
     */
    @PostMapping("/user/{userId}")
    @Operation(summary = "Create patient profile", description = "Create a patient profile for a user")
    public ResponseEntity<Map<String, Object>> createPatientProfile(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Valid @RequestBody UpdatePatientRequest request) {
        try {
            PatientResponse patient = patientService.createPatient(userId, request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Patient profile created successfully");
            response.put("data", patient);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating patient profile for user: {}", userId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to create patient profile: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get patient statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get patient statistics", description = "Get overall patient statistics")
    public ResponseEntity<Map<String, Object>> getPatientStatistics() {
        try {
            PatientStatisticsResponse statistics = patientService.getPatientStatistics();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Patient statistics retrieved successfully");
            response.put("data", statistics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving patient statistics", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve patient statistics: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get medical records for patient
     */
    @GetMapping("/{id}/medical-records")
    @Operation(summary = "Get medical records for patient", description = "Retrieve all medical records for a specific patient")
    public ResponseEntity<Map<String, Object>> getMedicalRecordsByPatientId(
            @Parameter(description = "Patient ID") @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<MedicalRecordResponse> records = medicalRecordService.getMedicalRecordsByPatientId(id, page, size, sortBy, sortDir);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Medical records retrieved successfully");
            response.put("data", records.getContent());
            response.put("totalElements", records.getTotalElements());
            response.put("totalPages", records.getTotalPages());
            response.put("currentPage", records.getNumber());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving medical records for patient: {}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve medical records: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Create medical record
     */
    @PostMapping("/{id}/medical-records")
    @Operation(summary = "Create medical record", description = "Create a new medical record for a patient")
    public ResponseEntity<Map<String, Object>> createMedicalRecord(
            @Parameter(description = "Patient ID") @PathVariable Long id,
            @Valid @RequestBody CreateMedicalRecordRequest request) {
        try {
            // Set patient ID from path variable
            request.setPatientId(id);
            MedicalRecordResponse record = medicalRecordService.createMedicalRecord(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Medical record created successfully");
            response.put("data", record);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating medical record for patient: {}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to create medical record: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get appointments for patient
     */
    @GetMapping("/{id}/appointments")
    @Operation(summary = "Get appointments for patient", description = "Retrieve all appointments for a specific patient")
    public ResponseEntity<Map<String, Object>> getAppointmentsByPatientId(
            @Parameter(description = "Patient ID") @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<AppointmentResponse> appointments = appointmentService.getAppointmentsByPatientId(id, page, size, sortBy, sortDir);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Appointments retrieved successfully");
            response.put("data", appointments.getContent());
            response.put("totalElements", appointments.getTotalElements());
            response.put("totalPages", appointments.getTotalPages());
            response.put("currentPage", appointments.getNumber());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving appointments for patient: {}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve appointments: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Schedule appointment
     */
    @PostMapping("/{id}/appointments")
    @Operation(summary = "Schedule appointment", description = "Schedule a new appointment for a patient")
    public ResponseEntity<Map<String, Object>> scheduleAppointment(
            @Parameter(description = "Patient ID") @PathVariable Long id,
            @Valid @RequestBody ScheduleAppointmentRequest request) {
        try {
            // Set patient ID from path variable
            request.setPatientId(id);
            AppointmentResponse appointment = appointmentService.scheduleAppointment(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Appointment scheduled successfully");
            response.put("data", appointment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error scheduling appointment for patient: {}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to schedule appointment: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}