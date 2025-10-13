package com.gvkss.patil.user_service.repository;

import com.gvkss.patil.user_service.entity.Department;
import com.gvkss.patil.user_service.enums.DepartmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Department entity operations.
 * Provides methods for department data access and custom queries.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    /**
     * Find department by name
     * 
     * @param name The department name
     * @return Optional containing the department if found
     */
    Optional<Department> findByName(String name);
    
    /**
     * Find department by code
     * 
     * @param code The department code
     * @return Optional containing the department if found
     */
    Optional<Department> findByCode(String code);
    
    
    /**
     * Check if department exists by name
     * 
     * @param name The department name
     * @return true if department exists
     */
    boolean existsByName(String name);
    
    /**
     * Check if department exists by code
     * 
     * @param code The department code
     * @return true if department exists
     */
    boolean existsByCode(String code);
    
    /**
     * Find active departments
     * 
     * @return List of active departments
     */
    List<Department> findByIsActiveTrue();
    
    /**
     * Find active departments with pagination
     * 
     * @param pageable Pagination information
     * @return Page of active departments
     */
    Page<Department> findByIsActiveTrue(Pageable pageable);
    
    /**
     * Find departments by department type
     * 
     * @param departmentType The department type
     * @return List of departments with the specified type
     */
    List<Department> findByDepartmentType(DepartmentType departmentType);
    
    /**
     * Find departments by department type with pagination
     * 
     * @param departmentType The department type
     * @param pageable Pagination information
     * @return Page of departments with the specified type
     */
    Page<Department> findByDepartmentType(DepartmentType departmentType, Pageable pageable);
    
    /**
     * Find departments by head ID
     * 
     * @param headId The department head ID
     * @return List of departments with the specified head
     */
    List<Department> findByHeadId(Long headId);
    
    /**
     * Find departments by location
     * 
     * @param location The location
     * @return List of departments in the specified location
     */
    List<Department> findByLocation(String location);
    
    /**
     * Find departments by location containing the search term
     * 
     * @param searchTerm The search term
     * @return List of departments with location matching the search term
     */
    @Query("SELECT d FROM Department d WHERE LOWER(d.location) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Department> findByLocationContaining(@Param("searchTerm") String searchTerm);
    
    /**
     * Find departments by name containing the search term
     * 
     * @param searchTerm The search term
     * @return List of departments matching the search term
     */
    @Query("SELECT d FROM Department d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Department> findByNameContaining(@Param("searchTerm") String searchTerm);
    
    /**
     * Find departments by name containing the search term with pagination
     * 
     * @param searchTerm The search term
     * @param pageable Pagination information
     * @return Page of departments matching the search term
     */
    @Query("SELECT d FROM Department d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Department> findByNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    /**
     * Find departments by description containing the search term
     * 
     * @param searchTerm The search term
     * @return List of departments with description matching the search term
     */
    @Query("SELECT d FROM Department d WHERE LOWER(d.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Department> findByDescriptionContaining(@Param("searchTerm") String searchTerm);
    
    /**
     * Find departments by description containing the search term with pagination
     * 
     * @param searchTerm The search term
     * @param pageable Pagination information
     * @return Page of departments with description matching the search term
     */
    @Query("SELECT d FROM Department d WHERE LOWER(d.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Department> findByDescriptionContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    /**
     * Find departments with no users
     * 
     * @return List of departments with no users
     */
    @Query("SELECT d FROM Department d WHERE d.users IS EMPTY")
    List<Department> findDepartmentsWithNoUsers();
    
    /**
     * Find departments with users
     * 
     * @return List of departments with users
     */
    @Query("SELECT d FROM Department d WHERE d.users IS NOT EMPTY")
    List<Department> findDepartmentsWithUsers();
    
    /**
     * Find departments with users with pagination
     * 
     * @param pageable Pagination information
     * @return Page of departments with users
     */
    @Query("SELECT d FROM Department d WHERE d.users IS NOT EMPTY")
    Page<Department> findDepartmentsWithUsers(Pageable pageable);
    
    /**
     * Count departments by department type
     * 
     * @param departmentType The department type
     * @return Number of departments with the specified type
     */
    long countByDepartmentType(DepartmentType departmentType);
    
    /**
     * Count active departments
     * 
     * @return Number of active departments
     */
    long countByIsActiveTrue();
    
    /**
     * Count departments by location
     * 
     * @param location The location
     * @return Number of departments in the specified location
     */
    long countByLocation(String location);
    
    /**
     * Find departments ordered by name
     * 
     * @return List of departments ordered by name
     */
    List<Department> findAllByOrderByNameAsc();
    
    /**
     * Find departments ordered by department type
     * 
     * @return List of departments ordered by department type
     */
    List<Department> findAllByOrderByDepartmentTypeAsc();
    
    /**
     * Find departments by multiple department types
     * 
     * @param departmentTypes The department types
     * @return List of departments with any of the specified types
     */
    List<Department> findByDepartmentTypeIn(List<DepartmentType> departmentTypes);
    
    /**
     * Find departments by multiple department types with pagination
     * 
     * @param departmentTypes The department types
     * @param pageable Pagination information
     * @return Page of departments with any of the specified types
     */
    Page<Department> findByDepartmentTypeIn(List<DepartmentType> departmentTypes, Pageable pageable);
    
    /**
     * Find departments by contact email
     * 
     * @param contactEmail The contact email
     * @return List of departments with the specified contact email
     */
    List<Department> findByContactEmail(String contactEmail);
    
    /**
     * Find departments by contact phone
     * 
     * @param contactPhone The contact phone
     * @return List of departments with the specified contact phone
     */
    List<Department> findByContactPhone(String contactPhone);
    
    /**
     * Find departments with contact information
     * 
     * @return List of departments with contact information
     */
    @Query("SELECT d FROM Department d WHERE d.contactEmail IS NOT NULL OR d.contactPhone IS NOT NULL")
    List<Department> findDepartmentsWithContactInfo();
    
    /**
     * Find departments without contact information
     * 
     * @return List of departments without contact information
     */
    @Query("SELECT d FROM Department d WHERE d.contactEmail IS NULL AND d.contactPhone IS NULL")
    List<Department> findDepartmentsWithoutContactInfo();
}
