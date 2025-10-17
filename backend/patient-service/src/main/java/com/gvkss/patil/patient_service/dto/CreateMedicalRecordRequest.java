package com.gvkss.patil.patient_service.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Create Medical Record Request DTO
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMedicalRecordRequest {
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    
    @NotBlank(message = "Record type is required")
    @Size(max = 50, message = "Record type cannot exceed 50 characters")
    private String recordType;
    
    @NotNull(message = "Visit date is required")
    @PastOrPresent(message = "Visit date cannot be in the future")
    private LocalDate visitDate;
    
    @Size(max = 2000, message = "Diagnosis cannot exceed 2000 characters")
    private String diagnosis;
    
    @Size(max = 20, message = "Diagnosis code cannot exceed 20 characters")
    private String diagnosisCode;
    
    @Size(max = 2000, message = "Treatment cannot exceed 2000 characters")
    private String treatment;
    
    @Size(max = 2000, message = "Prescription cannot exceed 2000 characters")
    private String prescription;
    
    @Size(max = 2000, message = "Notes cannot exceed 2000 characters")
    private String notes;
    
    @Size(max = 1000, message = "Vital signs cannot exceed 1000 characters")
    private String vitalSigns;
    
    @Size(max = 2000, message = "Lab results cannot exceed 2000 characters")
    private String labResults;
    
    @Size(max = 2000, message = "Imaging results cannot exceed 2000 characters")
    private String imagingResults;
}
