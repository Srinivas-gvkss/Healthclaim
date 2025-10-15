package com.gvkss.patil.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for healthcare user registration requests
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthcareUserRequest {
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    private String password;
    
    @NotBlank(message = "Role is required")
    private String role;
    
    // Healthcare-specific fields
    private LocalDate dateOfBirth;
    
    @Size(max = 10, message = "Gender must not exceed 10 characters")
    private String gender;
    
    private String address;
    
    @Size(max = 100, message = "Emergency contact name must not exceed 100 characters")
    private String emergencyContactName;
    
    @Size(max = 20, message = "Emergency contact phone must not exceed 20 characters")
    private String emergencyContactPhone;
    
    // Doctor-specific fields
    @Size(max = 100, message = "Medical license number must not exceed 100 characters")
    private String medicalLicenseNumber;
    
    @Size(max = 100, message = "Specialty must not exceed 100 characters")
    private String specialty;
    
    // Patient-specific fields
    @Size(max = 100, message = "Insurance policy number must not exceed 100 characters")
    private String insurancePolicyNumber;
    
    @Size(max = 100, message = "Insurance provider must not exceed 100 characters")
    private String insuranceProvider;
    
    @Size(max = 5, message = "Blood type must not exceed 5 characters")
    private String bloodType;
    
    private String allergies;
    
    private String medicalConditions;
}
