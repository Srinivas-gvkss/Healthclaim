package com.gvkss.patil.claim_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Insurance Claim Entity
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "insurance_claims")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceClaim {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "claim_number", unique = true, nullable = false, length = 50)
    private String claimNumber;
    
    @Column(name = "patient_id", nullable = false)
    private Long patientId;
    
    @Column(name = "doctor_id")
    private Long doctorId;
    
    @Column(name = "provider_id")
    private Long providerId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "claim_type", nullable = false, length = 50)
    private ClaimType claimType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "claim_status", nullable = false, length = 50)
    private ClaimStatus claimStatus = ClaimStatus.SUBMITTED;
    
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(name = "approved_amount", precision = 10, scale = 2)
    private BigDecimal approvedAmount;
    
    @Column(name = "deductible_amount", precision = 10, scale = 2)
    private BigDecimal deductibleAmount = BigDecimal.ZERO;
    
    @Column(name = "copay_amount", precision = 10, scale = 2)
    private BigDecimal copayAmount = BigDecimal.ZERO;
    
    @Column(name = "coinsurance_amount", precision = 10, scale = 2)
    private BigDecimal coinsuranceAmount = BigDecimal.ZERO;
    
    @Column(name = "treatment_date", nullable = false)
    private LocalDate treatmentDate;
    
    @Column(name = "service_description", nullable = false, columnDefinition = "TEXT")
    private String serviceDescription;
    
    @Column(name = "diagnosis_code", length = 20)
    private String diagnosisCode;
    
    @Column(name = "procedure_code", length = 20)
    private String procedureCode;
    
    @Column(name = "is_emergency", nullable = false)
    private Boolean isEmergency = false;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 20)
    private ClaimPriority priority = ClaimPriority.NORMAL;
    
    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;
    
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;
    
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    
    @Column(name = "paid_at")
    private LocalDateTime paidAt;
    
    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Enums
    public enum ClaimType {
        MEDICAL, DENTAL, VISION, PHARMACY, LABORATORY, IMAGING, EMERGENCY
    }
    
    public enum ClaimStatus {
        SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, PAID, CANCELLED
    }
    
    public enum ClaimPriority {
        LOW, NORMAL, HIGH, URGENT
    }
}
