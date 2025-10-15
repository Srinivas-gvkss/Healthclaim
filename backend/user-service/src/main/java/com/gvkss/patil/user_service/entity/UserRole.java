package com.gvkss.patil.user_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * UserRole entity representing the many-to-many relationship between User and Role entities.
 * This entity allows users to have multiple roles and roles to be assigned to multiple users.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "user_roles", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"user_id", "role_id"})
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {
    
    /**
     * Unique identifier for the user role mapping
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * User associated with this role
     */
    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /**
     * Role associated with this user
     */
    @NotNull(message = "Role is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    
    /**
     * Whether this role assignment is active
     */
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    /**
     * Whether this is the primary role for the user
     */
    @Builder.Default
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;
    
    /**
     * Timestamp when the role was assigned
     */
    @CreationTimestamp
    @Column(name = "assigned_at", nullable = false, updatable = false)
    private LocalDateTime assignedAt;
    
    /**
     * Timestamp when the role assignment was last updated
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * User who assigned this role (for audit purposes)
     */
    @Column(name = "assigned_by")
    private Long assignedBy;
    
    /**
     * Notes about this role assignment
     */
    @Column(name = "notes", length = 500)
    private String notes;
    
    /**
     * Check if the role assignment is active
     * 
     * @return true if the role assignment is active
     */
    public boolean isActive() {
        return isActive != null && isActive;
    }
    
    /**
     * Activate the role assignment
     */
    public void activate() {
        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Deactivate the role assignment
     */
    public void deactivate() {
        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Check if this is the primary role
     * 
     * @return true if this is the primary role
     */
    public boolean isPrimary() {
        return isPrimary != null && isPrimary;
    }
    
    /**
     * Set as primary role
     */
    public void setAsPrimary() {
        this.isPrimary = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Remove primary role status
     */
    public void removePrimary() {
        this.isPrimary = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Update the role assignment
     * 
     * @param assignedBy The user who is updating the assignment
     * @param notes Notes about the update
     */
    public void updateAssignment(Long assignedBy, String notes) {
        this.assignedBy = assignedBy;
        this.notes = notes;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Get the role name
     * 
     * @return The role name
     */
    public String getRoleName() {
        return role != null ? role.getName() : null;
    }
    
    /**
     * Get the role code
     * 
     * @return The role code
     */
    public String getRoleCode() {
        return role != null ? role.getCode() : null;
    }
    
    /**
     * Get the user email
     * 
     * @return The user email
     */
    public String getUserEmail() {
        return user != null ? user.getEmail() : null;
    }
    
    /**
     * Get the user full name
     * 
     * @return The user full name
     */
    public String getUserFullName() {
        if (user != null) {
            return user.getFirstName() + " " + user.getLastName();
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", user=" + (user != null ? user.getEmail() : "null") +
                ", role=" + (role != null ? role.getName() : "null") +
                ", isActive=" + isActive +
                ", isPrimary=" + isPrimary +
                ", assignedAt=" + assignedAt +
                '}';
    }
}
