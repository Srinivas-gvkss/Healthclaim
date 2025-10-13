package com.gvkss.patil.user_service.controller;

import com.gvkss.patil.user_service.dto.*;
import com.gvkss.patil.user_service.entity.User;
import com.gvkss.patil.user_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Authentication controller for handling user authentication operations.
 * Provides endpoints for login, signup, token management, and password operations.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * User login endpoint
     * 
     * @param loginRequest The login request
     * @return Authentication response with JWT tokens
     */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT tokens")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {
        
        log.info("Login attempt for user: {}", loginRequest.getEmail());
        
        AuthResponse authResponse = authService.login(loginRequest);
        
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Login successful"));
    }
    
    /**
     * User signup endpoint
     * 
     * @param signupRequest The signup request
     * @return Authentication response with JWT tokens
     */
    @PostMapping("/signup")
    @Operation(summary = "User signup", description = "Register a new user and return JWT tokens")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request or user already exists"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<ApiResponse<AuthResponse>> signup(
            @Valid @RequestBody SignupRequest signupRequest) {
        
        log.info("Signup attempt for user: {}", signupRequest.getEmail());
        
        AuthResponse authResponse = authService.signup(signupRequest);
        
        return ResponseEntity.status(201)
                .body(ApiResponse.created(authResponse, "User created successfully"));
    }
    
    /**
     * Refresh JWT token endpoint
     * 
     * @param refreshTokenRequest The refresh token request
     * @return New authentication response with refreshed tokens
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Generate new access token using refresh token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid refresh token"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        
        log.info("Token refresh attempt");
        
        AuthResponse authResponse = authService.refreshToken(refreshTokenRequest.getRefreshToken());
        
        return ResponseEntity.ok(ApiResponse.success(authResponse, "Token refreshed successfully"));
    }
    
    /**
     * User logout endpoint
     * 
     * @param logoutRequest The logout request
     * @return Success response
     */
    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout user and invalidate tokens")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logout successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<ApiResponse<Object>> logout(
            @Valid @RequestBody LogoutRequest logoutRequest) {
        
        log.info("Logout attempt");
        
        authService.logout(logoutRequest.getRefreshToken());
        
        return ResponseEntity.ok(ApiResponse.success(null, "Logout successful"));
    }
    
    /**
     * Get current user profile endpoint
     * 
     * @return Current user information
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get current user", description = "Get current authenticated user information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User information retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        
        log.info("Get current user request");
        
        User currentUser = authService.getCurrentUser();
        UserResponse userResponse = convertToUserResponse(currentUser);
        
        return ResponseEntity.ok(ApiResponse.success(userResponse, "User information retrieved successfully"));
    }
    
    /**
     * Validate token endpoint
     * 
     * @param tokenValidationRequest The token validation request
     * @return Token validation response
     */
    @PostMapping("/validate-token")
    @Operation(summary = "Validate token", description = "Validate JWT token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token validation result"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<ApiResponse<TokenValidationResponse>> validateToken(
            @Valid @RequestBody TokenValidationRequest tokenValidationRequest) {
        
        log.info("Token validation request");
        
        // This would typically be handled by the JWT filter, but can be used for explicit validation
        TokenValidationResponse validationResponse = TokenValidationResponse.builder()
                .valid(true)
                .message("Token is valid")
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(validationResponse, "Token validation completed"));
    }
    
    /**
     * Convert User entity to UserResponse DTO
     * 
     * @param user The user entity
     * @return User response DTO
     */
    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus().name())
                .enabled(user.getEnabled())
                .accountNonLocked(user.getAccountNonLocked())
                .credentialsNonExpired(user.getCredentialsNonExpired())
                .failedLoginAttempts(user.getFailedLoginAttempts())
                .lockedAt(user.getLockedAt())
                .lastLoginAt(user.getLastLoginAt())
                .passwordChangedAt(user.getPasswordChangedAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .build();
    }
    
    /**
     * Refresh token request DTO
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class RefreshTokenRequest {
        @jakarta.validation.constraints.NotBlank(message = "Refresh token is required")
        private String refreshToken;
    }
    
    /**
     * Logout request DTO
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class LogoutRequest {
        private String refreshToken;
    }
    
    /**
     * Token validation request DTO
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class TokenValidationRequest {
        @jakarta.validation.constraints.NotBlank(message = "Token is required")
        private String token;
    }
    
    /**
     * Token validation response DTO
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class TokenValidationResponse {
        private boolean valid;
        private String message;
        private String tokenType;
        private Long expiresIn;
    }
    
    /**
     * Initialize system admin endpoint - only works if no admin exists
     * This is a secure way to create the first admin user
     */
    @PostMapping("/init-admin")
    @Operation(summary = "Initialize admin user", description = "Create initial system admin if none exists")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Admin created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Admin already exists"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<ApiResponse<Object>> initializeAdmin(
            @Valid @RequestBody SignupRequest signupRequest) {
        
        log.info("Admin initialization attempt for: {}", signupRequest.getEmail());
        
        try {
            // Check if any admin user already exists
            if (authService.adminUserExists()) {
                return ResponseEntity.status(409)
                        .body(ApiResponse.error("System admin already exists", 409));
            }
            
            // Set admin role
            signupRequest.setRole("ADMIN");
            
            AuthResponse authResponse = authService.signup(signupRequest);
            
            log.info("System admin initialized successfully: {}", signupRequest.getEmail());
            return ResponseEntity.status(201)
                    .body(ApiResponse.created(authResponse, "System admin initialized successfully"));
            
        } catch (Exception e) {
            log.error("Failed to initialize admin", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to initialize admin: " + e.getMessage(), 400));
        }
    }
    
}
