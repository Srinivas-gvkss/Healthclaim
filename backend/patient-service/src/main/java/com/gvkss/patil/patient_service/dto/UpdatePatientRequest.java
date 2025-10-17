package com.gvkss.patil.patient_service.dto;

import com.gvkss.patil.patient_service.entity.Patient;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Update Patient Request DTO
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePatientRequest {
    
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    
    private Patient.Gender gender;
    
    @Size(max = 5, message = "Blood type cannot exceed 5 characters")
    private String bloodType;
    
    @Min(value = 50, message = "Height must be at least 50 cm")
    @Max(value = 250, message = "Height cannot exceed 250 cm")
    private Integer heightCm;
    
    @DecimalMin(value = "1.0", message = "Weight must be at least 1 kg")
    @DecimalMax(value = "500.0", message = "Weight cannot exceed 500 kg")
    private Double weightKg;
    
    @Size(max = 2000, message = "Allergies cannot exceed 2000 characters")
    private String allergies;
    
    @Size(max = 2000, message = "Medical conditions cannot exceed 2000 characters")
    private String medicalConditions;
    
    @Size(max = 100, message = "Emergency contact name cannot exceed 100 characters")
    private String emergencyContactName;
    
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
    private String emergencyContactPhone;
    
    @Size(max = 50, message = "Emergency contact relationship cannot exceed 50 characters")
    private String emergencyContactRelationship;
    
    private Long primaryDoctorId;
    
    private Patient.PatientStatus status;
}
