package com.gvkss.patil.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for authentication response.
 * Contains JWT tokens and user information after successful authentication.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    /**
     * JWT access token
     */
    private String accessToken;
    
    /**
     * JWT refresh token
     */
    private String refreshToken;
    
    /**
     * Token type (Bearer)
     */
    @Builder.Default
    private String tokenType = "Bearer";
    
    /**
     * Access token expiration time in milliseconds
     */
    private Long expiresIn;
    
    /**
     * Refresh token expiration time in milliseconds
     */
    private Long refreshExpiresIn;
    
    /**
     * User information
     */
    private UserInfo user;
    
    /**
     * User information nested class
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        
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
         * User's roles
         */
        private List<String> roles;
        
        /**
         * User's permissions
         */
        private List<String> permissions;
        
        /**
         * Department information
         */
        private DepartmentInfo department;
        
        /**
         * Last login timestamp
         */
        private LocalDateTime lastLoginAt;
        
        /**
         * Account creation timestamp
         */
        private LocalDateTime createdAt;
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
         * Department type
         */
        private String departmentType;
    }
}
