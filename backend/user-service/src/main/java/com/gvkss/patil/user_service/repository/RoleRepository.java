package com.gvkss.patil.user_service.repository;

import com.gvkss.patil.user_service.entity.Role;
import com.gvkss.patil.user_service.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Role entity operations.
 * Provides methods for role data access and custom queries.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * Find role by name
     * 
     * @param name The role name
     * @return Optional containing the role if found
     */
    Optional<Role> findByName(String name);
    
    /**
     * Find role by code
     * 
     * @param code The role code
     * @return Optional containing the role if found
     */
    Optional<Role> findByCode(String code);
    
    /**
     * Find role by role type
     * 
     * @param roleType The role type
     * @return Optional containing the role if found
     */
    Optional<Role> findByRoleType(UserRole roleType);
    
    /**
     * Check if role exists by name
     * 
     * @param name The role name
     * @return true if role exists
     */
    boolean existsByName(String name);
    
    /**
     * Check if role exists by code
     * 
     * @param code The role code
     * @return true if role exists
     */
    boolean existsByCode(String code);
    
    /**
     * Find active roles
     * 
     * @return List of active roles
     */
    List<Role> findByIsActiveTrue();
    
    /**
     * Find active roles with pagination
     * 
     * @param pageable Pagination information
     * @return Page of active roles
     */
    Page<Role> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find system-defined roles
     * 
     * @return List of system-defined roles
     */
    List<Role> findByIsSystemDefinedTrue();
    
    /**
     * Find user-defined roles
     * 
     * @return List of user-defined roles
     */
    List<Role> findByIsSystemDefinedFalse();
    
    /**
     * Find roles by access level
     * 
     * @param accessLevel The access level
     * @return List of roles with the specified access level
     */
    List<Role> findByAccessLevel(Integer accessLevel);
    
    /**
     * Find roles with access level greater than or equal to the specified level
     * 
     * @param accessLevel The access level
     * @return List of roles with access level >= specified level
     */
    List<Role> findByAccessLevelGreaterThanEqual(Integer accessLevel);
    
    /**
     * Find roles with access level less than or equal to the specified level
     * 
     * @param accessLevel The access level
     * @return List of roles with access level <= specified level
     */
    List<Role> findByAccessLevelLessThanEqual(Integer accessLevel);
    
    /**
     * Find roles by access level range
     * 
     * @param minAccessLevel The minimum access level
     * @param maxAccessLevel The maximum access level
     * @return List of roles within the access level range
     */
    List<Role> findByAccessLevelBetween(Integer minAccessLevel, Integer maxAccessLevel);
    
    /**
     * Find roles by name containing the search term
     * 
     * @param searchTerm The search term
     * @return List of roles matching the search term
     */
    @Query("SELECT r FROM Role r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Role> findByNameContaining(@Param("searchTerm") String searchTerm);
    
    /**
     * Find roles by name containing the search term with pagination
     * 
     * @param searchTerm The search term
     * @param pageable Pagination information
     * @return Page of roles matching the search term
     */
    @Query("SELECT r FROM Role r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Role> findByNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    /**
     * Find roles by description containing the search term
     * 
     * @param searchTerm The search term
     * @return List of roles with description matching the search term
     */
    @Query("SELECT r FROM Role r WHERE LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Role> findByDescriptionContaining(@Param("searchTerm") String searchTerm);
    
    /**
     * Find roles by description containing the search term with pagination
     * 
     * @param searchTerm The search term
     * @param pageable Pagination information
     * @return Page of roles with description matching the search term
     */
    @Query("SELECT r FROM Role r WHERE LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Role> findByDescriptionContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    /**
     * Find roles with no users assigned
     * 
     * @return List of roles with no users
     */
    @Query("SELECT r FROM Role r WHERE r.userRoles IS EMPTY")
    List<Role> findRolesWithNoUsers();
    
    /**
     * Find roles with users assigned
     * 
     * @return List of roles with users
     */
    @Query("SELECT r FROM Role r WHERE r.userRoles IS NOT EMPTY")
    List<Role> findRolesWithUsers();
    
    /**
     * Count roles by access level
     * 
     * @param accessLevel The access level
     * @return Number of roles with the specified access level
     */
    long countByAccessLevel(Integer accessLevel);
    
    /**
     * Count active roles
     * 
     * @return Number of active roles
     */
    long countByIsActiveTrue();
    
    /**
     * Count system-defined roles
     * 
     * @return Number of system-defined roles
     */
    long countByIsSystemDefinedTrue();
    
    /**
     * Count user-defined roles
     * 
     * @return Number of user-defined roles
     */
    long countByIsSystemDefinedFalse();
    
    /**
     * Find roles ordered by access level ascending
     * 
     * @return List of roles ordered by access level
     */
    List<Role> findAllByOrderByAccessLevelAsc();
    
    /**
     * Find roles ordered by access level descending
     * 
     * @return List of roles ordered by access level
     */
    List<Role> findAllByOrderByAccessLevelDesc();
    
    /**
     * Find roles ordered by name
     * 
     * @return List of roles ordered by name
     */
    List<Role> findAllByOrderByNameAsc();
    
    /**
     * Find roles by multiple role types
     * 
     * @param roleTypes The role types
     * @return List of roles with any of the specified role types
     */
    List<Role> findByRoleTypeIn(List<UserRole> roleTypes);
    
    /**
     * Find roles by multiple role types with pagination
     * 
     * @param roleTypes The role types
     * @param pageable Pagination information
     * @return Page of roles with any of the specified role types
     */
    Page<Role> findByRoleTypeIn(List<UserRole> roleTypes, Pageable pageable);
}
