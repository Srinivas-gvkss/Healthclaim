package com.gvkss.patil.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for healthcare user response
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthcareUserResponse {
    
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String status;
    private Boolean enabled;
    private LocalDateTime lastLoginAt;
    private String departmentName;
    private List<String> roles;
    
    // Healthcare-specific fields
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String emergencyContactName;
    private String emergencyContactPhone;
    
    // Doctor-specific fields
    private String medicalLicenseNumber;
    private String specialty;
    
    // Patient-specific fields
    private String insurancePolicyNumber;
    private String insuranceProvider;
    private String bloodType;
    private String allergies;
    private String medicalConditions;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
