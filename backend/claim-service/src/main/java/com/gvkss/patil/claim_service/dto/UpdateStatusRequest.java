package com.gvkss.patil.claim_service.dto;

import com.gvkss.patil.claim_service.entity.InsuranceClaim;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Update Claim Status Request DTO
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequest {
    
    @NotNull(message = "Claim status is required")
    private InsuranceClaim.ClaimStatus claimStatus;
    
    private BigDecimal approvedAmount;
    
    @Size(max = 2000, message = "Rejection reason cannot exceed 2000 characters")
    private String rejectionReason;
    
    @Size(max = 2000, message = "Notes cannot exceed 2000 characters")
    private String notes;
}
