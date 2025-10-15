package com.gvkss.patil.user_service.entity;

import com.gvkss.patil.user_service.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User entity representing users in the Healthcare Insurance Claim System.
 * This entity stores user information, authentication details, and healthcare-specific data.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "users", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "email"),
           @UniqueConstraint(columnNames = "username")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    /**
     * Unique identifier for the user
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * User's email address (used for login)
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    
    /**
     * Username (alternative login identifier)
     */
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(name = "username", unique = true, length = 50)
    private String username;
    
    /**
     * Encrypted password
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    @Column(name = "password", nullable = false, length = 128)
    private String password;
    
    /**
     * User's first name
     */
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    
    /**
     * User's last name
     */
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    
    /**
     * User's phone number
     */
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    /**
     * Medical license number (for doctors)
     */
    @Size(max = 100, message = "Medical license number must not exceed 100 characters")
    @Column(name = "medical_license_number", length = 100)
    private String medicalLicenseNumber;
    
    /**
     * Medical specialty (for doctors)
     */
    @Size(max = 100, message = "Specialty must not exceed 100 characters")
    @Column(name = "specialty", length = 100)
    private String specialty;
    
    /**
     * Insurance policy number (for patients)
     */
    @Size(max = 100, message = "Insurance policy number must not exceed 100 characters")
    @Column(name = "insurance_policy_number", length = 100)
    private String insurancePolicyNumber;
    
    /**
     * Date of birth
     */
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    /**
     * Gender
     */
    @Size(max = 10, message = "Gender must not exceed 10 characters")
    @Column(name = "gender", length = 10)
    private String gender;
    
    /**
     * Address
     */
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;
    
    /**
     * Emergency contact name
     */
    @Size(max = 100, message = "Emergency contact name must not exceed 100 characters")
    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;
    
    /**
     * Emergency contact phone
     */
    @Size(max = 20, message = "Emergency contact phone must not exceed 20 characters")
    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;
    
    /**
     * Insurance provider
     */
    @Size(max = 100, message = "Insurance provider must not exceed 100 characters")
    @Column(name = "insurance_provider", length = 100)
    private String insuranceProvider;
    
    /**
     * Blood type
     */
    @Size(max = 5, message = "Blood type must not exceed 5 characters")
    @Column(name = "blood_type", length = 5)
    private String bloodType;
    
    /**
     * Allergies
     */
    @Column(name = "allergies", columnDefinition = "TEXT")
    private String allergies;
    
    /**
     * Medical conditions
     */
    @Column(name = "medical_conditions", columnDefinition = "TEXT")
    private String medicalConditions;
    
    /**
     * User's status
     */
    @NotNull(message = "User status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;
    
    /**
     * Whether the user account is enabled
     */
    @Builder.Default
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;
    
    /**
     * Whether the user account is locked
     */
    @Builder.Default
    @Column(name = "account_non_locked", nullable = false)
    private Boolean accountNonLocked = true;
    
    /**
     * Whether the user's credentials are non-expired
     */
    @Builder.Default
    @Column(name = "credentials_non_expired", nullable = false)
    private Boolean credentialsNonExpired = true;
    
    /**
     * Number of failed login attempts
     */
    @Builder.Default
    @Column(name = "failed_login_attempts", nullable = false)
    private Integer failedLoginAttempts = 0;
    
    /**
     * Timestamp when the account was locked
     */
    @Column(name = "locked_at")
    private LocalDateTime lockedAt;
    
    /**
     * Timestamp when the user last logged in
     */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    /**
     * Timestamp when the password was last changed
     */
    @Column(name = "password_changed_at")
    private LocalDateTime passwordChangedAt;
    
    /**
     * Department the user belongs to
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    
    /**
     * User's roles
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserRole> userRoles = new ArrayList<>();
    
    /**
     * Timestamp when the user was created
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the user was last updated
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * User who created this user (for audit purposes)
     */
    @Column(name = "created_by")
    private Long createdBy;
    
    /**
     * User who last updated this user (for audit purposes)
     */
    @Column(name = "updated_by")
    private Long updatedBy;
    
    /**
     * Get the user's full name
     * 
     * @return The user's full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    /**
     * Get the user's display name (first name + last name)
     * 
     * @return The user's display name
     */
    public String getDisplayName() {
        return getFullName();
    }
    
    /**
     * Check if the user is active
     * 
     * @return true if the user is active
     */
    public boolean isActive() {
        return status == UserStatus.ACTIVE && enabled && accountNonLocked;
    }
    
    /**
     * Check if the user can access the system
     * 
     * @return true if the user can access the system
     */
    public boolean canAccess() {
        return status.canAccess() && enabled && accountNonLocked && credentialsNonExpired;
    }
    
    /**
     * Activate the user account
     */
    public void activate() {
        this.status = UserStatus.ACTIVE;
        this.enabled = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.failedLoginAttempts = 0;
        this.lockedAt = null;
    }
    
    /**
     * Deactivate the user account
     */
    public void deactivate() {
        this.status = UserStatus.INACTIVE;
        this.enabled = false;
    }
    
    /**
     * Lock the user account
     */
    public void lock() {
        this.accountNonLocked = false;
        this.lockedAt = LocalDateTime.now();
    }
    
    /**
     * Unlock the user account
     */
    public void unlock() {
        this.accountNonLocked = true;
        this.lockedAt = null;
        this.failedLoginAttempts = 0;
    }
    
    /**
     * Increment failed login attempts
     */
    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts++;
    }
    
    /**
     * Reset failed login attempts
     */
    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
    }
    
    /**
     * Update last login timestamp
     */
    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
        this.resetFailedLoginAttempts();
    }
    
    /**
     * Update password changed timestamp
     */
    public void updatePasswordChanged() {
        this.passwordChangedAt = LocalDateTime.now();
        this.resetFailedLoginAttempts();
    }
    
    /**
     * Add a role to the user
     * 
     * @param userRole The user role mapping to add
     */
    public void addUserRole(UserRole userRole) {
        if (userRole != null && !userRoles.contains(userRole)) {
            userRoles.add(userRole);
            userRole.setUser(this);
        }
    }
    
    /**
     * Remove a role from the user
     * 
     * @param userRole The user role mapping to remove
     */
    public void removeUserRole(UserRole userRole) {
        if (userRole != null && userRoles.contains(userRole)) {
            userRoles.remove(userRole);
            userRole.setUser(null);
        }
    }
    
    /**
     * Get the user's primary role
     * 
     * @return The primary role, or null if no primary role is set
     */
    public UserRole getPrimaryRole() {
        return userRoles.stream()
                .filter(UserRole::isPrimary)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Get all active roles for the user
     * 
     * @return List of active user roles
     */
    public List<UserRole> getActiveRoles() {
        return userRoles.stream()
                .filter(UserRole::isActive)
                .toList();
    }
    
    /**
     * Check if the user has a specific role
     * 
     * @param roleCode The role code to check
     * @return true if the user has the role
     */
    public boolean hasRole(String roleCode) {
        return userRoles.stream()
                .anyMatch(userRole -> userRole.isActive() && 
                        roleCode.equals(userRole.getRoleCode()));
    }
    
    /**
     * Check if the user has any of the specified roles
     * 
     * @param roleCodes The role codes to check
     * @return true if the user has any of the roles
     */
    public boolean hasAnyRole(String... roleCodes) {
        for (String roleCode : roleCodes) {
            if (hasRole(roleCode)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get the user's department name
     * 
     * @return The department name, or null if no department is assigned
     */
    public String getDepartmentName() {
        return department != null ? department.getName() : null;
    }
    
    /**
     * Get the user's department code
     * 
     * @return The department code, or null if no department is assigned
     */
    public String getDepartmentCode() {
        return department != null ? department.getCode() : null;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status=" + status +
                ", enabled=" + enabled +
                ", accountNonLocked=" + accountNonLocked +
                '}';
    }
}
