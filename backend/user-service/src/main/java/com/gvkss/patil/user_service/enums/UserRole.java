package com.gvkss.patil.user_service.enums;

/**
 * Enumeration representing user roles in the Healthcare Insurance Claim System.
 * Aligned with frontend role definitions.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
public enum UserRole {
    
    /**
     * Patient - Can submit and track personal insurance claims
     * Access Level: 1 (Lowest)
     * Permissions: Submit claims, view own claims, upload documents, update profile, view claim status
     */
    PATIENT("patient", "Patient", 1),
    
    /**
     * Doctor - Healthcare provider who can verify and support patient claims
     * Access Level: 2
     * Permissions: Verify claims, view patient claims, upload medical records, provide medical opinion
     */
    DOCTOR("doctor", "Doctor", 2),
    
    /**
     * Admin - Platform administrator with management capabilities
     * Access Level: 3
     * Permissions: Manage the platform, process claims, user management, system configuration
     */
    ADMIN("admin", "Admin", 3),
    
    /**
     * Insurance Provider - Reviews and approves claims
     * Access Level: 4
     * Permissions: Review claims, approve/reject claims, access reports, manage policies
     */
    INSURANCE_PROVIDER("insurance_provider", "Insurance Provider", 4);
    
    private final String code;
    private final String displayName;
    private final int accessLevel;
    
    /**
     * Constructor for UserRole enum
     * 
     * @param code The role code (lowercase, matches frontend)
     * @param displayName The display name for the role
     * @param accessLevel The access level (1-4, where 4 is highest)
     */
    UserRole(String code, String displayName, int accessLevel) {
        this.code = code;
        this.displayName = displayName;
        this.accessLevel = accessLevel;
    }
    
    /**
     * Get the role code
     * 
     * @return The role code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Get the display name
     * 
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Get the access level
     * 
     * @return The access level (1-4)
     */
    public int getAccessLevel() {
        return accessLevel;
    }
    
    /**
     * Check if this role has higher or equal access level than the given role
     * 
     * @param otherRole The role to compare with
     * @return true if this role has higher or equal access level
     */
    public boolean hasAccessLevel(UserRole otherRole) {
        return this.accessLevel >= otherRole.accessLevel;
    }
    
    /**
     * Get role by code (case-insensitive)
     * 
     * @param code The role code
     * @return The UserRole enum value
     * @throws IllegalArgumentException if code is not found
     */
    public static UserRole fromCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Role code cannot be null");
        }
        for (UserRole role : values()) {
            if (role.code.equalsIgnoreCase(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role code: " + code);
    }
    
    /**
     * Get role by display name
     * 
     * @param displayName The display name
     * @return The UserRole enum value
     * @throws IllegalArgumentException if display name is not found
     */
    public static UserRole fromDisplayName(String displayName) {
        for (UserRole role : values()) {
            if (role.displayName.equals(displayName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role display name: " + displayName);
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
