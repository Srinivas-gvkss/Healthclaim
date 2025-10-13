package com.gvkss.patil.user_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Role entity representing user roles in the IT Support Ticketing System.
 * Each role has specific permissions and access levels.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "roles", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "name"),
           @UniqueConstraint(columnNames = "code")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    
    /**
     * Unique identifier for the role
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Role name (e.g., "End User", "Support Agent")
     */
    @NotBlank(message = "Role name is required")
    @Size(min = 2, max = 100, message = "Role name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    /**
     * Role code (e.g., "END_USER", "SUPPORT_AGENT")
     */
    @NotBlank(message = "Role code is required")
    @Size(min = 2, max = 50, message = "Role code must be between 2 and 50 characters")
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;
    
    /**
     * Role description
     */
    @Size(max = 500, message = "Role description must not exceed 500 characters")
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * Role type from the UserRole enum
     */
    @NotNull(message = "Role type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    private com.gvkss.patil.user_service.enums.UserRole roleType;
    
    /**
     * Access level (1-5, where 5 is highest)
     */
    @NotNull(message = "Access level is required")
    @Column(name = "access_level", nullable = false)
    private Integer accessLevel;
    
    /**
     * Whether the role is active
     */
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    /**
     * Whether the role is system-defined (cannot be deleted)
     */
    @Builder.Default
    @Column(name = "is_system_defined", nullable = false)
    private Boolean isSystemDefined = false;
    
    /**
     * Permissions associated with this role (JSON format)
     */
    @Column(name = "permissions", columnDefinition = "TEXT")
    private String permissions;
    
    /**
     * Timestamp when the role was created
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the role was last updated
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * Users with this role
     */
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<com.gvkss.patil.user_service.entity.UserRole> userRoles = new ArrayList<>();
    
    /**
     * Get the role type from the UserRole enum
     * 
     * @return The UserRole enum value
     */
    public com.gvkss.patil.user_service.enums.UserRole getRoleType() {
        return roleType;
    }
    
    /**
     * Set the role type from the UserRole enum
     * 
     * @param roleType The UserRole enum value
     */
    public void setRoleType(com.gvkss.patil.user_service.enums.UserRole roleType) {
        this.roleType = roleType;
        if (roleType != null) {
            this.code = roleType.getCode();
            this.name = roleType.getDisplayName();
            this.accessLevel = roleType.getAccessLevel();
        }
    }
    
    /**
     * Check if this role has higher or equal access level than the given role
     * 
     * @param otherRole The role to compare with
     * @return true if this role has higher or equal access level
     */
    public boolean hasAccessLevel(Role otherRole) {
        if (otherRole == null) {
            return false;
        }
        return this.accessLevel >= otherRole.accessLevel;
    }
    
    /**
     * Check if this role has higher or equal access level than the given access level
     * 
     * @param accessLevel The access level to compare with
     * @return true if this role has higher or equal access level
     */
    public boolean hasAccessLevel(Integer accessLevel) {
        if (accessLevel == null) {
            return false;
        }
        return this.accessLevel >= accessLevel;
    }
    
    /**
     * Add a user to this role
     * 
     * @param userRole The user role mapping to add
     */
    public void addUserRole(com.gvkss.patil.user_service.entity.UserRole userRole) {
        if (userRole != null && !userRoles.contains(userRole)) {
            userRoles.add(userRole);
            userRole.setRole(this);
        }
    }
    
    /**
     * Remove a user from this role
     * 
     * @param userRole The user role mapping to remove
     */
    public void removeUserRole(com.gvkss.patil.user_service.entity.UserRole userRole) {
        if (userRole != null && userRoles.contains(userRole)) {
            userRoles.remove(userRole);
            userRole.setRole(null);
        }
    }
    
    /**
     * Get the number of users with this role
     * 
     * @return The number of users
     */
    public int getUserCount() {
        return userRoles.size();
    }
    
    /**
     * Check if the role is active
     * 
     * @return true if the role is active
     */
    public boolean isActive() {
        return isActive != null && isActive;
    }
    
    /**
     * Activate the role
     */
    public void activate() {
        this.isActive = true;
    }
    
    /**
     * Deactivate the role
     */
    public void deactivate() {
        this.isActive = false;
    }
    
    /**
     * Check if the role is system-defined
     * 
     * @return true if the role is system-defined
     */
    public boolean isSystemDefined() {
        return isSystemDefined != null && isSystemDefined;
    }
    
    /**
     * Mark the role as system-defined
     */
    public void markAsSystemDefined() {
        this.isSystemDefined = true;
    }
    
    /**
     * Mark the role as user-defined
     */
    public void markAsUserDefined() {
        this.isSystemDefined = false;
    }
    
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", roleType=" + roleType +
                ", accessLevel=" + accessLevel +
                ", isActive=" + isActive +
                '}';
    }
}
