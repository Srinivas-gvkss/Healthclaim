package com.gvkss.patil.user_service.entity;

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
 * Insurance Claim entity representing healthcare insurance claims
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
    
    @Column(name = "claim_number", nullable = false, unique = true, length = 50)
    private String claimNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private User doctor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private HealthcareProvider provider;
    
    @Column(name = "claim_type", nullable = false, length = 50)
    private String claimType; // MEDICAL, DENTAL, VISION, PHARMACY
    
    @Column(name = "claim_status", nullable = false, length = 50)
    @Builder.Default
    private String claimStatus = "SUBMITTED"; // SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, PAID
    
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(name = "approved_amount", precision = 10, scale = 2)
    private BigDecimal approvedAmount;
    
    @Column(name = "deductible_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal deductibleAmount = BigDecimal.ZERO;
    
    @Column(name = "copay_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal copayAmount = BigDecimal.ZERO;
    
    @Column(name = "coinsurance_amount", precision = 10, scale = 2)
    @Builder.Default
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
    @Builder.Default
    private Boolean isEmergency = false;
    
    @Column(name = "priority", nullable = false, length = 20)
    @Builder.Default
    private String priority = "NORMAL"; // LOW, NORMAL, HIGH, URGENT
    
    @Column(name = "submitted_at", nullable = false)
    @Builder.Default
    private LocalDateTime submittedAt = LocalDateTime.now();
    
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
}
