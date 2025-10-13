package com.gvkss.patil.user_service.service;

import com.gvkss.patil.user_service.entity.Role;
import com.gvkss.patil.user_service.enums.UserRole;
import com.gvkss.patil.user_service.repository.RoleRepository;
import com.gvkss.patil.user_service.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Role service for managing role operations.
 * Provides methods for role CRUD operations and role management.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoleService {
    
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    
    /**
     * Get all roles with pagination
     * 
     * @param pageable Pagination information
     * @return Page of roles
     */
    @Transactional(readOnly = true)
    public Page<Role> getAllRoles(Pageable pageable) {
        log.info("Fetching all roles with pagination: {}", pageable);
        
        return roleRepository.findAll(pageable);
    }
    
    /**
     * Get all active roles
     * 
     * @return List of active roles
     */
    @Transactional(readOnly = true)
    public List<Role> getActiveRoles() {
        log.info("Fetching all active roles");
        
        return roleRepository.findByIsActiveTrue();
    }
    
    /**
     * Get role by ID
     * 
     * @param roleId The role ID
     * @return Role entity
     */
    @Transactional(readOnly = true)
    public Role getRoleById(Long roleId) {
        log.info("Fetching role by ID: {}", roleId);
        
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
    }
    
    /**
     * Get role by code
     * 
     * @param roleCode The role code
     * @return Role entity
     */
    @Transactional(readOnly = true)
    public Role getRoleByCode(String roleCode) {
        log.info("Fetching role by code: {}", roleCode);
        
        return roleRepository.findByCode(roleCode)
                .orElseThrow(() -> new RuntimeException("Role not found with code: " + roleCode));
    }
    
    /**
     * Get role by name
     * 
     * @param roleName The role name
     * @return Role entity
     */
    @Transactional(readOnly = true)
    public Role getRoleByName(String roleName) {
        log.info("Fetching role by name: {}", roleName);
        
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleName));
    }
    
    /**
     * Get roles by access level
     * 
     * @param accessLevel The access level
     * @return List of roles with the specified access level
     */
    @Transactional(readOnly = true)
    public List<Role> getRolesByAccessLevel(Integer accessLevel) {
        log.info("Fetching roles by access level: {}", accessLevel);
        
        return roleRepository.findByAccessLevel(accessLevel);
    }
    
    /**
     * Get roles with access level greater than or equal to the specified level
     * 
     * @param accessLevel The access level
     * @return List of roles with access level >= specified level
     */
    @Transactional(readOnly = true)
    public List<Role> getRolesWithAccessLevelGreaterThanEqual(Integer accessLevel) {
        log.info("Fetching roles with access level >= {}", accessLevel);
        
        return roleRepository.findByAccessLevelGreaterThanEqual(accessLevel);
    }
    
    /**
     * Get roles with access level less than or equal to the specified level
     * 
     * @param accessLevel The access level
     * @return List of roles with access level <= specified level
     */
    @Transactional(readOnly = true)
    public List<Role> getRolesWithAccessLevelLessThanEqual(Integer accessLevel) {
        log.info("Fetching roles with access level <= {}", accessLevel);
        
        return roleRepository.findByAccessLevelLessThanEqual(accessLevel);
    }
    
    /**
     * Get roles by access level range
     * 
     * @param minAccessLevel The minimum access level
     * @param maxAccessLevel The maximum access level
     * @return List of roles within the access level range
     */
    @Transactional(readOnly = true)
    public List<Role> getRolesByAccessLevelRange(Integer minAccessLevel, Integer maxAccessLevel) {
        log.info("Fetching roles with access level between {} and {}", minAccessLevel, maxAccessLevel);
        
        return roleRepository.findByAccessLevelBetween(minAccessLevel, maxAccessLevel);
    }
    
    /**
     * Search roles by name
     * 
     * @param searchTerm The search term
     * @param pageable Pagination information
     * @return Page of roles
     */
    @Transactional(readOnly = true)
    public Page<Role> searchRolesByName(String searchTerm, Pageable pageable) {
        log.info("Searching roles by name: {}", searchTerm);
        
        return roleRepository.findByNameContaining(searchTerm, pageable);
    }
    
    /**
     * Search roles by description
     * 
     * @param searchTerm The search term
     * @param pageable Pagination information
     * @return Page of roles
     */
    @Transactional(readOnly = true)
    public Page<Role> searchRolesByDescription(String searchTerm, Pageable pageable) {
        log.info("Searching roles by description: {}", searchTerm);
        
        return roleRepository.findByDescriptionContaining(searchTerm, pageable);
    }
    
    /**
     * Get system-defined roles
     * 
     * @return List of system-defined roles
     */
    @Transactional(readOnly = true)
    public List<Role> getSystemDefinedRoles() {
        log.info("Fetching system-defined roles");
        
        return roleRepository.findByIsSystemDefinedTrue();
    }
    
    /**
     * Get user-defined roles
     * 
     * @return List of user-defined roles
     */
    @Transactional(readOnly = true)
    public List<Role> getUserDefinedRoles() {
        log.info("Fetching user-defined roles");
        
        return roleRepository.findByIsSystemDefinedFalse();
    }
    
    /**
     * Get roles with no users assigned
     * 
     * @return List of roles with no users
     */
    @Transactional(readOnly = true)
    public List<Role> getRolesWithNoUsers() {
        log.info("Fetching roles with no users assigned");
        
        return roleRepository.findRolesWithNoUsers();
    }
    
    /**
     * Get roles with users assigned
     * 
     * @return List of roles with users
     */
    @Transactional(readOnly = true)
    public List<Role> getRolesWithUsers() {
        log.info("Fetching roles with users assigned");
        
        return roleRepository.findRolesWithUsers();
    }
    
    /**
     * Create a new role
     * 
     * @param role The role to create
     * @return Created role
     */
    public Role createRole(Role role) {
        log.info("Creating new role: {}", role.getName());
        
        // Validate role code uniqueness
        if (roleRepository.existsByCode(role.getCode())) {
            throw new RuntimeException("Role code is already in use: " + role.getCode());
        }
        
        // Validate role name uniqueness
        if (roleRepository.existsByName(role.getName())) {
            throw new RuntimeException("Role name is already in use: " + role.getName());
        }
        
        // Set default values
        if (role.getIsActive() == null) {
            role.setIsActive(true);
        }
        if (role.getIsSystemDefined() == null) {
            role.setIsSystemDefined(false);
        }
        
        Role savedRole = roleRepository.save(role);
        log.info("Role created successfully: {}", savedRole.getName());
        
        return savedRole;
    }
    
    /**
     * Create a role from UserRole enum
     * 
     * @param userRole The UserRole enum value
     * @return Created role
     */
    public Role createRoleFromEnum(UserRole userRole) {
        log.info("Creating role from enum: {}", userRole);
        
        // Check if role already exists
        if (roleRepository.existsByCode(userRole.getCode())) {
            return roleRepository.findByCode(userRole.getCode()).orElse(null);
        }
        
        Role role = Role.builder()
                .name(userRole.getDisplayName())
                .code(userRole.getCode())
                .description("System-defined role: " + userRole.getDisplayName())
                .roleType(userRole)
                .accessLevel(userRole.getAccessLevel())
                .isActive(true)
                .isSystemDefined(true)
                .build();
        
        Role savedRole = roleRepository.save(role);
        log.info("Role created from enum successfully: {}", savedRole.getName());
        
        return savedRole;
    }
    
    /**
     * Update role information
     * 
     * @param roleId The role ID
     * @param updatedRole The updated role information
     * @return Updated role
     */
    public Role updateRole(Long roleId, Role updatedRole) {
        log.info("Updating role: {}", roleId);
        
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
        
        // Check if role is system-defined
        if (existingRole.isSystemDefined()) {
            throw new RuntimeException("Cannot update system-defined role: " + existingRole.getName());
        }
        
        // Update fields
        if (updatedRole.getName() != null) {
            // Validate name uniqueness
            if (!existingRole.getName().equals(updatedRole.getName()) && 
                roleRepository.existsByName(updatedRole.getName())) {
                throw new RuntimeException("Role name is already in use: " + updatedRole.getName());
            }
            existingRole.setName(updatedRole.getName());
        }
        
        if (updatedRole.getDescription() != null) {
            existingRole.setDescription(updatedRole.getDescription());
        }
        
        if (updatedRole.getAccessLevel() != null) {
            existingRole.setAccessLevel(updatedRole.getAccessLevel());
        }
        
        if (updatedRole.getPermissions() != null) {
            existingRole.setPermissions(updatedRole.getPermissions());
        }
        
        Role savedRole = roleRepository.save(existingRole);
        log.info("Role updated successfully: {}", savedRole.getName());
        
        return savedRole;
    }
    
    /**
     * Delete role
     * 
     * @param roleId The role ID
     */
    public void deleteRole(Long roleId) {
        log.info("Deleting role: {}", roleId);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
        
        // Check if role is system-defined
        if (role.isSystemDefined()) {
            throw new RuntimeException("Cannot delete system-defined role: " + role.getName());
        }
        
        // Check if role has users assigned
        if (userRoleRepository.countByRoleIdAndIsActiveTrue(roleId) > 0) {
            throw new RuntimeException("Cannot delete role with active users assigned: " + role.getName());
        }
        
        roleRepository.delete(role);
        log.info("Role deleted successfully: {}", role.getName());
    }
    
    /**
     * Activate role
     * 
     * @param roleId The role ID
     * @return Updated role
     */
    public Role activateRole(Long roleId) {
        log.info("Activating role: {}", roleId);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
        
        role.activate();
        Role savedRole = roleRepository.save(role);
        
        log.info("Role activated successfully: {}", savedRole.getName());
        return savedRole;
    }
    
    /**
     * Deactivate role
     * 
     * @param roleId The role ID
     * @return Updated role
     */
    public Role deactivateRole(Long roleId) {
        log.info("Deactivating role: {}", roleId);
        
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));
        
        role.deactivate();
        Role savedRole = roleRepository.save(role);
        
        log.info("Role deactivated successfully: {}", savedRole.getName());
        return savedRole;
    }
    
    /**
     * Get role statistics
     * 
     * @return Role statistics
     */
    @Transactional(readOnly = true)
    public RoleStatistics getRoleStatistics() {
        log.info("Fetching role statistics");
        
        long totalRoles = roleRepository.count();
        long activeRoles = roleRepository.countByIsActiveTrue();
        long systemDefinedRoles = roleRepository.countByIsSystemDefinedTrue();
        long userDefinedRoles = roleRepository.countByIsSystemDefinedFalse();
        
        return RoleStatistics.builder()
                .totalRoles(totalRoles)
                .activeRoles(activeRoles)
                .inactiveRoles(totalRoles - activeRoles)
                .systemDefinedRoles(systemDefinedRoles)
                .userDefinedRoles(userDefinedRoles)
                .build();
    }
    
    /**
     * Initialize default roles
     * Creates all system-defined roles if they don't exist
     */
    public void initializeDefaultRoles() {
        log.info("Initializing default roles");
        
        for (UserRole userRole : UserRole.values()) {
            if (!roleRepository.existsByCode(userRole.getCode())) {
                createRoleFromEnum(userRole);
            }
        }
        
        log.info("Default roles initialization completed");
    }
    
    /**
     * Role statistics class
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class RoleStatistics {
        private long totalRoles;
        private long activeRoles;
        private long inactiveRoles;
        private long systemDefinedRoles;
        private long userDefinedRoles;
    }
}
