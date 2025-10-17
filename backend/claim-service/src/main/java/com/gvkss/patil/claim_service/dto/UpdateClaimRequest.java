package com.gvkss.patil.claim_service.dto;

import com.gvkss.patil.claim_service.entity.InsuranceClaim;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Update Claim Request DTO
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClaimRequest {
    
    private Long doctorId;
    
    private Long providerId;
    
    private InsuranceClaim.ClaimType claimType;
    
    @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Total amount must have at most 8 integer digits and 2 decimal places")
    private BigDecimal totalAmount;
    
    @DecimalMin(value = "0.00", message = "Deductible amount cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Deductible amount must have at most 8 integer digits and 2 decimal places")
    private BigDecimal deductibleAmount;
    
    @DecimalMin(value = "0.00", message = "Copay amount cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Copay amount must have at most 8 integer digits and 2 decimal places")
    private BigDecimal copayAmount;
    
    @DecimalMin(value = "0.00", message = "Coinsurance amount cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Coinsurance amount must have at most 8 integer digits and 2 decimal places")
    private BigDecimal coinsuranceAmount;
    
    @PastOrPresent(message = "Treatment date cannot be in the future")
    private LocalDate treatmentDate;
    
    @Size(max = 2000, message = "Service description cannot exceed 2000 characters")
    private String serviceDescription;
    
    @Size(max = 20, message = "Diagnosis code cannot exceed 20 characters")
    private String diagnosisCode;
    
    @Size(max = 20, message = "Procedure code cannot exceed 20 characters")
    private String procedureCode;
    
    private Boolean isEmergency;
    
    private InsuranceClaim.ClaimPriority priority;
    
    @Size(max = 2000, message = "Notes cannot exceed 2000 characters")
    private String notes;
}
