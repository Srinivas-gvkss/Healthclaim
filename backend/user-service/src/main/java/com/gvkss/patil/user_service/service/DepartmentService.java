package com.gvkss.patil.user_service.service;

import com.gvkss.patil.user_service.entity.Department;
import com.gvkss.patil.user_service.entity.User;
import com.gvkss.patil.user_service.enums.DepartmentType;
import com.gvkss.patil.user_service.repository.DepartmentRepository;
import com.gvkss.patil.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Department service for managing department operations.
 * Provides methods for department CRUD operations and department management.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    
    /**
     * Get all departments with pagination
     * 
     * @param pageable Pagination information
     * @return Page of departments
     */
    @Transactional(readOnly = true)
    public Page<Department> getAllDepartments(Pageable pageable) {
        log.info("Fetching all departments with pagination: {}", pageable);
        
        return departmentRepository.findAll(pageable);
    }
    
    /**
     * Get all active departments
     * 
     * @return List of active departments
     */
    @Transactional(readOnly = true)
    public List<Department> getActiveDepartments() {
        log.info("Fetching all active departments");
        
        return departmentRepository.findByIsActiveTrue();
    }
    
    /**
     * Get department by ID
     * 
     * @param departmentId The department ID
     * @return Department entity
     */
    @Transactional(readOnly = true)
    public Department getDepartmentById(Long departmentId) {
        log.info("Fetching department by ID: {}", departmentId);
        
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + departmentId));
    }
    
    /**
     * Get department by code
     * 
     * @param departmentCode The department code
     * @return Department entity
     */
    @Transactional(readOnly = true)
    public Department getDepartmentByCode(String departmentCode) {
        log.info("Fetching department by code: {}", departmentCode);
        
        return departmentRepository.findByCode(departmentCode)
                .orElseThrow(() -> new RuntimeException("Department not found with code: " + departmentCode));
    }
    
    /**
     * Get department by name
     * 
     * @param departmentName The department name
     * @return Department entity
     */
    @Transactional(readOnly = true)
    public Department getDepartmentByName(String departmentName) {
        log.info("Fetching department by name: {}", departmentName);
        
        return departmentRepository.findByName(departmentName)
                .orElseThrow(() -> new RuntimeException("Department not found with name: " + departmentName));
    }
    
    /**
     * Get departments by type
     * 
     * @param departmentType The department type
     * @param pageable Pagination information
     * @return Page of departments
     */
    @Transactional(readOnly = true)
    public Page<Department> getDepartmentsByType(DepartmentType departmentType, Pageable pageable) {
        log.info("Fetching departments by type: {}", departmentType);
        
        return departmentRepository.findByDepartmentType(departmentType, pageable);
    }
    
    /**
     * Get departments by head ID
     * 
     * @param headId The department head ID
     * @return List of departments with the specified head
     */
    @Transactional(readOnly = true)
    public List<Department> getDepartmentsByHead(Long headId) {
        log.info("Fetching departments by head ID: {}", headId);
        
        return departmentRepository.findByHeadId(headId);
    }
    
    /**
     * Get departments by location
     * 
     * @param location The location
     * @return List of departments in the specified location
     */
    @Transactional(readOnly = true)
    public List<Department> getDepartmentsByLocation(String location) {
        log.info("Fetching departments by location: {}", location);
        
        return departmentRepository.findByLocation(location);
    }
    
    /**
     * Search departments by name
     * 
     * @param searchTerm The search term
     * @param pageable Pagination information
     * @return Page of departments
     */
    @Transactional(readOnly = true)
    public Page<Department> searchDepartmentsByName(String searchTerm, Pageable pageable) {
        log.info("Searching departments by name: {}", searchTerm);
        
        return departmentRepository.findByNameContaining(searchTerm, pageable);
    }
    
    /**
     * Search departments by description
     * 
     * @param searchTerm The search term
     * @param pageable Pagination information
     * @return Page of departments
     */
    @Transactional(readOnly = true)
    public Page<Department> searchDepartmentsByDescription(String searchTerm, Pageable pageable) {
        log.info("Searching departments by description: {}", searchTerm);
        
        return departmentRepository.findByDescriptionContaining(searchTerm, pageable);
    }
    
    /**
     * Search departments by location
     * 
     * @param searchTerm The search term
     * @return List of departments with location matching the search term
     */
    @Transactional(readOnly = true)
    public List<Department> searchDepartmentsByLocation(String searchTerm) {
        log.info("Searching departments by location: {}", searchTerm);
        
        return departmentRepository.findByLocationContaining(searchTerm);
    }
    
    /**
     * Get departments with no users
     * 
     * @return List of departments with no users
     */
    @Transactional(readOnly = true)
    public List<Department> getDepartmentsWithNoUsers() {
        log.info("Fetching departments with no users");
        
        return departmentRepository.findDepartmentsWithNoUsers();
    }
    
    /**
     * Get departments with users
     * 
     * @param pageable Pagination information
     * @return Page of departments with users
     */
    @Transactional(readOnly = true)
    public Page<Department> getDepartmentsWithUsers(Pageable pageable) {
        log.info("Fetching departments with users");
        
        return departmentRepository.findDepartmentsWithUsers(pageable);
    }
    
    /**
     * Get departments with contact information
     * 
     * @return List of departments with contact information
     */
    @Transactional(readOnly = true)
    public List<Department> getDepartmentsWithContactInfo() {
        log.info("Fetching departments with contact information");
        
        return departmentRepository.findDepartmentsWithContactInfo();
    }
    
    /**
     * Get departments without contact information
     * 
     * @return List of departments without contact information
     */
    @Transactional(readOnly = true)
    public List<Department> getDepartmentsWithoutContactInfo() {
        log.info("Fetching departments without contact information");
        
        return departmentRepository.findDepartmentsWithoutContactInfo();
    }
    
    /**
     * Create a new department
     * 
     * @param department The department to create
     * @return Created department
     */
    public Department createDepartment(Department department) {
        log.info("Creating new department: {}", department.getName());
        
        // Validate department code uniqueness
        if (departmentRepository.existsByCode(department.getCode())) {
            throw new RuntimeException("Department code is already in use: " + department.getCode());
        }
        
        // Validate department name uniqueness
        if (departmentRepository.existsByName(department.getName())) {
            throw new RuntimeException("Department name is already in use: " + department.getName());
        }
        
        // Set default values
        if (department.getIsActive() == null) {
            department.setIsActive(true);
        }
        
        Department savedDepartment = departmentRepository.save(department);
        log.info("Department created successfully: {}", savedDepartment.getName());
        
        return savedDepartment;
    }
    
    /**
     * Create a department from DepartmentType enum
     * 
     * @param departmentType The DepartmentType enum value
     * @return Created department
     */
    public Department createDepartmentFromEnum(DepartmentType departmentType) {
        log.info("Creating department from enum: {}", departmentType);
        
        // Check if department already exists
        if (departmentRepository.existsByCode(departmentType.getCode())) {
            return departmentRepository.findByCode(departmentType.getCode()).orElse(null);
        }
        
        Department department = Department.builder()
                .name(departmentType.getDisplayName())
                .code(departmentType.getCode())
                .description(departmentType.getDescription())
                .departmentType(departmentType)
                .isActive(true)
                .build();
        
        Department savedDepartment = departmentRepository.save(department);
        log.info("Department created from enum successfully: {}", savedDepartment.getName());
        
        return savedDepartment;
    }
    
    /**
     * Update department information
     * 
     * @param departmentId The department ID
     * @param updatedDepartment The updated department information
     * @return Updated department
     */
    public Department updateDepartment(Long departmentId, Department updatedDepartment) {
        log.info("Updating department: {}", departmentId);
        
        Department existingDepartment = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + departmentId));
        
        // Update fields
        if (updatedDepartment.getName() != null) {
            // Validate name uniqueness
            if (!existingDepartment.getName().equals(updatedDepartment.getName()) && 
                departmentRepository.existsByName(updatedDepartment.getName())) {
                throw new RuntimeException("Department name is already in use: " + updatedDepartment.getName());
            }
            existingDepartment.setName(updatedDepartment.getName());
        }
        
        if (updatedDepartment.getDescription() != null) {
            existingDepartment.setDescription(updatedDepartment.getDescription());
        }
        
        if (updatedDepartment.getDepartmentType() != null) {
            existingDepartment.setDepartmentType(updatedDepartment.getDepartmentType());
        }
        
        if (updatedDepartment.getHeadId() != null) {
            // Validate head user exists
            if (!userRepository.existsById(updatedDepartment.getHeadId())) {
                throw new RuntimeException("Department head not found with ID: " + updatedDepartment.getHeadId());
            }
            existingDepartment.setHeadId(updatedDepartment.getHeadId());
        }
        
        if (updatedDepartment.getContactEmail() != null) {
            existingDepartment.setContactEmail(updatedDepartment.getContactEmail());
        }
        
        if (updatedDepartment.getContactPhone() != null) {
            existingDepartment.setContactPhone(updatedDepartment.getContactPhone());
        }
        
        if (updatedDepartment.getLocation() != null) {
            existingDepartment.setLocation(updatedDepartment.getLocation());
        }
        
        Department savedDepartment = departmentRepository.save(existingDepartment);
        log.info("Department updated successfully: {}", savedDepartment.getName());
        
        return savedDepartment;
    }
    
    /**
     * Delete department
     * 
     * @param departmentId The department ID
     */
    public void deleteDepartment(Long departmentId) {
        log.info("Deleting department: {}", departmentId);
        
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + departmentId));
        
        // Check if department has users assigned
        if (userRepository.countByDepartmentId(departmentId) > 0) {
            throw new RuntimeException("Cannot delete department with users assigned: " + department.getName());
        }
        
        departmentRepository.delete(department);
        log.info("Department deleted successfully: {}", department.getName());
    }
    
    /**
     * Activate department
     * 
     * @param departmentId The department ID
     * @return Updated department
     */
    public Department activateDepartment(Long departmentId) {
        log.info("Activating department: {}", departmentId);
        
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + departmentId));
        
        department.activate();
        Department savedDepartment = departmentRepository.save(department);
        
        log.info("Department activated successfully: {}", savedDepartment.getName());
        return savedDepartment;
    }
    
    /**
     * Deactivate department
     * 
     * @param departmentId The department ID
     * @return Updated department
     */
    public Department deactivateDepartment(Long departmentId) {
        log.info("Deactivating department: {}", departmentId);
        
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + departmentId));
        
        department.deactivate();
        Department savedDepartment = departmentRepository.save(department);
        
        log.info("Department deactivated successfully: {}", savedDepartment.getName());
        return savedDepartment;
    }
    
    /**
     * Set department head
     * 
     * @param departmentId The department ID
     * @param headId The head user ID
     * @return Updated department
     */
    public Department setDepartmentHead(Long departmentId, Long headId) {
        log.info("Setting department head: {} for department: {}", headId, departmentId);
        
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + departmentId));
        
        User headUser = userRepository.findById(headId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + headId));
        
        department.setHead(headUser);
        Department savedDepartment = departmentRepository.save(department);
        
        log.info("Department head set successfully: {} for department: {}", headUser.getEmail(), department.getName());
        return savedDepartment;
    }
    
    /**
     * Remove department head
     * 
     * @param departmentId The department ID
     * @return Updated department
     */
    public Department removeDepartmentHead(Long departmentId) {
        log.info("Removing department head for department: {}", departmentId);
        
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + departmentId));
        
        department.setHead(null);
        Department savedDepartment = departmentRepository.save(department);
        
        log.info("Department head removed successfully for department: {}", department.getName());
        return savedDepartment;
    }
    
    /**
     * Get department statistics
     * 
     * @return Department statistics
     */
    @Transactional(readOnly = true)
    public DepartmentStatistics getDepartmentStatistics() {
        log.info("Fetching department statistics");
        
        long totalDepartments = departmentRepository.count();
        long activeDepartments = departmentRepository.countByIsActiveTrue();
        
        return DepartmentStatistics.builder()
                .totalDepartments(totalDepartments)
                .activeDepartments(activeDepartments)
                .inactiveDepartments(totalDepartments - activeDepartments)
                .build();
    }
    
    /**
     * Initialize default departments
     * Creates all system-defined departments if they don't exist
     */
    public void initializeDefaultDepartments() {
        log.info("Initializing default departments");
        
        for (DepartmentType departmentType : DepartmentType.values()) {
            if (!departmentRepository.existsByCode(departmentType.getCode())) {
                createDepartmentFromEnum(departmentType);
            }
        }
        
        log.info("Default departments initialization completed");
    }
    
    /**
     * Department statistics class
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class DepartmentStatistics {
        private long totalDepartments;
        private long activeDepartments;
        private long inactiveDepartments;
    }
}
