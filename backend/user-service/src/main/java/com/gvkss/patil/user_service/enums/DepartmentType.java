package com.gvkss.patil.user_service.enums;

/**
 * Enumeration representing department types in the Healthcare Insurance Claim System.
 * Simplified for organizational purposes.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
public enum DepartmentType {
    
    /**
     * General Department
     * For general operations and default assignments
     */
    GENERAL("GENERAL", "General", "General operations and services"),
    
    /**
     * Administration Department
     * For administrative staff and management
     */
    ADMINISTRATION("ADMINISTRATION", "Administration", "Administrative and management operations"),
    
    /**
     * Claims Processing Department
     * For insurance claims processing staff
     */
    CLAIMS_PROCESSING("CLAIMS_PROCESSING", "Claims Processing", "Insurance claims processing and review");
    
    private final String code;
    private final String displayName;
    private final String description;
    
    /**
     * Constructor for DepartmentType enum
     * 
     * @param code The department type code
     * @param displayName The display name for the department type
     * @param description The description of the department type
     */
    DepartmentType(String code, String displayName, String description) {
        this.code = code;
        this.displayName = displayName;
        this.description = description;
    }
    
    /**
     * Get the department type code
     * 
     * @return The department type code
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
     * Get the description
     * 
     * @return The description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Get department type by code
     * 
     * @param code The department type code
     * @return The DepartmentType enum value
     * @throws IllegalArgumentException if code is not found
     */
    public static DepartmentType fromCode(String code) {
        for (DepartmentType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid department type code: " + code);
    }
    
    /**
     * Get department type by display name
     * 
     * @param displayName The display name
     * @return The DepartmentType enum value
     * @throws IllegalArgumentException if display name is not found
     */
    public static DepartmentType fromDisplayName(String displayName) {
        for (DepartmentType type : values()) {
            if (type.displayName.equals(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid department type display name: " + displayName);
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
