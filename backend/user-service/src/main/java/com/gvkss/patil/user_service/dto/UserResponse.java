package com.gvkss.patil.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for user response.
 * Contains user information for API responses.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    
    /**
     * User ID
     */
    private Long id;
    
    /**
     * User's email
     */
    private String email;
    
    /**
     * Username
     */
    private String username;
    
    /**
     * User's first name
     */
    private String firstName;
    
    /**
     * User's last name
     */
    private String lastName;
    
    /**
     * User's full name
     */
    private String fullName;
    
    /**
     * User's phone number
     */
    private String phoneNumber;
    
    /**
     * User's status
     */
    private String status;
    
    /**
     * Whether the user is enabled
     */
    private Boolean enabled;
    
    /**
     * Whether the account is locked
     */
    private Boolean accountNonLocked;
    
    /**
     * Whether credentials are non-expired
     */
    private Boolean credentialsNonExpired;
    
    /**
     * Number of failed login attempts
     */
    private Integer failedLoginAttempts;
    
    /**
     * Timestamp when the account was locked
     */
    private LocalDateTime lockedAt;
    
    /**
     * Timestamp of last login
     */
    private LocalDateTime lastLoginAt;
    
    /**
     * Timestamp when password was last changed
     */
    private LocalDateTime passwordChangedAt;
    
    /**
     * User's roles
     */
    private List<RoleInfo> roles;
    
    /**
     * Department information
     */
    private DepartmentInfo department;
    
    /**
     * Account creation timestamp
     */
    private LocalDateTime createdAt;
    
    /**
     * Last update timestamp
     */
    private LocalDateTime updatedAt;
    
    /**
     * User who created this user
     */
    private Long createdBy;
    
    /**
     * User who last updated this user
     */
    private Long updatedBy;
    
    /**
     * Role information nested class
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleInfo {
        
        /**
         * Role ID
         */
        private Long id;
        
        /**
         * Role name
         */
        private String name;
        
        /**
         * Role code
         */
        private String code;
        
        /**
         * Role description
         */
        private String description;
        
        /**
         * Access level
         */
        private Integer accessLevel;
        
        /**
         * Whether this is the primary role
         */
        private Boolean isPrimary;
        
        /**
         * Whether the role assignment is active
         */
        private Boolean isActive;
        
        /**
         * When the role was assigned
         */
        private LocalDateTime assignedAt;
    }
    
    /**
     * Department information nested class
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentInfo {
        
        /**
         * Department ID
         */
        private Long id;
        
        /**
         * Department name
         */
        private String name;
        
        /**
         * Department code
         */
        private String code;
        
        /**
         * Department description
         */
        private String description;
        
        /**
         * Department type
         */
        private String departmentType;
        
        /**
         * Department head ID
         */
        private Long headId;
        
        /**
         * Whether the department is active
         */
        private Boolean isActive;
        
        /**
         * Department location
         */
        private String location;
    }
}
