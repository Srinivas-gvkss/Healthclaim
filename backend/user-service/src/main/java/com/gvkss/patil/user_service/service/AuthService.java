package com.gvkss.patil.user_service.service;

import com.gvkss.patil.user_service.dto.*;
import com.gvkss.patil.user_service.entity.Role;
import com.gvkss.patil.user_service.entity.User;
import com.gvkss.patil.user_service.entity.UserRole;
import com.gvkss.patil.user_service.enums.UserStatus;
import com.gvkss.patil.user_service.repository.DepartmentRepository;
import com.gvkss.patil.user_service.repository.RoleRepository;
import com.gvkss.patil.user_service.repository.UserRepository;
import com.gvkss.patil.user_service.repository.UserRoleRepository;
import com.gvkss.patil.user_service.security.JwtTokenProvider;
import com.gvkss.patil.user_service.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Authentication service for handling user authentication operations.
 * Provides methods for login, signup, token management.
 * Aligned with frontend requirements.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    
    /**
     * Authenticate user and generate JWT tokens
     * 
     * @param loginRequest The login request
     * @return Authentication response with tokens
     */
    public AuthResponse login(LoginRequest loginRequest) {
        log.info("Attempting to login user: {}", loginRequest.getEmail());
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Authenticate user using email
            long authStartTime = System.currentTimeMillis();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            long authEndTime = System.currentTimeMillis();
            log.debug("Authentication took {} ms", authEndTime - authStartTime);
            
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User user = userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Update last login
            long updateStartTime = System.currentTimeMillis();
            user.updateLastLogin();
            userRepository.save(user);
            long updateEndTime = System.currentTimeMillis();
            log.debug("User update took {} ms", updateEndTime - updateStartTime);
            
            // Generate tokens
            long tokenStartTime = System.currentTimeMillis();
            String accessToken = tokenProvider.generateAccessToken(userPrincipal);
            String refreshToken = tokenProvider.generateRefreshToken(userPrincipal);
            long tokenEndTime = System.currentTimeMillis();
            log.debug("Token generation took {} ms", tokenEndTime - tokenStartTime);
            
            long totalTime = System.currentTimeMillis() - startTime;
            log.info("User {} logged in successfully in {} ms", user.getEmail(), totalTime);
            
            return createAuthResponse(accessToken, refreshToken, user);
            
        } catch (Exception e) {
            long totalTime = System.currentTimeMillis() - startTime;
            log.error("Login failed for user: {} after {} ms", loginRequest.getEmail(), totalTime, e);
            throw new BadCredentialsException("Invalid email or password");
        }
    }
    
    /**
     * Register a new user
     * 
     * @param signupRequest The signup request
     * @return Authentication response with tokens
     */
    public AuthResponse signup(SignupRequest signupRequest) {
        log.info("Attempting to signup user: {}", signupRequest.getEmail());
        
        // Check if user already exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }
        
        if (signupRequest.getUsername() != null && 
            userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("Username is already in use");
        }
        
        // Get role from request (required field)
        String roleCode = signupRequest.getRole();
        if (roleCode == null || roleCode.isBlank()) {
            throw new IllegalArgumentException("Role is required");
        }
        
        // Validate role-specific fields
        validateRoleSpecificFields(roleCode, signupRequest);
        
        // Create new user
        User user = User.builder()
                .email(signupRequest.getEmail())
                .username(signupRequest.getUsername())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .phoneNumber(signupRequest.getPhoneNumber())
                .medicalLicenseNumber(signupRequest.getMedicalLicenseNumber())
                .specialty(signupRequest.getSpecialty())
                .insurancePolicyNumber(signupRequest.getInsurancePolicyNumber())
                .status(UserStatus.ACTIVE)
                .enabled(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .failedLoginAttempts(0)
                .build();
        
        // Set department if provided
        if (signupRequest.getDepartmentId() != null) {
            departmentRepository.findById(signupRequest.getDepartmentId())
                    .ifPresent(user::setDepartment);
        }
        
        // Save user
        user = userRepository.save(user);
        
        // Assign role to user
        assignRoleToUser(user, roleCode.toLowerCase(), true);
        
        // Generate tokens
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        String accessToken = tokenProvider.generateAccessToken(userPrincipal);
        String refreshToken = tokenProvider.generateRefreshToken(userPrincipal);
        
        log.info("User {} signed up successfully with role {}", user.getEmail(), roleCode);
        
        return createAuthResponse(accessToken, refreshToken, user);
    }
    
    /**
     * Validate role-specific fields
     * 
     * @param roleCode The role code
     * @param signupRequest The signup request
     */
    private void validateRoleSpecificFields(String roleCode, SignupRequest signupRequest) {
        String normalizedRole = roleCode.toLowerCase();
        
        switch (normalizedRole) {
            case "doctor":
                if (signupRequest.getMedicalLicenseNumber() == null || signupRequest.getMedicalLicenseNumber().isBlank()) {
                    throw new IllegalArgumentException("Medical license number is required for doctors");
                }
                if (signupRequest.getSpecialty() == null || signupRequest.getSpecialty().isBlank()) {
                    throw new IllegalArgumentException("Specialty is required for doctors");
                }
                break;
            case "patient":
                if (signupRequest.getInsurancePolicyNumber() == null || signupRequest.getInsurancePolicyNumber().isBlank()) {
                    throw new IllegalArgumentException("Insurance policy number is required for patients");
                }
                break;
            case "admin":
            case "insurance_provider":
                // No additional fields required
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + roleCode);
        }
    }
    
    /**
     * Refresh JWT access token
     * 
     * @param refreshToken The refresh token
     * @return New authentication response with tokens
     */
    public AuthResponse refreshToken(String refreshToken) {
        log.info("Attempting to refresh token");
        
        if (!tokenProvider.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        String email = tokenProvider.getEmailFromToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!user.canAccess()) {
            throw new RuntimeException("User account is not accessible");
        }
        
        // Generate new tokens
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        String newAccessToken = tokenProvider.generateAccessToken(userPrincipal);
        String newRefreshToken = tokenProvider.generateRefreshToken(userPrincipal);
        
        log.info("Token refreshed successfully for user: {}", user.getEmail());
        
        return createAuthResponse(newAccessToken, newRefreshToken, user);
    }
    
    /**
     * Logout user (invalidate tokens)
     * 
     * @param refreshToken The refresh token to invalidate
     */
    public void logout(String refreshToken) {
        log.info("User logout requested");
        
        // In a real implementation, you would:
        // 1. Add the token to a blacklist
        // 2. Store blacklisted tokens in Redis or database
        // 3. Check blacklist during token validation
        
        // For now, we'll just log the logout
        if (refreshToken != null && tokenProvider.validateRefreshToken(refreshToken)) {
            String email = tokenProvider.getEmailFromToken(refreshToken);
            log.info("User {} logged out successfully", email);
        }
    }
    
    /**
     * Get current authenticated user
     * 
     * @return Current user
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    /**
     * Check if user exists by email
     * 
     * @param email The email to check
     * @return true if user exists
     */
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * Check if any admin user exists in the system
     * 
     * @return true if admin user exists
     */
    public boolean adminUserExists() {
        List<User> adminUsers = userRepository.findByRoleCode("ADMIN");
        return !adminUsers.isEmpty();
    }
    
    
    /**
     * Assign role to user
     * 
     * @param user The user
     * @param roleCode The role code
     * @param isPrimary Whether this is the primary role
     */
    private void assignRoleToUser(User user, String roleCode, boolean isPrimary) {
        Role role = roleRepository.findByCode(roleCode)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleCode));
        
        UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .isActive(true)
                .isPrimary(isPrimary)
                .assignedAt(LocalDateTime.now())
                .build();
        
        userRoleRepository.save(userRole);
        user.addUserRole(userRole);
        
        log.info("Role {} assigned to user {}", roleCode, user.getEmail());
    }
    
    /**
     * Create authentication response
     * 
     * @param accessToken The access token
     * @param refreshToken The refresh token
     * @param user The user
     * @return Authentication response
     */
    private AuthResponse createAuthResponse(String accessToken, String refreshToken, User user) {
        List<String> roles = user.getActiveRoles().stream()
                .map(userRole -> userRole.getRoleCode())
                .toList();
        
        List<String> permissions = user.getActiveRoles().stream()
                .flatMap(userRole -> {
                    String roleCode = userRole.getRoleCode();
                    return switch (roleCode) {
                        case "ADMIN" -> List.of(
                                "PERMISSION_USER_MANAGEMENT", "PERMISSION_ROLE_MANAGEMENT",
                                "PERMISSION_DEPARTMENT_MANAGEMENT", "PERMISSION_SYSTEM_CONFIG",
                                "PERMISSION_ALL_REPORTS", "PERMISSION_VIEW_ALL_CLAIMS",
                                "PERMISSION_MANAGE_POLICIES"
                        ).stream();
                        case "INSURANCE_PROVIDER" -> List.of(
                                "PERMISSION_REVIEW_CLAIMS", "PERMISSION_APPROVE_CLAIMS",
                                "PERMISSION_REJECT_CLAIMS", "PERMISSION_ACCESS_REPORTS",
                                "PERMISSION_MANAGE_POLICIES"
                        ).stream();
                        case "DOCTOR" -> List.of(
                                "PERMISSION_VERIFY_CLAIMS", "PERMISSION_VIEW_PATIENT_CLAIMS",
                                "PERMISSION_UPLOAD_MEDICAL_RECORDS", "PERMISSION_PROVIDE_MEDICAL_OPINION"
                        ).stream();
                        case "PATIENT" -> List.of(
                                "PERMISSION_SUBMIT_CLAIMS", "PERMISSION_VIEW_OWN_CLAIMS",
                                "PERMISSION_UPLOAD_DOCUMENTS", "PERMISSION_VIEW_CLAIM_STATUS",
                                "PERMISSION_UPDATE_PROFILE"
                        ).stream();
                        default -> List.<String>of().stream();
                    };
                })
                .toList();
        
        AuthResponse.DepartmentInfo departmentInfo = null;
        if (user.getDepartment() != null) {
            departmentInfo = AuthResponse.DepartmentInfo.builder()
                    .id(user.getDepartment().getId())
                    .name(user.getDepartment().getName())
                    .code(user.getDepartment().getCode())
                    .departmentType(user.getDepartment().getDepartmentType().name())
                    .build();
        }
        
        AuthResponse.UserInfo userInfo = AuthResponse.UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus().name())
                .roles(roles)
                .permissions(permissions)
                .department(departmentInfo)
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .build();
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(tokenProvider.getJwtExpirationInMs())
                .refreshExpiresIn(tokenProvider.getRefreshTokenExpirationInMs())
                .user(userInfo)
                .build();
    }
}
