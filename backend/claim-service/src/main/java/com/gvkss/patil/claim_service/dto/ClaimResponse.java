package com.gvkss.patil.claim_service.dto;

import com.gvkss.patil.claim_service.entity.InsuranceClaim;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Claim Response DTO
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimResponse {
    
    private Long id;
    private String claimNumber;
    private Long patientId;
    private Long doctorId;
    private Long providerId;
    private InsuranceClaim.ClaimType claimType;
    private InsuranceClaim.ClaimStatus claimStatus;
    private BigDecimal totalAmount;
    private BigDecimal approvedAmount;
    private BigDecimal deductibleAmount;
    private BigDecimal copayAmount;
    private BigDecimal coinsuranceAmount;
    private LocalDate treatmentDate;
    private String serviceDescription;
    private String diagnosisCode;
    private String procedureCode;
    private Boolean isEmergency;
    private InsuranceClaim.ClaimPriority priority;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime paidAt;
    private String rejectionReason;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for UI
    private String patientName;
    private String doctorName;
    private String providerName;
    private String statusDisplayName;
    private String typeDisplayName;
    private String priorityDisplayName;
}
