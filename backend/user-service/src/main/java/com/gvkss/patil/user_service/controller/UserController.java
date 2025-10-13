package com.gvkss.patil.user_service.controller;

import com.gvkss.patil.user_service.dto.ApiResponse;
import com.gvkss.patil.user_service.dto.UserResponse;
import com.gvkss.patil.user_service.entity.User;
import com.gvkss.patil.user_service.enums.UserStatus;
import com.gvkss.patil.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * User controller for handling user management operations.
 * Provides endpoints for user CRUD operations, role management, and user queries.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "User management APIs")
public class UserController {
    
    private final UserService userService;
    
    /**
     * Get all users with pagination
     * 
     * @param page Page number (0-based)
     * @param size Page size
     * @param sortBy Sort field
     * @param sortDir Sort direction (asc, desc)
     * @return Page of users
     */
    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD')")
    @Operation(summary = "Get all users", description = "Retrieve all users with pagination and sorting")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.info("Get all users request - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<UserResponse> users = userService.getAllUsers(pageable);
        
        return ResponseEntity.ok(ApiResponse.success(users, "Users retrieved successfully"));
    }
    
    /**
     * Get user by ID
     * 
     * @param userId The user ID
     * @return User information
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD') or @userService.getCurrentUser().getId() == #userId")
    @Operation(summary = "Get user by ID", description = "Retrieve user information by user ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        
        log.info("Get user by ID request: {}", userId);
        
        UserResponse user = userService.getUserById(userId);
        
        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully"));
    }
    
    /**
     * Get user by email
     * 
     * @param email The user email
     * @return User information
     */
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD')")
    @Operation(summary = "Get user by email", description = "Retrieve user information by email address")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(
            @Parameter(description = "User email") @PathVariable String email) {
        
        log.info("Get user by email request: {}", email);
        
        UserResponse user = userService.getUserByEmail(email);
        
        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully"));
    }
    
    /**
     * Get users by status
     * 
     * @param status The user status
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of users
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD')")
    @Operation(summary = "Get users by status", description = "Retrieve users filtered by status")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsersByStatus(
            @Parameter(description = "User status") @PathVariable UserStatus status,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        log.info("Get users by status request: {}", status);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> users = userService.getUsersByStatus(status, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(users, "Users retrieved successfully"));
    }
    
    /**
     * Get users by department
     * 
     * @param departmentId The department ID
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of users
     */
    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD')")
    @Operation(summary = "Get users by department", description = "Retrieve users filtered by department")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsersByDepartment(
            @Parameter(description = "Department ID") @PathVariable Long departmentId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        log.info("Get users by department request: {}", departmentId);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> users = userService.getUsersByDepartment(departmentId, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(users, "Users retrieved successfully"));
    }
    
    /**
     * Get users by role
     * 
     * @param roleCode The role code
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of users
     */
    @GetMapping("/role/{roleCode}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD')")
    @Operation(summary = "Get users by role", description = "Retrieve users filtered by role")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsersByRole(
            @Parameter(description = "Role code") @PathVariable String roleCode,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        log.info("Get users by role request: {}", roleCode);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> users = userService.getUsersByRole(roleCode, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(users, "Users retrieved successfully"));
    }
    
    /**
     * Search users by name
     * 
     * @param searchTerm The search term
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of users
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD')")
    @Operation(summary = "Search users by name", description = "Search users by first name or last name")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Page<UserResponse>>> searchUsersByName(
            @Parameter(description = "Search term") @RequestParam String searchTerm,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        log.info("Search users by name request: {}", searchTerm);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> users = userService.searchUsersByName(searchTerm, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(users, "Users retrieved successfully"));
    }
    
    /**
     * Get active users
     * 
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of active users
     */
    @GetMapping("/active")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD')")
    @Operation(summary = "Get active users", description = "Retrieve all active users")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Active users retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getActiveUsers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        log.info("Get active users request");
        
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> users = userService.getActiveUsers(pageable);
        
