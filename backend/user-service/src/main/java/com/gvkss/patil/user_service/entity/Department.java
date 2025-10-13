package com.gvkss.patil.user_service.entity;

import com.gvkss.patil.user_service.enums.DepartmentType;
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
 * Department entity representing organizational departments in the IT Support Ticketing System.
 * Each department has specific responsibilities and support areas.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "departments", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "name"),
           @UniqueConstraint(columnNames = "code")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {
    
    /**
     * Unique identifier for the department
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Department name (e.g., "IT Infrastructure", "Application Support")
     */
    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    /**
     * Department code (e.g., "IT_INFRA", "APP_SUPPORT")
     */
    @NotBlank(message = "Department code is required")
    @Size(min = 2, max = 50, message = "Department code must be between 2 and 50 characters")
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;
    
    /**
     * Department description
     */
    @Size(max = 500, message = "Department description must not exceed 500 characters")
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * Department type from the DepartmentType enum
     */
    @NotNull(message = "Department type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "department_type", nullable = false)
    private DepartmentType departmentType;
    
    /**
     * Department head user ID
     */
    @Column(name = "head_id")
    private Long headId;
    
    /**
     * Whether the department is active
     */
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    /**
     * Department contact email
     */
    @Size(max = 100, message = "Contact email must not exceed 100 characters")
    @Column(name = "contact_email", length = 100)
    private String contactEmail;
    
    /**
     * Department contact phone
     */
    @Size(max = 20, message = "Contact phone must not exceed 20 characters")
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;
    
    /**
     * Department location/office
     */
    @Size(max = 200, message = "Location must not exceed 200 characters")
    @Column(name = "location", length = 200)
    private String location;
    
    /**
     * Timestamp when the department was created
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the department was last updated
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    /**
     * Users belonging to this department
     */
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<User> users = new ArrayList<>();
    
    /**
     * Get the department head user
     * 
     * @return The department head user, or null if not set
     */
    public User getHead() {
        if (headId == null) {
            return null;
        }
        return users.stream()
                .filter(user -> user.getId().equals(headId))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Set the department head
     * 
     * @param head The user to set as department head
     */
    public void setHead(User head) {
        if (head != null) {
            this.headId = head.getId();
        } else {
            this.headId = null;
        }
    }
    
    /**
     * Add a user to this department
     * 
     * @param user The user to add
     */
    public void addUser(User user) {
        if (user != null && !users.contains(user)) {
            users.add(user);
            user.setDepartment(this);
        }
    }
    
    /**
     * Remove a user from this department
     * 
     * @param user The user to remove
     */
    public void removeUser(User user) {
        if (user != null && users.contains(user)) {
            users.remove(user);
            user.setDepartment(null);
        }
    }
    
    /**
     * Get the number of users in this department
     * 
     * @return The number of users
     */
    public int getUserCount() {
        return users.size();
    }
    
    /**
     * Check if the department is active
     * 
     * @return true if the department is active
     */
    public boolean isActive() {
        return isActive != null && isActive;
    }
    
    /**
     * Activate the department
     */
    public void activate() {
        this.isActive = true;
    }
    
    /**
     * Deactivate the department
     */
    public void deactivate() {
        this.isActive = false;
    }
    
    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", departmentType=" + departmentType +
                ", isActive=" + isActive +
                '}';
    }
}
