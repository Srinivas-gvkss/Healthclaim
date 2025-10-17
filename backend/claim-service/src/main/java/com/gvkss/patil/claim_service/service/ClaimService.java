package com.gvkss.patil.claim_service.service;

import com.gvkss.patil.claim_service.dto.*;
import com.gvkss.patil.claim_service.entity.InsuranceClaim;
import com.gvkss.patil.claim_service.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Claim Service Implementation
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClaimService {
    
    private final ClaimRepository claimRepository;
    
    /**
     * Create a new claim
     */
    public ClaimResponse createClaim(CreateClaimRequest request) {
        log.info("Creating new claim for patient: {}", request.getPatientId());
        
        // Generate unique claim number
        String claimNumber = generateClaimNumber();
        
        // Create claim entity
        InsuranceClaim claim = InsuranceClaim.builder()
                .claimNumber(claimNumber)
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .providerId(request.getProviderId())
                .claimType(request.getClaimType())
                .claimStatus(InsuranceClaim.ClaimStatus.SUBMITTED)
                .totalAmount(request.getTotalAmount())
                .deductibleAmount(request.getDeductibleAmount() != null ? request.getDeductibleAmount() : BigDecimal.ZERO)
                .copayAmount(request.getCopayAmount() != null ? request.getCopayAmount() : BigDecimal.ZERO)
                .coinsuranceAmount(request.getCoinsuranceAmount() != null ? request.getCoinsuranceAmount() : BigDecimal.ZERO)
                .treatmentDate(request.getTreatmentDate())
                .serviceDescription(request.getServiceDescription())
                .diagnosisCode(request.getDiagnosisCode())
                .procedureCode(request.getProcedureCode())
                .isEmergency(request.getIsEmergency() != null ? request.getIsEmergency() : false)
                .priority(request.getPriority() != null ? request.getPriority() : InsuranceClaim.ClaimPriority.NORMAL)
                .submittedAt(LocalDateTime.now())
                .notes(request.getNotes())
                .build();
        
        InsuranceClaim savedClaim = claimRepository.save(claim);
        log.info("Claim created successfully with ID: {}", savedClaim.getId());
        
        return convertToResponse(savedClaim);
    }
    
    /**
     * Get claim by ID
     */
    @Transactional(readOnly = true)
    public ClaimResponse getClaimById(Long id) {
        log.info("Fetching claim by ID: {}", id);
        
        InsuranceClaim claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found with ID: " + id));
        
        return convertToResponse(claim);
    }
    
    /**
     * Get claim by claim number
     */
    @Transactional(readOnly = true)
    public ClaimResponse getClaimByNumber(String claimNumber) {
        log.info("Fetching claim by number: {}", claimNumber);
        
        InsuranceClaim claim = claimRepository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new RuntimeException("Claim not found with number: " + claimNumber));
        
        return convertToResponse(claim);
    }
    
    /**
     * Get all claims with pagination
     */
    @Transactional(readOnly = true)
    public Page<ClaimResponse> getAllClaims(int page, int size, String sortBy, String sortDir) {
        log.info("Fetching all claims - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<InsuranceClaim> claims = claimRepository.findAll(pageable);
        return claims.map(this::convertToResponse);
    }
    
    /**
     * Get claims by patient ID
     */
    @Transactional(readOnly = true)
    public Page<ClaimResponse> getClaimsByPatientId(Long patientId, int page, int size, String sortBy, String sortDir) {
        log.info("Fetching claims for patient: {}", patientId);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<InsuranceClaim> claims = claimRepository.findByPatientId(patientId, pageable);
        return claims.map(this::convertToResponse);
    }
    
    /**
     * Get claims by doctor ID
     */
    @Transactional(readOnly = true)
    public Page<ClaimResponse> getClaimsByDoctorId(Long doctorId, int page, int size, String sortBy, String sortDir) {
        log.info("Fetching claims for doctor: {}", doctorId);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<InsuranceClaim> claims = claimRepository.findByDoctorId(doctorId, pageable);
        return claims.map(this::convertToResponse);
    }
    
    /**
     * Get claims by status
     */
    @Transactional(readOnly = true)
    public Page<ClaimResponse> getClaimsByStatus(InsuranceClaim.ClaimStatus status, int page, int size, String sortBy, String sortDir) {
        log.info("Fetching claims with status: {}", status);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<InsuranceClaim> claims = claimRepository.findByClaimStatus(status, pageable);
        return claims.map(this::convertToResponse);
    }
    
    /**
     * Update claim
     */
    public ClaimResponse updateClaim(Long id, UpdateClaimRequest request) {
        log.info("Updating claim: {}", id);
        
        InsuranceClaim existingClaim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found with ID: " + id));
        
        // Update fields if provided
        if (request.getDoctorId() != null) {
            existingClaim.setDoctorId(request.getDoctorId());
        }
        if (request.getProviderId() != null) {
            existingClaim.setProviderId(request.getProviderId());
        }
        if (request.getClaimType() != null) {
            existingClaim.setClaimType(request.getClaimType());
        }
        if (request.getTotalAmount() != null) {
            existingClaim.setTotalAmount(request.getTotalAmount());
        }
        if (request.getDeductibleAmount() != null) {
            existingClaim.setDeductibleAmount(request.getDeductibleAmount());
        }
        if (request.getCopayAmount() != null) {
            existingClaim.setCopayAmount(request.getCopayAmount());
        }
        if (request.getCoinsuranceAmount() != null) {
            existingClaim.setCoinsuranceAmount(request.getCoinsuranceAmount());
        }
        if (request.getTreatmentDate() != null) {
            existingClaim.setTreatmentDate(request.getTreatmentDate());
        }
        if (request.getServiceDescription() != null) {
            existingClaim.setServiceDescription(request.getServiceDescription());
        }
        if (request.getDiagnosisCode() != null) {
            existingClaim.setDiagnosisCode(request.getDiagnosisCode());
        }
        if (request.getProcedureCode() != null) {
            existingClaim.setProcedureCode(request.getProcedureCode());
        }
        if (request.getIsEmergency() != null) {
            existingClaim.setIsEmergency(request.getIsEmergency());
        }
        if (request.getPriority() != null) {
            existingClaim.setPriority(request.getPriority());
        }
        if (request.getNotes() != null) {
            existingClaim.setNotes(request.getNotes());
        }
        
        InsuranceClaim updatedClaim = claimRepository.save(existingClaim);
        log.info("Claim updated successfully: {}", updatedClaim.getId());
        
        return convertToResponse(updatedClaim);
    }
    
    /**
     * Update claim status
     */
    public ClaimResponse updateClaimStatus(Long id, UpdateStatusRequest request) {
        log.info("Updating claim status: {} to {}", id, request.getClaimStatus());
        
        InsuranceClaim existingClaim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found with ID: " + id));
        
        existingClaim.setClaimStatus(request.getClaimStatus());
        
        // Set timestamps based on status
        LocalDateTime now = LocalDateTime.now();
        switch (request.getClaimStatus()) {
            case UNDER_REVIEW:
                existingClaim.setReviewedAt(now);
                break;
            case APPROVED:
                existingClaim.setApprovedAt(now);
                if (request.getApprovedAmount() != null) {
                    existingClaim.setApprovedAmount(request.getApprovedAmount());
                }
                break;
            case REJECTED:
                existingClaim.setRejectionReason(request.getRejectionReason());
                break;
            case PAID:
                existingClaim.setPaidAt(now);
                break;
        }
        
        if (request.getNotes() != null) {
            existingClaim.setNotes(request.getNotes());
        }
        
        InsuranceClaim updatedClaim = claimRepository.save(existingClaim);
        log.info("Claim status updated successfully: {}", updatedClaim.getId());
        
        return convertToResponse(updatedClaim);
    }
    
    /**
     * Delete claim
     */
    public void deleteClaim(Long id) {
        log.info("Deleting claim: {}", id);
        
        if (!claimRepository.existsById(id)) {
            throw new RuntimeException("Claim not found with ID: " + id);
        }
        
        claimRepository.deleteById(id);
        log.info("Claim deleted successfully: {}", id);
    }
    
    /**
     * Get recent claims for patient
     */
    @Transactional(readOnly = true)
    public List<ClaimResponse> getRecentClaimsByPatientId(Long patientId, int limit) {
        log.info("Fetching recent claims for patient: {}", patientId);
        
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        List<InsuranceClaim> claims = claimRepository.findRecentClaimsByPatientId(patientId, pageable);
        
        return claims.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get pending claims for doctor
     */
    @Transactional(readOnly = true)
    public List<ClaimResponse> getPendingClaimsForDoctor(Long doctorId) {
        log.info("Fetching pending claims for doctor: {}", doctorId);
        
        List<InsuranceClaim> claims = claimRepository.findPendingClaimsForDoctor(doctorId);
        
        return claims.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get claims for insurance review
     */
    @Transactional(readOnly = true)
    public Page<ClaimResponse> getClaimsForInsuranceReview(int page, int size) {
        log.info("Fetching claims for insurance review");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("submittedAt").ascending());
        Page<InsuranceClaim> claims = claimRepository.findClaimsForInsuranceReview(pageable);
        
        return claims.map(this::convertToResponse);
    }
    
    /**
     * Get claim statistics for patient
     */
    @Transactional(readOnly = true)
    public ClaimStatisticsResponse getClaimStatisticsForPatient(Long patientId) {
        log.info("Fetching claim statistics for patient: {}", patientId);
        
        long totalClaims = claimRepository.countByPatientId(patientId);
        long pendingClaims = claimRepository.countByClaimStatus(InsuranceClaim.ClaimStatus.SUBMITTED);
        long approvedClaims = claimRepository.countByClaimStatus(InsuranceClaim.ClaimStatus.APPROVED);
        long rejectedClaims = claimRepository.countByClaimStatus(InsuranceClaim.ClaimStatus.REJECTED);
        
        Double totalAmount = claimRepository.getTotalAmountByPatientId(patientId);
        Double approvedAmount = claimRepository.getTotalApprovedAmountByPatientId(patientId);
        
        return ClaimStatisticsResponse.builder()
                .totalClaims(totalClaims)
                .pendingClaims(pendingClaims)
                .approvedClaims(approvedClaims)
                .rejectedClaims(rejectedClaims)
                .totalAmount(BigDecimal.valueOf(totalAmount != null ? totalAmount : 0.0))
                .approvedAmount(BigDecimal.valueOf(approvedAmount != null ? approvedAmount : 0.0))
                .build();
    }
    
    /**
     * Generate unique claim number
     */
    private String generateClaimNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "CLM-" + timestamp + "-" + randomSuffix;
    }
    
    /**
     * Convert entity to response DTO
     */
    private ClaimResponse convertToResponse(InsuranceClaim claim) {
        return ClaimResponse.builder()
                .id(claim.getId())
                .claimNumber(claim.getClaimNumber())
                .patientId(claim.getPatientId())
                .doctorId(claim.getDoctorId())
                .providerId(claim.getProviderId())
                .claimType(claim.getClaimType())
                .claimStatus(claim.getClaimStatus())
                .totalAmount(claim.getTotalAmount())
                .approvedAmount(claim.getApprovedAmount())
                .deductibleAmount(claim.getDeductibleAmount())
                .copayAmount(claim.getCopayAmount())
                .coinsuranceAmount(claim.getCoinsuranceAmount())
                .treatmentDate(claim.getTreatmentDate())
                .serviceDescription(claim.getServiceDescription())
                .diagnosisCode(claim.getDiagnosisCode())
                .procedureCode(claim.getProcedureCode())
                .isEmergency(claim.getIsEmergency())
                .priority(claim.getPriority())
                .submittedAt(claim.getSubmittedAt())
                .reviewedAt(claim.getReviewedAt())
                .approvedAt(claim.getApprovedAt())
                .paidAt(claim.getPaidAt())
                .rejectionReason(claim.getRejectionReason())
                .notes(claim.getNotes())
                .createdAt(claim.getCreatedAt())
                .updatedAt(claim.getUpdatedAt())
                .statusDisplayName(getStatusDisplayName(claim.getClaimStatus()))
                .typeDisplayName(getTypeDisplayName(claim.getClaimType()))
                .priorityDisplayName(getPriorityDisplayName(claim.getPriority()))
                .build();
    }
    
    private String getStatusDisplayName(InsuranceClaim.ClaimStatus status) {
        switch (status) {
            case SUBMITTED: return "Submitted";
            case UNDER_REVIEW: return "Under Review";
            case APPROVED: return "Approved";
            case REJECTED: return "Rejected";
            case PAID: return "Paid";
            case CANCELLED: return "Cancelled";
            default: return status.toString();
        }
    }
    
    private String getTypeDisplayName(InsuranceClaim.ClaimType type) {
        switch (type) {
            case MEDICAL: return "Medical";
            case DENTAL: return "Dental";
            case VISION: return "Vision";
            case PHARMACY: return "Pharmacy";
            case LABORATORY: return "Laboratory";
            case IMAGING: return "Imaging";
            case EMERGENCY: return "Emergency";
            default: return type.toString();
        }
    }
    
    private String getPriorityDisplayName(InsuranceClaim.ClaimPriority priority) {
        switch (priority) {
            case LOW: return "Low";
            case NORMAL: return "Normal";
            case HIGH: return "High";
            case URGENT: return "Urgent";
            default: return priority.toString();
        }
    }
}