        return ResponseEntity.ok(ApiResponse.success(users, "Active users retrieved successfully"));
    }
    
    /**
     * Create a new user
     * 
     * @param user The user to create
     * @return Created user information
     */
    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Create user", description = "Create a new user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "User already exists"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody User user) {
        
        log.info("Create user request: {}", user.getEmail());
        
        UserResponse createdUser = userService.createUser(user);
        
        return ResponseEntity.status(201)
                .body(ApiResponse.created(createdUser, "User created successfully"));
    }
    
    /**
     * Update user information
     * 
     * @param userId The user ID
     * @param updatedUser The updated user information
     * @return Updated user information
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD') or @userService.getCurrentUser().getId() == #userId")
    @Operation(summary = "Update user", description = "Update user information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Valid @RequestBody User updatedUser) {
        
        log.info("Update user request: {}", userId);
        
        UserResponse updatedUserResponse = userService.updateUser(userId, updatedUser);
        
        return ResponseEntity.ok(ApiResponse.success(updatedUserResponse, "User updated successfully"));
    }
    
    /**
     * Delete user (soft delete)
     * 
     * @param userId The user ID
     * @return Success response
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Delete user", description = "Delete user (soft delete)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Object>> deleteUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        
        log.info("Delete user request: {}", userId);
        
        userService.deleteUser(userId);
        
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }
    
    /**
     * Activate user account
     * 
     * @param userId The user ID
     * @return Updated user information
     */
    @PostMapping("/{userId}/activate")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Activate user", description = "Activate user account")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User activated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<UserResponse>> activateUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        
        log.info("Activate user request: {}", userId);
        
        UserResponse activatedUser = userService.activateUser(userId);
        
        return ResponseEntity.ok(ApiResponse.success(activatedUser, "User activated successfully"));
    }
    
    /**
     * Deactivate user account
     * 
     * @param userId The user ID
     * @return Updated user information
     */
    @PostMapping("/{userId}/deactivate")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Deactivate user", description = "Deactivate user account")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User deactivated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<UserResponse>> deactivateUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        
        log.info("Deactivate user request: {}", userId);
        
        UserResponse deactivatedUser = userService.deactivateUser(userId);
        
        return ResponseEntity.ok(ApiResponse.success(deactivatedUser, "User deactivated successfully"));
    }
    
    /**
     * Lock user account
     * 
     * @param userId The user ID
     * @return Updated user information
     */
    @PostMapping("/{userId}/lock")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Lock user", description = "Lock user account")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User locked successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<UserResponse>> lockUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        
        log.info("Lock user request: {}", userId);
        
        UserResponse lockedUser = userService.lockUser(userId);
        
        return ResponseEntity.ok(ApiResponse.success(lockedUser, "User locked successfully"));
    }
    
    /**
     * Unlock user account
     * 
     * @param userId The user ID
     * @return Updated user information
     */
    @PostMapping("/{userId}/unlock")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Unlock user", description = "Unlock user account")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User unlocked successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<UserResponse>> unlockUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        
        log.info("Unlock user request: {}", userId);
        
        UserResponse unlockedUser = userService.unlockUser(userId);
        
        return ResponseEntity.ok(ApiResponse.success(unlockedUser, "User unlocked successfully"));
    }
    
    /**
     * Assign role to user
     * 
     * @param userId The user ID
     * @param roleAssignmentRequest The role assignment request
     * @return Updated user information
     */
    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Assign role to user", description = "Assign a role to a user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Role assigned successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User or role not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<UserResponse>> assignRoleToUser(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Valid @RequestBody RoleAssignmentRequest roleAssignmentRequest) {
        
        log.info("Assign role to user request: {} -> {}", userId, roleAssignmentRequest.getRoleCode());
        
        UserResponse updatedUser = userService.assignRoleToUser(
                userId, 
                roleAssignmentRequest.getRoleCode(), 
                roleAssignmentRequest.isPrimary()
        );
        
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "Role assigned successfully"));
    }
    
    /**
     * Remove role from user
     * 
     * @param userId The user ID
     * @param roleCode The role code
     * @return Updated user information
     */
    @DeleteMapping("/{userId}/roles/{roleCode}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Remove role from user", description = "Remove a role from a user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Role removed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User or role not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<UserResponse>> removeRoleFromUser(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Role code") @PathVariable String roleCode) {
        
        log.info("Remove role from user request: {} -> {}", userId, roleCode);
        
        UserResponse updatedUser = userService.removeRoleFromUser(userId, roleCode);
        
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "Role removed successfully"));
    }
    
    /**
     * Assign department to user
     * 
     * @param userId The user ID
     * @param departmentAssignmentRequest The department assignment request
     * @return Updated user information
     */
    @PostMapping("/{userId}/department")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Assign department to user", description = "Assign a department to a user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Department assigned successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User or department not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<UserResponse>> assignDepartmentToUser(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Valid @RequestBody DepartmentAssignmentRequest departmentAssignmentRequest) {
        
        log.info("Assign department to user request: {} -> {}", userId, departmentAssignmentRequest.getDepartmentId());
        
        UserResponse updatedUser = userService.assignDepartmentToUser(userId, departmentAssignmentRequest.getDepartmentId());
        
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "Department assigned successfully"));
    }
    
    /**
     * Get user statistics
     * 
     * @return User statistics
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Get user statistics", description = "Get user statistics and counts")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<UserService.UserStatistics>> getUserStatistics() {
        
        log.info("Get user statistics request");
        
        UserService.UserStatistics statistics = userService.getUserStatistics();
        
        return ResponseEntity.ok(ApiResponse.success(statistics, "Statistics retrieved successfully"));
    }
    
    /**
     * Role assignment request DTO
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class RoleAssignmentRequest {
        @jakarta.validation.constraints.NotBlank(message = "Role code is required")
        private String roleCode;
        
        @lombok.Builder.Default
        private boolean primary = false;
    }
    
    /**
     * Department assignment request DTO
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class DepartmentAssignmentRequest {
        @jakarta.validation.constraints.NotNull(message = "Department ID is required")
        private Long departmentId;
    }
}
