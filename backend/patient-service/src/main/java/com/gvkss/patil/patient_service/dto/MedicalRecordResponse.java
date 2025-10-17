package com.gvkss.patil.patient_service.dto;

import com.gvkss.patil.patient_service.entity.MedicalRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Medical Record Response DTO
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponse {
    
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String recordType;
    private LocalDate visitDate;
    private String diagnosis;
    private String diagnosisCode;
    private String treatment;
    private String prescription;
    private String notes;
    private String vitalSigns;
    private String labResults;
    private String imagingResults;
    private MedicalRecord.RecordStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for UI
    private String patientName;
    private String doctorName;
    private String statusDisplayName;
}
