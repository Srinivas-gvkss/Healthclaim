package com.gvkss.patil.user_service.controller;

import com.gvkss.patil.user_service.dto.ApiResponse;
import com.gvkss.patil.user_service.entity.Role;
import com.gvkss.patil.user_service.enums.UserRole;
import com.gvkss.patil.user_service.service.RoleService;
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

import java.util.List;

/**
 * Role controller for handling role management operations.
 * Provides endpoints for role CRUD operations and role management.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Role Management", description = "Role management APIs")
public class RoleController {
    
    private final RoleService roleService;
    
    /**
     * Get all roles with pagination
     * 
     * @param page Page number (0-based)
     * @param size Page size
     * @param sortBy Sort field
     * @param sortDir Sort direction (asc, desc)
     * @return Page of roles
     */
    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Get all roles", description = "Retrieve all roles with pagination and sorting")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Page<Role>>> getAllRoles(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.info("Get all roles request - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Role> roles = roleService.getAllRoles(pageable);
        
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles retrieved successfully"));
    }
    
    /**
     * Get all active roles
     * 
     * @return List of active roles
     */
    @GetMapping("/active")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD')")
    @Operation(summary = "Get active roles", description = "Retrieve all active roles")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Active roles retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<List<Role>>> getActiveRoles() {
        
        log.info("Get active roles request");
        
        List<Role> roles = roleService.getActiveRoles();
        
        return ResponseEntity.ok(ApiResponse.success(roles, "Active roles retrieved successfully"));
    }
    
    /**
     * Get all active roles (public endpoint for development)
     * 
     * @return List of active roles
     */
    @GetMapping("/public/active")
    @Operation(summary = "Get active roles (public)", description = "Retrieve all active roles without authentication")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Active roles retrieved successfully")
    })
    public ResponseEntity<ApiResponse<List<Role>>> getActiveRolesPublic() {
        
        log.info("Get active roles request (public)");
        
        List<Role> roles = roleService.getActiveRoles();
        
        return ResponseEntity.ok(ApiResponse.success(roles, "Active roles retrieved successfully"));
    }
    
    /**
     * Get role by ID
     * 
     * @param roleId The role ID
     * @return Role information
     */
    @GetMapping("/{roleId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD')")
    @Operation(summary = "Get role by ID", description = "Retrieve role information by role ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Role retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Role not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Role>> getRoleById(
            @Parameter(description = "Role ID") @PathVariable Long roleId) {
        
        log.info("Get role by ID request: {}", roleId);
        
        Role role = roleService.getRoleById(roleId);
        
        return ResponseEntity.ok(ApiResponse.success(role, "Role retrieved successfully"));
    }
    
    /**
     * Get role by code
     * 
     * @param roleCode The role code
     * @return Role information
     */
    @GetMapping("/code/{roleCode}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD')")
    @Operation(summary = "Get role by code", description = "Retrieve role information by role code")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Role retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Role not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Role>> getRoleByCode(
            @Parameter(description = "Role code") @PathVariable String roleCode) {
        
        log.info("Get role by code request: {}", roleCode);
        
        Role role = roleService.getRoleByCode(roleCode);
        
        return ResponseEntity.ok(ApiResponse.success(role, "Role retrieved successfully"));
    }
    
    /**
     * Get roles by access level
     * 
     * @param accessLevel The access level
     * @return List of roles with the specified access level
     */
    @GetMapping("/access-level/{accessLevel}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Get roles by access level", description = "Retrieve roles filtered by access level")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<List<Role>>> getRolesByAccessLevel(
            @Parameter(description = "Access level") @PathVariable Integer accessLevel) {
        
        log.info("Get roles by access level request: {}", accessLevel);
        
        List<Role> roles = roleService.getRolesByAccessLevel(accessLevel);
        
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles retrieved successfully"));
    }
    
    /**
     * Get roles with access level greater than or equal to the specified level
     * 
     * @param accessLevel The access level
     * @return List of roles with access level >= specified level
     */
    @GetMapping("/access-level/gte/{accessLevel}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Get roles with access level >= specified", description = "Retrieve roles with access level greater than or equal to specified level")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<List<Role>>> getRolesWithAccessLevelGreaterThanEqual(
            @Parameter(description = "Access level") @PathVariable Integer accessLevel) {
        
        log.info("Get roles with access level >= request: {}", accessLevel);
        
        List<Role> roles = roleService.getRolesWithAccessLevelGreaterThanEqual(accessLevel);
        
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles retrieved successfully"));
    }
    
    /**
     * Search roles by name
     * 
     * @param searchTerm The search term
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of roles
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Search roles by name", description = "Search roles by name")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Page<Role>>> searchRolesByName(
            @Parameter(description = "Search term") @RequestParam String searchTerm,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        log.info("Search roles by name request: {}", searchTerm);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Role> roles = roleService.searchRolesByName(searchTerm, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles retrieved successfully"));
    }
    
    /**
     * Get system-defined roles
     * 
     * @return List of system-defined roles
     */
    @GetMapping("/system-defined")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Get system-defined roles", description = "Retrieve all system-defined roles")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "System-defined roles retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<List<Role>>> getSystemDefinedRoles() {
        
        log.info("Get system-defined roles request");
        
        List<Role> roles = roleService.getSystemDefinedRoles();
        
        return ResponseEntity.ok(ApiResponse.success(roles, "System-defined roles retrieved successfully"));
    }
    
    /**
     * Get user-defined roles
     * 
     * @return List of user-defined roles
     */
    @GetMapping("/user-defined")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Get user-defined roles", description = "Retrieve all user-defined roles")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User-defined roles retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<List<Role>>> getUserDefinedRoles() {
        
        log.info("Get user-defined roles request");
        
        List<Role> roles = roleService.getUserDefinedRoles();
        
        return ResponseEntity.ok(ApiResponse.success(roles, "User-defined roles retrieved successfully"));
    }
    
    /**
     * Get roles with no users assigned
     * 
     * @return List of roles with no users
     */
    @GetMapping("/no-users")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Get roles with no users", description = "Retrieve roles that have no users assigned")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<List<Role>>> getRolesWithNoUsers() {
        
        log.info("Get roles with no users request");
        
        List<Role> roles = roleService.getRolesWithNoUsers();
        
        return ResponseEntity.ok(ApiResponse.success(roles, "Roles retrieved successfully"));
    }
    
    /**
     * Create a new role
     * 
     * @param role The role to create
     * @return Created role information
     */
    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Create role", description = "Create a new role")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Role created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Role already exists"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Role>> createRole(
            @Valid @RequestBody Role role) {
        
        log.info("Create role request: {}", role.getName());
        
        Role createdRole = roleService.createRole(role);
        
        return ResponseEntity.status(201)
                .body(ApiResponse.created(createdRole, "Role created successfully"));
    }
    
    /**
     * Create a role from UserRole enum
     * 
     * @param roleType The UserRole enum value
     * @return Created role information
     */
    @PostMapping("/from-enum/{roleType}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Create role from enum", description = "Create a role from UserRole enum")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Role created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Role>> createRoleFromEnum(
            @Parameter(description = "Role type") @PathVariable UserRole roleType) {
        
        log.info("Create role from enum request: {}", roleType);
        
        Role createdRole = roleService.createRoleFromEnum(roleType);
        
        return ResponseEntity.status(201)
                .body(ApiResponse.created(createdRole, "Role created successfully"));
    }
    
    /**
     * Update role information
     * 
     * @param roleId The role ID
     * @param updatedRole The updated role information
     * @return Updated role information
     */
    @PutMapping("/{roleId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Update role", description = "Update role information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Role updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Role not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Role>> updateRole(
            @Parameter(description = "Role ID") @PathVariable Long roleId,
            @Valid @RequestBody Role updatedRole) {
        
        log.info("Update role request: {}", roleId);
        
        Role updatedRoleResponse = roleService.updateRole(roleId, updatedRole);
        
        return ResponseEntity.ok(ApiResponse.success(updatedRoleResponse, "Role updated successfully"));
    }
    
    /**
     * Delete role
     * 
     * @param roleId The role ID
     * @return Success response
     */
    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Delete role", description = "Delete role")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Role deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Role not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Cannot delete role with users assigned"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Object>> deleteRole(
            @Parameter(description = "Role ID") @PathVariable Long roleId) {
        
        log.info("Delete role request: {}", roleId);
        
        roleService.deleteRole(roleId);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Role deleted successfully"));
    }
    
    /**
     * Activate role
     * 
     * @param roleId The role ID
     * @return Updated role information
     */
    @PostMapping("/{roleId}/activate")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Activate role", description = "Activate role")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Role activated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Role not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Role>> activateRole(
            @Parameter(description = "Role ID") @PathVariable Long roleId) {
        
        log.info("Activate role request: {}", roleId);
        
        Role activatedRole = roleService.activateRole(roleId);
        
        return ResponseEntity.ok(ApiResponse.success(activatedRole, "Role activated successfully"));
    }
    
    /**
     * Deactivate role
     * 
     * @param roleId The role ID
     * @return Updated role information
     */
    @PostMapping("/{roleId}/deactivate")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Deactivate role", description = "Deactivate role")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Role deactivated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Role not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Role>> deactivateRole(
            @Parameter(description = "Role ID") @PathVariable Long roleId) {
        
        log.info("Deactivate role request: {}", roleId);
        
        Role deactivatedRole = roleService.deactivateRole(roleId);
        
        return ResponseEntity.ok(ApiResponse.success(deactivatedRole, "Role deactivated successfully"));
    }
    
    /**
     * Initialize default roles
     * 
     * @return Success response
     */
    @PostMapping("/initialize")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Initialize default roles", description = "Initialize all system-defined roles")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Default roles initialized successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Object>> initializeDefaultRoles() {
        
        log.info("Initialize default roles request");
        
        roleService.initializeDefaultRoles();
        
        return ResponseEntity.ok(ApiResponse.success(null, "Default roles initialized successfully"));
    }
    
    /**
     * Initialize default roles (public endpoint for development)
     * 
     * @return Success response
     */
    @PostMapping("/public/initialize")
    @Operation(summary = "Initialize default roles (public)", description = "Initialize all system-defined roles without authentication")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Default roles initialized successfully")
    })
    public ResponseEntity<ApiResponse<Object>> initializeDefaultRolesPublic() {
        
        log.info("Initialize default roles request (public)");
        
        roleService.initializeDefaultRoles();
        
        return ResponseEntity.ok(ApiResponse.success(null, "Default roles initialized successfully"));
    }
    
    /**
     * Get role statistics
     * 
     * @return Role statistics
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Get role statistics", description = "Get role statistics and counts")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<RoleService.RoleStatistics>> getRoleStatistics() {
        
        log.info("Get role statistics request");
        
        RoleService.RoleStatistics statistics = roleService.getRoleStatistics();
        
        return ResponseEntity.ok(ApiResponse.success(statistics, "Statistics retrieved successfully"));
    }
}
