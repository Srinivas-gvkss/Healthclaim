package com.gvkss.patil.user_service.repository;

import com.gvkss.patil.user_service.entity.User;
import com.gvkss.patil.user_service.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * Provides methods for user data access and custom queries.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by email address
     * 
     * @param email The email address
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find user by username
     * 
     * @param username The username
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email or username
     * 
     * @param email The email address
     * @param username The username
     * @return Optional containing the user if found
     */
    @Query("SELECT u FROM User u WHERE u.email = :email OR u.username = :username")
    Optional<User> findByEmailOrUsername(@Param("email") String email, @Param("username") String username);
    
    /**
     * Find user by email or username with all related data loaded (optimized for authentication)
     * This query uses JOIN FETCH to load user roles, roles, and department in a single query
     * to avoid N+1 query problems during authentication.
     * 
     * @param email The email address
     * @param username The username
     * @return Optional containing the user if found with all related data loaded
     */
    @Query("SELECT DISTINCT u FROM User u " +
           "LEFT JOIN FETCH u.userRoles ur " +
           "LEFT JOIN FETCH ur.role r " +
           "LEFT JOIN FETCH u.department d " +
           "WHERE u.email = :email OR u.username = :username")
    Optional<User> findByEmailOrUsernameWithRolesAndDepartment(@Param("email") String email, @Param("username") String username);
    
    /**
     * Check if user exists by email
     * 
     * @param email The email address
     * @return true if user exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if user exists by username
     * 
     * @param username The username
     * @return true if user exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Find users by status
     * 
     * @param status The user status
     * @return List of users with the specified status
     */
    List<User> findByStatus(UserStatus status);
    
    /**
     * Find users by status with pagination
     * 
     * @param status The user status
     * @param pageable Pagination information
     * @return Page of users with the specified status
     */
    Page<User> findByStatus(UserStatus status, Pageable pageable);
    
    /**
     * Find users by department ID
     * 
     * @param departmentId The department ID
     * @return List of users in the specified department
     */
    List<User> findByDepartmentId(Long departmentId);
    
    /**
     * Find users by department ID with pagination
     * 
     * @param departmentId The department ID
     * @param pageable Pagination information
     * @return Page of users in the specified department
     */
    Page<User> findByDepartmentId(Long departmentId, Pageable pageable);
    
    /**
     * Find active users
     * 
     * @return List of active users
     */
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' AND u.enabled = true AND u.accountNonLocked = true")
    List<User> findActiveUsers();
    
    /**
     * Find active users with pagination
     * 
     * @param pageable Pagination information
     * @return Page of active users
     */
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' AND u.enabled = true AND u.accountNonLocked = true")
    Page<User> findActiveUsers(Pageable pageable);
    
    /**
     * Find users by role code
     * 
     * @param roleCode The role code
     * @return List of users with the specified role
     */
    @Query("SELECT u FROM User u JOIN u.userRoles ur JOIN ur.role r WHERE r.code = :roleCode AND ur.isActive = true")
    List<User> findByRoleCode(@Param("roleCode") String roleCode);
    
    /**
     * Find users by role code with pagination
     * 
     * @param roleCode The role code
     * @param pageable Pagination information
     * @return Page of users with the specified role
     */
    @Query("SELECT u FROM User u JOIN u.userRoles ur JOIN ur.role r WHERE r.code = :roleCode AND ur.isActive = true")
    Page<User> findByRoleCode(@Param("roleCode") String roleCode, Pageable pageable);
    
    /**
     * Find users by multiple role codes
     * 
     * @param roleCodes The role codes
     * @return List of users with any of the specified roles
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.userRoles ur JOIN ur.role r WHERE r.code IN :roleCodes AND ur.isActive = true")
    List<User> findByRoleCodes(@Param("roleCodes") List<String> roleCodes);
    
    /**
     * Find users by multiple role codes with pagination
     * 
     * @param roleCodes The role codes
     * @param pageable Pagination information
     * @return Page of users with any of the specified roles
     */
    @Query("SELECT DISTINCT u FROM User u JOIN u.userRoles ur JOIN ur.role r WHERE r.code IN :roleCodes AND ur.isActive = true")
    Page<User> findByRoleCodes(@Param("roleCodes") List<String> roleCodes, Pageable pageable);
    
    /**
     * Find users by name (first name or last name contains the search term)
     * 
     * @param searchTerm The search term
     * @return List of users matching the search term
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> findByNameContaining(@Param("searchTerm") String searchTerm);
    
    /**
     * Find users by name with pagination
     * 
     * @param searchTerm The search term
     * @param pageable Pagination information
     * @return Page of users matching the search term
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findByNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    /**
     * Find users created after a specific date
     * 
     * @param date The date
     * @return List of users created after the date
     */
    List<User> findByCreatedAtAfter(LocalDateTime date);
    
    /**
     * Find users created between two dates
     * 
     * @param startDate The start date
     * @param endDate The end date
     * @return List of users created between the dates
     */
    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find users who last logged in after a specific date
     * 
     * @param date The date
     * @return List of users who last logged in after the date
     */
    List<User> findByLastLoginAtAfter(LocalDateTime date);
    
    /**
     * Find users who haven't logged in for a specific period
     * 
     * @param date The date
     * @return List of users who haven't logged in since the date
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginAt IS NULL OR u.lastLoginAt < :date")
    List<User> findUsersNotLoggedInSince(@Param("date") LocalDateTime date);
    
    /**
     * Count users by status
     * 
     * @param status The user status
     * @return Number of users with the specified status
     */
    long countByStatus(UserStatus status);
    
    /**
     * Count users by department
     * 
     * @param departmentId The department ID
     * @return Number of users in the specified department
     */
    long countByDepartmentId(Long departmentId);
    
    /**
     * Count active users
     * 
     * @return Number of active users
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = 'ACTIVE' AND u.enabled = true AND u.accountNonLocked = true")
    long countActiveUsers();
    
    /**
     * Find users with failed login attempts greater than a threshold
     * 
     * @param threshold The threshold
     * @return List of users with failed login attempts above the threshold
     */
    List<User> findByFailedLoginAttemptsGreaterThan(Integer threshold);
    
    /**
     * Find locked users
     * 
     * @return List of locked users
     */
    @Query("SELECT u FROM User u WHERE u.accountNonLocked = false")
    List<User> findLockedUsers();
    
    /**
     * Find users by email domain
     * 
     * @param domain The email domain
     * @return List of users with the specified email domain
     */
    @Query("SELECT u FROM User u WHERE u.email LIKE CONCAT('%@', :domain)")
    List<User> findByEmailDomain(@Param("domain") String domain);
}
