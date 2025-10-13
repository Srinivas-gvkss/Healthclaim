package com.gvkss.patil.user_service.enums;

/**
 * Enumeration representing user account status in the IT Support Ticketing System.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
public enum UserStatus {
    
    /**
     * Active - User account is active and can access the system
     */
    ACTIVE("ACTIVE", "Active", true),
    
    /**
     * Inactive - User account is temporarily disabled
     */
    INACTIVE("INACTIVE", "Inactive", false),
    
    /**
     * Locked - User account is locked due to security reasons
     */
    LOCKED("LOCKED", "Locked", false),
    
    /**
     * Pending - User account is pending activation
     */
    PENDING("PENDING", "Pending Activation", false),
    
    /**
     * Suspended - User account is suspended due to policy violations
     */
    SUSPENDED("SUSPENDED", "Suspended", false),
    
    /**
     * Deleted - User account is soft deleted
     */
    DELETED("DELETED", "Deleted", false);
    
    private final String code;
    private final String displayName;
    private final boolean canAccess;
    
    /**
     * Constructor for UserStatus enum
     * 
     * @param code The status code
     * @param displayName The display name for the status
     * @param canAccess Whether the user can access the system with this status
     */
    UserStatus(String code, String displayName, boolean canAccess) {
        this.code = code;
        this.displayName = displayName;
        this.canAccess = canAccess;
    }
    
    /**
     * Get the status code
     * 
     * @return The status code
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
     * Check if user can access the system with this status
     * 
     * @return true if user can access the system
     */
    public boolean canAccess() {
        return canAccess;
    }
    
    /**
     * Get status by code
     * 
     * @param code The status code
     * @return The UserStatus enum value
     * @throws IllegalArgumentException if code is not found
     */
    public static UserStatus fromCode(String code) {
        for (UserStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }
    
    /**
     * Get status by display name
     * 
     * @param displayName The display name
     * @return The UserStatus enum value
     * @throws IllegalArgumentException if display name is not found
     */
    public static UserStatus fromDisplayName(String displayName) {
        for (UserStatus status : values()) {
            if (status.displayName.equals(displayName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status display name: " + displayName);
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
