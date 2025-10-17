package com.gvkss.patil.claim_service.controller;

import com.gvkss.patil.claim_service.dto.*;
import com.gvkss.patil.claim_service.entity.InsuranceClaim;
import com.gvkss.patil.claim_service.service.ClaimService;
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
 * Claim Controller for healthcare insurance claims
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/claims")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Claims Management", description = "Healthcare insurance claims management APIs")
public class ClaimController {
    
    private final ClaimService claimService;
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the claim service is running")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "claim-service");
        response.put("message", "Healthcare Claim Service is running");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Create a new claim
     */
    @PostMapping
    @Operation(summary = "Create claim", description = "Create a new insurance claim")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Claim created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> createClaim(@Valid @RequestBody CreateClaimRequest request) {
        try {
            ClaimResponse claim = claimService.createClaim(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Claim created successfully");
            response.put("data", claim);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error creating claim", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to create claim: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get claim by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get claim by ID", description = "Retrieve a specific claim by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Claim retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Claim not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> getClaimById(
            @Parameter(description = "Claim ID") @PathVariable Long id) {
        try {
            ClaimResponse claim = claimService.getClaimById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Claim retrieved successfully");
            response.put("data", claim);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving claim: {}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve claim: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get claim by claim number
     */
    @GetMapping("/number/{claimNumber}")
    @Operation(summary = "Get claim by number", description = "Retrieve a specific claim by its claim number")
    public ResponseEntity<Map<String, Object>> getClaimByNumber(
            @Parameter(description = "Claim number") @PathVariable String claimNumber) {
        try {
            ClaimResponse claim = claimService.getClaimByNumber(claimNumber);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Claim retrieved successfully");
            response.put("data", claim);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving claim: {}", claimNumber, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve claim: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get all claims with pagination
     */
    @GetMapping
    @Operation(summary = "Get all claims", description = "Retrieve all claims with pagination and sorting")
    public ResponseEntity<Map<String, Object>> getAllClaims(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<ClaimResponse> claims = claimService.getAllClaims(page, size, sortBy, sortDir);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Claims retrieved successfully");
            response.put("data", claims.getContent());
            response.put("totalElements", claims.getTotalElements());
            response.put("totalPages", claims.getTotalPages());
            response.put("currentPage", claims.getNumber());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving claims", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve claims: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get claims by patient ID
     */
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get claims by patient ID", description = "Retrieve all claims for a specific patient")
    public ResponseEntity<Map<String, Object>> getClaimsByPatientId(
            @Parameter(description = "Patient ID") @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<ClaimResponse> claims = claimService.getClaimsByPatientId(patientId, page, size, sortBy, sortDir);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Patient claims retrieved successfully");
            response.put("data", claims.getContent());
            response.put("totalElements", claims.getTotalElements());
            response.put("totalPages", claims.getTotalPages());
            response.put("currentPage", claims.getNumber());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving claims for patient: {}", patientId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve patient claims: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get claims by doctor ID
     */
    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get claims by doctor ID", description = "Retrieve all claims for a specific doctor")
    public ResponseEntity<Map<String, Object>> getClaimsByDoctorId(
            @Parameter(description = "Doctor ID") @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<ClaimResponse> claims = claimService.getClaimsByDoctorId(doctorId, page, size, sortBy, sortDir);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Doctor claims retrieved successfully");
            response.put("data", claims.getContent());
            response.put("totalElements", claims.getTotalElements());
            response.put("totalPages", claims.getTotalPages());
            response.put("currentPage", claims.getNumber());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving claims for doctor: {}", doctorId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve doctor claims: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get claims by status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get claims by status", description = "Retrieve all claims with a specific status")
    public ResponseEntity<Map<String, Object>> getClaimsByStatus(
            @Parameter(description = "Claim status") @PathVariable InsuranceClaim.ClaimStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Page<ClaimResponse> claims = claimService.getClaimsByStatus(status, page, size, sortBy, sortDir);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Claims retrieved successfully");
            response.put("data", claims.getContent());
            response.put("totalElements", claims.getTotalElements());
            response.put("totalPages", claims.getTotalPages());
            response.put("currentPage", claims.getNumber());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving claims with status: {}", status, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve claims: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Update claim
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update claim", description = "Update an existing claim")
    public ResponseEntity<Map<String, Object>> updateClaim(
            @Parameter(description = "Claim ID") @PathVariable Long id,
            @Valid @RequestBody UpdateClaimRequest request) {
        try {
            ClaimResponse claim = claimService.updateClaim(id, request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Claim updated successfully");
            response.put("data", claim);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating claim: {}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to update claim: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Update claim status
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "Update claim status", description = "Update the status of a claim")
    public ResponseEntity<Map<String, Object>> updateClaimStatus(
            @Parameter(description = "Claim ID") @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request) {
        try {
            ClaimResponse claim = claimService.updateClaimStatus(id, request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Claim status updated successfully");
            response.put("data", claim);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating claim status: {}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to update claim status: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Delete claim
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete claim", description = "Delete a claim by ID")
    public ResponseEntity<Map<String, Object>> deleteClaim(
            @Parameter(description = "Claim ID") @PathVariable Long id) {
        try {
            claimService.deleteClaim(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Claim deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error deleting claim: {}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to delete claim: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get recent claims for patient
     */
    @GetMapping("/patient/{patientId}/recent")
    @Operation(summary = "Get recent claims for patient", description = "Get recent claims for a specific patient")
    public ResponseEntity<Map<String, Object>> getRecentClaimsByPatientId(
            @Parameter(description = "Patient ID") @PathVariable Long patientId,
            @RequestParam(defaultValue = "5") int limit) {
        try {
            List<ClaimResponse> claims = claimService.getRecentClaimsByPatientId(patientId, limit);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Recent claims retrieved successfully");
            response.put("data", claims);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving recent claims for patient: {}", patientId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve recent claims: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get pending claims for doctor
     */
    @GetMapping("/doctor/{doctorId}/pending")
    @Operation(summary = "Get pending claims for doctor", description = "Get pending claims for doctor verification")
    public ResponseEntity<Map<String, Object>> getPendingClaimsForDoctor(
            @Parameter(description = "Doctor ID") @PathVariable Long doctorId) {
        try {
            List<ClaimResponse> claims = claimService.getPendingClaimsForDoctor(doctorId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pending claims retrieved successfully");
            response.put("data", claims);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving pending claims for doctor: {}", doctorId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve pending claims: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get claims for insurance review
     */
    @GetMapping("/insurance/review")
    @Operation(summary = "Get claims for insurance review", description = "Get claims pending insurance provider review")
    public ResponseEntity<Map<String, Object>> getClaimsForInsuranceReview(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<ClaimResponse> claims = claimService.getClaimsForInsuranceReview(page, size);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
            response.put("message", "Claims for review retrieved successfully");
            response.put("data", claims.getContent());
            response.put("totalElements", claims.getTotalElements());
            response.put("totalPages", claims.getTotalPages());
            response.put("currentPage", claims.getNumber());
        return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving claims for insurance review", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve claims for review: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get claim statistics for patient
     */
    @GetMapping("/patient/{patientId}/statistics")
    @Operation(summary = "Get claim statistics for patient", description = "Get claim statistics for a specific patient")
    public ResponseEntity<Map<String, Object>> getClaimStatisticsForPatient(
            @Parameter(description = "Patient ID") @PathVariable Long patientId) {
        try {
            ClaimStatisticsResponse statistics = claimService.getClaimStatisticsForPatient(patientId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
            response.put("message", "Claim statistics retrieved successfully");
            response.put("data", statistics);
        return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving claim statistics for patient: {}", patientId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to retrieve claim statistics: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
