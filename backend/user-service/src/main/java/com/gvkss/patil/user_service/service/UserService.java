package com.gvkss.patil.user_service.service;

import com.gvkss.patil.user_service.dto.UserResponse;
import com.gvkss.patil.user_service.entity.Department;
import com.gvkss.patil.user_service.entity.Role;
import com.gvkss.patil.user_service.entity.User;
import com.gvkss.patil.user_service.entity.UserRole;
import com.gvkss.patil.user_service.enums.UserStatus;
import com.gvkss.patil.user_service.repository.DepartmentRepository;
import com.gvkss.patil.user_service.repository.RoleRepository;
import com.gvkss.patil.user_service.repository.UserRepository;
import com.gvkss.patil.user_service.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * User service for managing user operations.
 * Provides methods for user CRUD operations, role management, and user queries.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Get all users with pagination
     * 
     * @param pageable Pagination information
     * @return Page of users
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.info("Fetching all users with pagination: {}", pageable);
        
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::convertToUserResponse);
    }
    
    /**
     * Get user by ID
     * 
     * @param userId The user ID
     * @return User response
     */
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        log.info("Fetching user by ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        return convertToUserResponse(user);
    }
    
    /**
     * Get user by email
     * 
     * @param email The email address
     * @return User response
     */
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        
        return convertToUserResponse(user);
    }
    
    /**
     * Get users by status
     * 
     * @param status The user status
     * @param pageable Pagination information
     * @return Page of users
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> getUsersByStatus(UserStatus status, Pageable pageable) {
        log.info("Fetching users by status: {}", status);
        
        Page<User> users = userRepository.findByStatus(status, pageable);
        return users.map(this::convertToUserResponse);
    }
    
    /**
     * Get users by department
     * 
     * @param departmentId The department ID
     * @param pageable Pagination information
     * @return Page of users
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> getUsersByDepartment(Long departmentId, Pageable pageable) {
        log.info("Fetching users by department ID: {}", departmentId);
        
        Page<User> users = userRepository.findByDepartmentId(departmentId, pageable);
        return users.map(this::convertToUserResponse);
    }
    
    /**
     * Get users by role
     * 
     * @param roleCode The role code
     * @param pageable Pagination information
     * @return Page of users
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> getUsersByRole(String roleCode, Pageable pageable) {
        log.info("Fetching users by role: {}", roleCode);
        
        Page<User> users = userRepository.findByRoleCode(roleCode, pageable);
        return users.map(this::convertToUserResponse);
    }
    
    /**
     * Search users by name
     * 
     * @param searchTerm The search term
     * @param pageable Pagination information
     * @return Page of users
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> searchUsersByName(String searchTerm, Pageable pageable) {
        log.info("Searching users by name: {}", searchTerm);
        
        Page<User> users = userRepository.findByNameContaining(searchTerm, pageable);
        return users.map(this::convertToUserResponse);
    }
    
    /**
     * Get active users
     * 
     * @param pageable Pagination information
     * @return Page of active users
     */
    @Transactional(readOnly = true)
    public Page<UserResponse> getActiveUsers(Pageable pageable) {
        log.info("Fetching active users");
        
        Page<User> users = userRepository.findActiveUsers(pageable);
        return users.map(this::convertToUserResponse);
    }
    
    /**
     * Create a new user
     * 
     * @param user The user to create
     * @return Created user response
     */
    public UserResponse createUser(User user) {
        log.info("Creating new user: {}", user.getEmail());
        
        // Validate email uniqueness
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already in use: " + user.getEmail());
        }
        
        // Validate username uniqueness if provided
        if (user.getUsername() != null && userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username is already in use: " + user.getUsername());
        }
        
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set default values
        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }
        if (user.getEnabled() == null) {
            user.setEnabled(true);
        }
        if (user.getAccountNonLocked() == null) {
            user.setAccountNonLocked(true);
        }
        if (user.getCredentialsNonExpired() == null) {
            user.setCredentialsNonExpired(true);
        }
        if (user.getFailedLoginAttempts() == null) {
            user.setFailedLoginAttempts(0);
        }
        
        User savedUser = userRepository.save(user);
        log.info("User created successfully: {}", savedUser.getEmail());
        
        return convertToUserResponse(savedUser);
    }
    
    /**
     * Update user information
     * 
     * @param userId The user ID
     * @param updatedUser The updated user information
     * @return Updated user response
     */
    public UserResponse updateUser(Long userId, User updatedUser) {
        log.info("Updating user: {}", userId);
        
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        // Update fields
        if (updatedUser.getFirstName() != null) {
            existingUser.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            existingUser.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        }
        if (updatedUser.getStatus() != null) {
            existingUser.setStatus(updatedUser.getStatus());
        }
        if (updatedUser.getEnabled() != null) {
            existingUser.setEnabled(updatedUser.getEnabled());
        }
        if (updatedUser.getAccountNonLocked() != null) {
            existingUser.setAccountNonLocked(updatedUser.getAccountNonLocked());
        }
        if (updatedUser.getCredentialsNonExpired() != null) {
            existingUser.setCredentialsNonExpired(updatedUser.getCredentialsNonExpired());
        }
        
        User savedUser = userRepository.save(existingUser);
        log.info("User updated successfully: {}", savedUser.getEmail());
        
        return convertToUserResponse(savedUser);
    }
    
    /**
     * Delete user (soft delete)
     * 
     * @param userId The user ID
     */
    public void deleteUser(Long userId) {
        log.info("Deleting user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        // Soft delete by setting status to DELETED
        user.setStatus(UserStatus.DELETED);
        user.setEnabled(false);
        userRepository.save(user);
        
        log.info("User deleted successfully: {}", user.getEmail());
    }
    
    /**
     * Activate user account
     * 
     * @param userId The user ID
     * @return Updated user response
     */
    public UserResponse activateUser(Long userId) {
        log.info("Activating user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        user.activate();
        User savedUser = userRepository.save(user);
        
        log.info("User activated successfully: {}", savedUser.getEmail());
        return convertToUserResponse(savedUser);
    }
    
    /**
     * Deactivate user account
     * 
     * @param userId The user ID
     * @return Updated user response
     */
    public UserResponse deactivateUser(Long userId) {
        log.info("Deactivating user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        user.deactivate();
        User savedUser = userRepository.save(user);
        
        log.info("User deactivated successfully: {}", savedUser.getEmail());
        return convertToUserResponse(savedUser);
    }
    
    /**
     * Lock user account
     * 
     * @param userId The user ID
     * @return Updated user response
     */
    public UserResponse lockUser(Long userId) {
        log.info("Locking user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        user.lock();
        User savedUser = userRepository.save(user);
        
        log.info("User locked successfully: {}", savedUser.getEmail());
        return convertToUserResponse(savedUser);
    }
    
    /**
     * Unlock user account
     * 
     * @param userId The user ID
     * @return Updated user response
     */
    public UserResponse unlockUser(Long userId) {
        log.info("Unlocking user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        user.unlock();
        User savedUser = userRepository.save(user);
        
        log.info("User unlocked successfully: {}", savedUser.getEmail());
        return convertToUserResponse(savedUser);
    }
    
    /**
     * Assign role to user
     * 
     * @param userId The user ID
     * @param roleCode The role code
     * @param isPrimary Whether this is the primary role
     * @return Updated user response
     */
    public UserResponse assignRoleToUser(Long userId, String roleCode, boolean isPrimary) {
        log.info("Assigning role {} to user: {}", roleCode, userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        Role role = roleRepository.findByCode(roleCode)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleCode));
        
        // Check if user already has this role
        if (userRoleRepository.existsByUserIdAndRoleIdAndIsActiveTrue(userId, role.getId())) {
            throw new RuntimeException("User already has this role: " + roleCode);
        }
        
        // If this is a primary role, remove primary status from other roles
        if (isPrimary) {
            List<UserRole> existingRoles = userRoleRepository.findByUserIdAndIsActiveTrue(userId);
            existingRoles.forEach(userRole -> {
                if (userRole.isPrimary()) {
                    userRole.removePrimary();
                    userRoleRepository.save(userRole);
                }
            });
        }
        
        UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .isActive(true)
                .isPrimary(isPrimary)
                .assignedAt(LocalDateTime.now())
                .build();
        
        userRoleRepository.save(userRole);
        user.addUserRole(userRole);
        
        log.info("Role {} assigned to user {} successfully", roleCode, user.getEmail());
        return convertToUserResponse(user);
    }
    
    /**
     * Remove role from user
     * 
     * @param userId The user ID
     * @param roleCode The role code
     * @return Updated user response
     */
    public UserResponse removeRoleFromUser(Long userId, String roleCode) {
        log.info("Removing role {} from user: {}", roleCode, userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        Role role = roleRepository.findByCode(roleCode)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleCode));
        
        UserRole userRole = userRoleRepository.findByUserIdAndRoleId(userId, role.getId())
                .orElseThrow(() -> new RuntimeException("User does not have this role: " + roleCode));
        
        userRole.deactivate();
        userRoleRepository.save(userRole);
        
        log.info("Role {} removed from user {} successfully", roleCode, user.getEmail());
        return convertToUserResponse(user);
    }
    
    /**
     * Assign department to user
     * 
     * @param userId The user ID
     * @param departmentId The department ID
     * @return Updated user response
     */
    public UserResponse assignDepartmentToUser(Long userId, Long departmentId) {
        log.info("Assigning department {} to user: {}", departmentId, userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + departmentId));
        
        user.setDepartment(department);
        User savedUser = userRepository.save(user);
        
        log.info("Department {} assigned to user {} successfully", department.getName(), user.getEmail());
        return convertToUserResponse(savedUser);
    }
    
    /**
     * Get user statistics
     * 
     * @return User statistics
     */
    @Transactional(readOnly = true)
    public UserStatistics getUserStatistics() {
        log.info("Fetching user statistics");
        
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countActiveUsers();
        long lockedUsers = userRepository.findLockedUsers().size();
        
        return UserStatistics.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .lockedUsers(lockedUsers)
                .inactiveUsers(totalUsers - activeUsers)
                .build();
    }
    
    /**
     * Convert User entity to UserResponse DTO
     * 
     * @param user The user entity
     * @return User response DTO
     */
    public UserResponse convertToUserResponse(User user) {
        List<UserResponse.RoleInfo> roleInfos = user.getActiveRoles().stream()
                .map(userRole -> UserResponse.RoleInfo.builder()
                        .id(userRole.getRole().getId())
                        .name(userRole.getRole().getName())
                        .code(userRole.getRole().getCode())
                        .description(userRole.getRole().getDescription())
                        .accessLevel(userRole.getRole().getAccessLevel())
                        .isPrimary(userRole.isPrimary())
                        .isActive(userRole.isActive())
                        .assignedAt(userRole.getAssignedAt())
                        .build())
                .toList();
        
        UserResponse.DepartmentInfo departmentInfo = null;
        if (user.getDepartment() != null) {
            departmentInfo = UserResponse.DepartmentInfo.builder()
                    .id(user.getDepartment().getId())
                    .name(user.getDepartment().getName())
                    .code(user.getDepartment().getCode())
                    .description(user.getDepartment().getDescription())
                    .departmentType(user.getDepartment().getDepartmentType().name())
                    .headId(user.getDepartment().getHeadId())
                    .isActive(user.getDepartment().isActive())
                    .location(user.getDepartment().getLocation())
                    .build();
        }
        
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
                .roles(roleInfos)
                .department(departmentInfo)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .build();
    }
    
    /**
     * User statistics class
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class UserStatistics {
        private long totalUsers;
        private long activeUsers;
        private long inactiveUsers;
        private long lockedUsers;
    }
}
