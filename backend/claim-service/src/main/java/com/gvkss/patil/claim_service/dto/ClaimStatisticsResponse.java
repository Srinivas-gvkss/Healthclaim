package com.gvkss.patil.claim_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Claim Statistics Response DTO
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimStatisticsResponse {
    
    private long totalClaims;
    private long pendingClaims;
    private long approvedClaims;
    private long rejectedClaims;
    private BigDecimal totalAmount;
    private BigDecimal approvedAmount;
}
