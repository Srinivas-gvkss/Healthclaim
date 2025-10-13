package com.gvkss.patil.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user signup request.
 * Contains all required information for user registration.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    
    /**
     * User's email address
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    /**
     * Username (optional, can be null)
     */
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    /**
     * User's password
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    private String password;
    
    /**
     * User's first name
     */
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    /**
     * User's last name
     */
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    /**
     * User's phone number (optional)
     */
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;
    
    /**
     * Department ID (optional)
     */
    private Long departmentId;
    
    /**
     * Role code (patient, doctor, admin, insurance_provider)
     */
    @NotBlank(message = "Role is required")
    @Size(max = 50, message = "Role code must not exceed 50 characters")
    private String role;
    
    /**
     * Medical license number (required for doctors)
     */
    @Size(max = 100, message = "Medical license number must not exceed 100 characters")
    private String medicalLicenseNumber;
    
    /**
     * Medical specialty (required for doctors)
     */
    @Size(max = 100, message = "Specialty must not exceed 100 characters")
    private String specialty;
    
    /**
     * Insurance policy number (required for patients)
     */
    @Size(max = 100, message = "Insurance policy number must not exceed 100 characters")
    private String insurancePolicyNumber;
}
