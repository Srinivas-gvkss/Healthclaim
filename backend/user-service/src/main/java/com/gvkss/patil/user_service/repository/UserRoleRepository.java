package com.gvkss.patil.user_service.repository;

import com.gvkss.patil.user_service.entity.UserRole;
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
 * Repository interface for UserRole entity operations.
 * Provides methods for user role mapping data access and custom queries.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    
    /**
     * Find user role mapping by user ID and role ID
     * 
     * @param userId The user ID
     * @param roleId The role ID
     * @return Optional containing the user role mapping if found
     */
    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);
    
    /**
     * Find user role mappings by user ID
     * 
     * @param userId The user ID
     * @return List of user role mappings for the specified user
     */
    List<UserRole> findByUserId(Long userId);
    
    /**
     * Find user role mappings by role ID
     * 
     * @param roleId The role ID
     * @return List of user role mappings for the specified role
     */
    List<UserRole> findByRoleId(Long roleId);
    
    /**
     * Find active user role mappings by user ID
     * 
     * @param userId The user ID
     * @return List of active user role mappings for the specified user
     */
    List<UserRole> findByUserIdAndIsActiveTrue(Long userId);
    
    /**
     * Find active user role mappings by role ID
     * 
     * @param roleId The role ID
     * @return List of active user role mappings for the specified role
     */
    List<UserRole> findByRoleIdAndIsActiveTrue(Long roleId);
    
    /**
     * Find primary user role mapping by user ID
     * 
     * @param userId The user ID
     * @return Optional containing the primary user role mapping if found
     */
    Optional<UserRole> findByUserIdAndIsPrimaryTrue(Long userId);
    
    /**
     * Find active user role mappings by user ID with pagination
     * 
     * @param userId The user ID
     * @param pageable Pagination information
     * @return Page of active user role mappings for the specified user
     */
    Page<UserRole> findByUserIdAndIsActiveTrue(Long userId, Pageable pageable);
    
    /**
     * Find active user role mappings by role ID with pagination
     * 
     * @param roleId The role ID
     * @param pageable Pagination information
     * @return Page of active user role mappings for the specified role
     */
    Page<UserRole> findByRoleIdAndIsActiveTrue(Long roleId, Pageable pageable);
    
    /**
     * Find user role mappings by user ID and role ID with pagination
     * 
     * @param userId The user ID
     * @param roleId The role ID
     * @param pageable Pagination information
     * @return Page of user role mappings for the specified user and role
     */
    Page<UserRole> findByUserIdAndRoleId(Long userId, Long roleId, Pageable pageable);
    
    /**
     * Find user role mappings by role code
     * 
     * @param roleCode The role code
     * @return List of user role mappings for the specified role code
     */
    @Query("SELECT ur FROM UserRole ur JOIN ur.role r WHERE r.code = :roleCode")
    List<UserRole> findByRoleCode(@Param("roleCode") String roleCode);
    
    /**
     * Find active user role mappings by role code
     * 
     * @param roleCode The role code
     * @return List of active user role mappings for the specified role code
     */
    @Query("SELECT ur FROM UserRole ur JOIN ur.role r WHERE r.code = :roleCode AND ur.isActive = true")
    List<UserRole> findByRoleCodeAndIsActiveTrue(@Param("roleCode") String roleCode);
    
    /**
     * Find user role mappings by user email
     * 
     * @param email The user email
     * @return List of user role mappings for the specified user email
     */
    @Query("SELECT ur FROM UserRole ur JOIN ur.user u WHERE u.email = :email")
    List<UserRole> findByUserEmail(@Param("email") String email);
    
    /**
     * Find active user role mappings by user email
     * 
     * @param email The user email
     * @return List of active user role mappings for the specified user email
     */
    @Query("SELECT ur FROM UserRole ur JOIN ur.user u WHERE u.email = :email AND ur.isActive = true")
    List<UserRole> findByUserEmailAndIsActiveTrue(@Param("email") String email);
    
    /**
     * Find user role mappings by multiple role codes
     * 
     * @param roleCodes The role codes
     * @return List of user role mappings for the specified role codes
     */
    @Query("SELECT ur FROM UserRole ur JOIN ur.role r WHERE r.code IN :roleCodes")
    List<UserRole> findByRoleCodes(@Param("roleCodes") List<String> roleCodes);
    
    /**
     * Find active user role mappings by multiple role codes
     * 
     * @param roleCodes The role codes
     * @return List of active user role mappings for the specified role codes
     */
    @Query("SELECT ur FROM UserRole ur JOIN ur.role r WHERE r.code IN :roleCodes AND ur.isActive = true")
    List<UserRole> findByRoleCodesAndIsActiveTrue(@Param("roleCodes") List<String> roleCodes);
    
    /**
     * Find user role mappings assigned by a specific user
     * 
     * @param assignedBy The user ID who assigned the roles
     * @return List of user role mappings assigned by the specified user
     */
    List<UserRole> findByAssignedBy(Long assignedBy);
    
    /**
     * Find user role mappings assigned after a specific date
     * 
     * @param date The date
     * @return List of user role mappings assigned after the date
     */
    List<UserRole> findByAssignedAtAfter(LocalDateTime date);
    
    /**
     * Find user role mappings assigned between two dates
     * 
     * @param startDate The start date
     * @param endDate The end date
     * @return List of user role mappings assigned between the dates
     */
    List<UserRole> findByAssignedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find user role mappings updated after a specific date
     * 
     * @param date The date
     * @return List of user role mappings updated after the date
     */
    List<UserRole> findByUpdatedAtAfter(LocalDateTime date);
    
    /**
     * Count user role mappings by user ID
     * 
     * @param userId The user ID
     * @return Number of user role mappings for the specified user
     */
    long countByUserId(Long userId);
    
    /**
     * Count active user role mappings by user ID
     * 
     * @param userId The user ID
     * @return Number of active user role mappings for the specified user
     */
    long countByUserIdAndIsActiveTrue(Long userId);
    
    /**
     * Count user role mappings by role ID
     * 
     * @param roleId The role ID
     * @return Number of user role mappings for the specified role
     */
    long countByRoleId(Long roleId);
    
    /**
     * Count active user role mappings by role ID
     * 
     * @param roleId The role ID
     * @return Number of active user role mappings for the specified role
     */
    long countByRoleIdAndIsActiveTrue(Long roleId);
    
    /**
     * Count user role mappings by role code
     * 
     * @param roleCode The role code
     * @return Number of user role mappings for the specified role code
     */
    @Query("SELECT COUNT(ur) FROM UserRole ur JOIN ur.role r WHERE r.code = :roleCode")
    long countByRoleCode(@Param("roleCode") String roleCode);
    
    /**
     * Count active user role mappings by role code
     * 
     * @param roleCode The role code
     * @return Number of active user role mappings for the specified role code
     */
    @Query("SELECT COUNT(ur) FROM UserRole ur JOIN ur.role r WHERE r.code = :roleCode AND ur.isActive = true")
    long countByRoleCodeAndIsActiveTrue(@Param("roleCode") String roleCode);
    
    /**
     * Check if user has a specific role
     * 
     * @param userId The user ID
     * @param roleId The role ID
     * @return true if the user has the role
     */
    boolean existsByUserIdAndRoleId(Long userId, Long roleId);
    
    /**
     * Check if user has an active specific role
     * 
     * @param userId The user ID
     * @param roleId The role ID
     * @return true if the user has the active role
     */
    boolean existsByUserIdAndRoleIdAndIsActiveTrue(Long userId, Long roleId);
    
    /**
     * Check if user has a specific role by role code
     * 
     * @param userId The user ID
     * @param roleCode The role code
     * @return true if the user has the role
     */
    @Query("SELECT COUNT(ur) > 0 FROM UserRole ur JOIN ur.role r WHERE ur.user.id = :userId AND r.code = :roleCode")
    boolean existsByUserIdAndRoleCode(@Param("userId") Long userId, @Param("roleCode") String roleCode);
    
    /**
     * Check if user has an active specific role by role code
     * 
     * @param userId The user ID
     * @param roleCode The role code
     * @return true if the user has the active role
     */
    @Query("SELECT COUNT(ur) > 0 FROM UserRole ur JOIN ur.role r WHERE ur.user.id = :userId AND r.code = :roleCode AND ur.isActive = true")
    boolean existsByUserIdAndRoleCodeAndIsActiveTrue(@Param("userId") Long userId, @Param("roleCode") String roleCode);
    
    /**
     * Find user role mappings ordered by assignment date
     * 
     * @return List of user role mappings ordered by assignment date
     */
    List<UserRole> findAllByOrderByAssignedAtAsc();
    
    /**
     * Find user role mappings ordered by assignment date descending
     * 
     * @return List of user role mappings ordered by assignment date
     */
    List<UserRole> findAllByOrderByAssignedAtDesc();
    
    /**
     * Find user role mappings ordered by update date
     * 
     * @return List of user role mappings ordered by update date
     */
    List<UserRole> findAllByOrderByUpdatedAtAsc();
    
    /**
     * Find user role mappings ordered by update date descending
     * 
     * @return List of user role mappings ordered by update date
     */
    List<UserRole> findAllByOrderByUpdatedAtDesc();
}
