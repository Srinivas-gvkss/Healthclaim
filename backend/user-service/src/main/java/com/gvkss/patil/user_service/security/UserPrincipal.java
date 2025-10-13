package com.gvkss.patil.user_service.security;

import com.gvkss.patil.user_service.entity.User;
import com.gvkss.patil.user_service.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserPrincipal class implementing UserDetails for Spring Security.
 * Represents the authenticated user with their roles and permissions.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    
    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private List<String> roles;
    private Collection<? extends GrantedAuthority> authorities;
    private Long departmentId;
    private String departmentName;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    
    /**
     * Create UserPrincipal from User entity
     * 
     * @param user The user entity
     * @return UserPrincipal object
     */
    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = user.getActiveRoles().stream()
                .map(userRole -> {
                    String roleCode = userRole.getRoleCode();
                    return new SimpleGrantedAuthority("ROLE_" + roleCode);
                })
                .collect(Collectors.toList());
        
        // Add role-based permissions for healthcare system
        user.getActiveRoles().forEach(userRole -> {
            String roleCode = userRole.getRoleCode();
            switch (roleCode) {
                case "ADMIN":
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_USER_MANAGEMENT"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_ROLE_MANAGEMENT"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_DEPARTMENT_MANAGEMENT"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_SYSTEM_CONFIG"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_ALL_REPORTS"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_VIEW_ALL_CLAIMS"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_MANAGE_POLICIES"));
                    break;
                case "INSURANCE_PROVIDER":
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_REVIEW_CLAIMS"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_APPROVE_CLAIMS"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_REJECT_CLAIMS"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_ACCESS_REPORTS"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_MANAGE_POLICIES"));
                    break;
                case "DOCTOR":
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_VERIFY_CLAIMS"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_VIEW_PATIENT_CLAIMS"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_UPLOAD_MEDICAL_RECORDS"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_PROVIDE_MEDICAL_OPINION"));
                    break;
                case "PATIENT":
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_SUBMIT_CLAIMS"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_VIEW_OWN_CLAIMS"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_UPLOAD_DOCUMENTS"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_VIEW_CLAIM_STATUS"));
                    authorities.add(new SimpleGrantedAuthority("PERMISSION_UPDATE_PROFILE"));
                    break;
            }
        });
        
        List<String> roles = user.getActiveRoles().stream()
                .map(UserRole::getRoleCode)
                .collect(Collectors.toList());
        
        return UserPrincipal.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .roles(roles)
                .authorities(authorities)
                .departmentId(user.getDepartment() != null ? user.getDepartment().getId() : null)
                .departmentName(user.getDepartmentName())
                .enabled(user.getEnabled())
                .accountNonExpired(user.getCredentialsNonExpired())
                .accountNonLocked(user.getAccountNonLocked())
                .credentialsNonExpired(user.getCredentialsNonExpired())
                .build();
    }
    
    /**
     * Get the user's full name
     * 
     * @return The user's full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    /**
     * Get the user's display name
     * 
     * @return The user's display name
     */
    public String getDisplayName() {
        return getFullName();
    }
    
    /**
     * Check if the user has a specific role
     * 
     * @param role The role to check
     * @return true if the user has the role
     */
    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }
    
    /**
     * Check if the user has any of the specified roles
     * 
     * @param rolesToCheck The roles to check
     * @return true if the user has any of the roles
     */
    public boolean hasAnyRole(String... rolesToCheck) {
        if (roles == null) {
            return false;
        }
        for (String role : rolesToCheck) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if the user has a specific permission
     * 
     * @param permission The permission to check
     * @return true if the user has the permission
     */
    public boolean hasPermission(String permission) {
        return authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals(permission));
    }
    
    /**
     * Check if the user has any of the specified permissions
     * 
     * @param permissions The permissions to check
     * @return true if the user has any of the permissions
     */
    public boolean hasAnyPermission(String... permissions) {
        for (String permission : permissions) {
            if (hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if the user is an administrator
     * 
     * @return true if the user is an administrator
     */
    public boolean isAdmin() {
        return hasRole("ADMIN");
    }
    
    /**
     * Check if the user is an insurance provider
     * 
     * @return true if the user is an insurance provider
     */
    public boolean isInsuranceProvider() {
        return hasRole("INSURANCE_PROVIDER");
    }
    
    /**
     * Check if the user is a doctor
     * 
     * @return true if the user is a doctor
     */
    public boolean isDoctor() {
        return hasRole("DOCTOR");
    }
    
    /**
     * Check if the user is a patient
     * 
     * @return true if the user is a patient
     */
    public boolean isPatient() {
        return hasRole("PATIENT");
    }
    
    /**
     * Check if the user has administrative privileges
     * 
     * @return true if the user has administrative privileges
     */
    public boolean hasAdminPrivileges() {
        return hasAnyRole("ADMIN", "INSURANCE_PROVIDER");
    }
    
    /**
     * Check if the user can process claims
     * 
     * @return true if the user can process claims
     */
    public boolean canProcessClaims() {
        return hasAnyRole("INSURANCE_PROVIDER", "ADMIN");
    }
    
    /**
     * Check if the user is a healthcare provider
     * 
     * @return true if the user is a healthcare provider
     */
    public boolean isHealthcareProvider() {
        return hasRole("DOCTOR");
    }
    
    /**
     * Get the user's primary role
     * 
     * @return The primary role, or null if no primary role is set
     */
    public String getPrimaryRole() {
        if (roles == null || roles.isEmpty()) {
            return null;
        }
        return roles.get(0); // First role is considered primary
    }
    
    /**
     * Get the user's access level based on their highest role
     * 
     * @return The access level (1-4, where 4 is highest)
     */
    public int getAccessLevel() {
        if (roles == null || roles.isEmpty()) {
            return 1; // Default to lowest access level
        }
        
        // Return the highest access level based on roles
        if (roles.contains("INSURANCE_PROVIDER")) return 4;
        if (roles.contains("ADMIN")) return 3;
        if (roles.contains("DOCTOR")) return 2;
        if (roles.contains("PATIENT")) return 1;
        
        return 1; // Default to lowest access level
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return email; // Use email as username for authentication
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public String toString() {
        return "UserPrincipal{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", roles=" + roles +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", enabled=" + enabled +
                ", accountNonLocked=" + accountNonLocked +
                '}';
    }
}
