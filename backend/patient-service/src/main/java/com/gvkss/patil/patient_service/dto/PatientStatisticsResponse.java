package com.gvkss.patil.patient_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Patient Statistics Response DTO
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientStatisticsResponse {
    
    private long totalPatients;
    private long activePatients;
    private long inactivePatients;
    private long patientsWithAllergies;
    private long patientsWithMedicalConditions;
}
