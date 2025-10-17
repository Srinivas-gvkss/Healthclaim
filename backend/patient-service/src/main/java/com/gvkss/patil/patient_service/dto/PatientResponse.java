package com.gvkss.patil.patient_service.dto;

import com.gvkss.patil.patient_service.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Patient Response DTO
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    
    private Long id;
    private Long userId;
    private String patientNumber;
    private LocalDate dateOfBirth;
    private Patient.Gender gender;
    private String bloodType;
    private Integer heightCm;
    private Double weightKg;
    private String allergies;
    private String medicalConditions;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactRelationship;
    private Long primaryDoctorId;
    private Patient.PatientStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for UI
    private String userName;
    private String userEmail;
    private String primaryDoctorName;
    private String genderDisplayName;
    private String statusDisplayName;
    private Integer age;
}
