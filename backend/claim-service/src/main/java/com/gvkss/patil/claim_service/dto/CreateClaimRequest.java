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
 * Create Claim Request DTO
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClaimRequest {
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    private Long doctorId;
    
    private Long providerId;
    
    @NotNull(message = "Claim type is required")
    private InsuranceClaim.ClaimType claimType;
    
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Total amount must have at most 8 integer digits and 2 decimal places")
    private BigDecimal totalAmount;
    
    @DecimalMin(value = "0.00", message = "Deductible amount cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Deductible amount must have at most 8 integer digits and 2 decimal places")
    private BigDecimal deductibleAmount = BigDecimal.ZERO;
    
    @DecimalMin(value = "0.00", message = "Copay amount cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Copay amount must have at most 8 integer digits and 2 decimal places")
    private BigDecimal copayAmount = BigDecimal.ZERO;
    
    @DecimalMin(value = "0.00", message = "Coinsurance amount cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Coinsurance amount must have at most 8 integer digits and 2 decimal places")
    private BigDecimal coinsuranceAmount = BigDecimal.ZERO;
    
    @NotNull(message = "Treatment date is required")
    @PastOrPresent(message = "Treatment date cannot be in the future")
    private LocalDate treatmentDate;
    
    @NotBlank(message = "Service description is required")
    @Size(max = 2000, message = "Service description cannot exceed 2000 characters")
    private String serviceDescription;
    
    @Size(max = 20, message = "Diagnosis code cannot exceed 20 characters")
    private String diagnosisCode;
    
    @Size(max = 20, message = "Procedure code cannot exceed 20 characters")
    private String procedureCode;
    
    private Boolean isEmergency = false;
    
    private InsuranceClaim.ClaimPriority priority = InsuranceClaim.ClaimPriority.NORMAL;
    
    @Size(max = 2000, message = "Notes cannot exceed 2000 characters")
    private String notes;
}
