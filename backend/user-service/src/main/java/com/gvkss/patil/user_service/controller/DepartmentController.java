package com.gvkss.patil.user_service.controller;

import com.gvkss.patil.user_service.dto.ApiResponse;
import com.gvkss.patil.user_service.entity.Department;
import com.gvkss.patil.user_service.enums.DepartmentType;
import com.gvkss.patil.user_service.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Department controller for handling department management operations.
 * Provides endpoints for department CRUD operations and department management.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Department Management", description = "Department management APIs")
public class DepartmentController {
    
    private final DepartmentService departmentService;
    
    /**
     * Get all departments with pagination
     * 
     * @param page Page number (0-based)
     * @param size Page size
     * @param sortBy Sort field
     * @param sortDir Sort direction (asc, desc)
     * @return Page of departments
     */
    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD')")
    @Operation(summary = "Get all departments", description = "Retrieve all departments with pagination and sorting")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Departments retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Page<Department>>> getAllDepartments(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.info("Get all departments request - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Department> departments = departmentService.getAllDepartments(pageable);
        
        return ResponseEntity.ok(ApiResponse.success(departments, "Departments retrieved successfully"));
    }
    
    /**
     * Get all active departments
     * 
     * @return List of active departments
     */
    @GetMapping("/active")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD') or hasRole('SUPPORT_AGENT')")
    @Operation(summary = "Get active departments", description = "Retrieve all active departments")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Active departments retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<List<Department>>> getActiveDepartments() {
        
        log.info("Get active departments request");
        
        List<Department> departments = departmentService.getActiveDepartments();
        
        return ResponseEntity.ok(ApiResponse.success(departments, "Active departments retrieved successfully"));
    }
    
    /**
     * Get department by ID
     * 
     * @param departmentId The department ID
     * @return Department information
     */
    @GetMapping("/{departmentId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD') or hasRole('SUPPORT_AGENT')")
    @Operation(summary = "Get department by ID", description = "Retrieve department information by department ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Department retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Department not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Department>> getDepartmentById(
            @Parameter(description = "Department ID") @PathVariable Long departmentId) {
        
        log.info("Get department by ID request: {}", departmentId);
        
        Department department = departmentService.getDepartmentById(departmentId);
        
        return ResponseEntity.ok(ApiResponse.success(department, "Department retrieved successfully"));
    }
    
    /**
     * Get department by code
     * 
     * @param departmentCode The department code
     * @return Department information
     */
    @GetMapping("/code/{departmentCode}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD') or hasRole('SUPPORT_AGENT')")
    @Operation(summary = "Get department by code", description = "Retrieve department information by department code")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Department retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Department not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Department>> getDepartmentByCode(
            @Parameter(description = "Department code") @PathVariable String departmentCode) {
        
        log.info("Get department by code request: {}", departmentCode);
        
        Department department = departmentService.getDepartmentByCode(departmentCode);
        
        return ResponseEntity.ok(ApiResponse.success(department, "Department retrieved successfully"));
    }
    
    /**
     * Get departments by type
     * 
     * @param departmentType The department type
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of departments
     */
    @GetMapping("/type/{departmentType}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD')")
    @Operation(summary = "Get departments by type", description = "Retrieve departments filtered by type")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Departments retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Page<Department>>> getDepartmentsByType(
            @Parameter(description = "Department type") @PathVariable DepartmentType departmentType,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        log.info("Get departments by type request: {}", departmentType);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Department> departments = departmentService.getDepartmentsByType(departmentType, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(departments, "Departments retrieved successfully"));
    }
    
    /**
     * Get departments by head
     * 
     * @param headId The department head ID
     * @return List of departments with the specified head
     */
    @GetMapping("/head/{headId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Get departments by head", description = "Retrieve departments filtered by head user ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Departments retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<List<Department>>> getDepartmentsByHead(
            @Parameter(description = "Head user ID") @PathVariable Long headId) {
        
        log.info("Get departments by head request: {}", headId);
        
        List<Department> departments = departmentService.getDepartmentsByHead(headId);
        
        return ResponseEntity.ok(ApiResponse.success(departments, "Departments retrieved successfully"));
    }
    
    /**
     * Get departments by location
     * 
     * @param location The location
     * @return List of departments in the specified location
     */
    @GetMapping("/location/{location}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD')")
    @Operation(summary = "Get departments by location", description = "Retrieve departments filtered by location")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Departments retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<List<Department>>> getDepartmentsByLocation(
            @Parameter(description = "Location") @PathVariable String location) {
        
        log.info("Get departments by location request: {}", location);
        
        List<Department> departments = departmentService.getDepartmentsByLocation(location);
        
        return ResponseEntity.ok(ApiResponse.success(departments, "Departments retrieved successfully"));
    }
    
    /**
     * Search departments by name
     * 
     * @param searchTerm The search term
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of departments
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD')")
    @Operation(summary = "Search departments by name", description = "Search departments by name")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Departments retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Page<Department>>> searchDepartmentsByName(
            @Parameter(description = "Search term") @RequestParam String searchTerm,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        log.info("Search departments by name request: {}", searchTerm);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Department> departments = departmentService.searchDepartmentsByName(searchTerm, pageable);
        
        return ResponseEntity.ok(ApiResponse.success(departments, "Departments retrieved successfully"));
    }
    
    /**
     * Get departments with no users
     * 
     * @return List of departments with no users
     */
    @GetMapping("/no-users")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Get departments with no users", description = "Retrieve departments that have no users assigned")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Departments retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<List<Department>>> getDepartmentsWithNoUsers() {
        
        log.info("Get departments with no users request");
        
        List<Department> departments = departmentService.getDepartmentsWithNoUsers();
        
        return ResponseEntity.ok(ApiResponse.success(departments, "Departments retrieved successfully"));
    }
    
    /**
     * Get departments with users
     * 
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of departments with users
     */
    @GetMapping("/with-users")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD')")
    @Operation(summary = "Get departments with users", description = "Retrieve departments that have users assigned")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Departments retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Page<Department>>> getDepartmentsWithUsers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        log.info("Get departments with users request");
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Department> departments = departmentService.getDepartmentsWithUsers(pageable);
        
        return ResponseEntity.ok(ApiResponse.success(departments, "Departments retrieved successfully"));
    }
    
    /**
     * Get departments with contact information
     * 
     * @return List of departments with contact information
     */
    @GetMapping("/with-contact-info")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('TEAM_LEAD')")
    @Operation(summary = "Get departments with contact info", description = "Retrieve departments that have contact information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Departments retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<List<Department>>> getDepartmentsWithContactInfo() {
        
        log.info("Get departments with contact info request");
        
        List<Department> departments = departmentService.getDepartmentsWithContactInfo();
        
        return ResponseEntity.ok(ApiResponse.success(departments, "Departments retrieved successfully"));
    }
    
    /**
     * Create a new department
     * 
     * @param department The department to create
     * @return Created department information
     */
    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Create department", description = "Create a new department")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Department created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Department already exists"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Department>> createDepartment(
            @Valid @RequestBody Department department) {
        
        log.info("Create department request: {}", department.getName());
        
        Department createdDepartment = departmentService.createDepartment(department);
        
        return ResponseEntity.status(201)
                .body(ApiResponse.created(createdDepartment, "Department created successfully"));
    }
    
    /**
     * Create a department from DepartmentType enum
     * 
     * @param departmentType The DepartmentType enum value
     * @return Created department information
     */
    @PostMapping("/from-enum/{departmentType}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Create department from enum", description = "Create a department from DepartmentType enum")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Department created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Department>> createDepartmentFromEnum(
            @Parameter(description = "Department type") @PathVariable DepartmentType departmentType) {
        
        log.info("Create department from enum request: {}", departmentType);
        
        Department createdDepartment = departmentService.createDepartmentFromEnum(departmentType);
        
        return ResponseEntity.status(201)
                .body(ApiResponse.created(createdDepartment, "Department created successfully"));
    }
    
    /**
     * Update department information
     * 
     * @param departmentId The department ID
     * @param updatedDepartment The updated department information
     * @return Updated department information
     */
    @PutMapping("/{departmentId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Update department", description = "Update department information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Department updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Department not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Department>> updateDepartment(
            @Parameter(description = "Department ID") @PathVariable Long departmentId,
            @Valid @RequestBody Department updatedDepartment) {
        
        log.info("Update department request: {}", departmentId);
        
        Department updatedDepartmentResponse = departmentService.updateDepartment(departmentId, updatedDepartment);
        
        return ResponseEntity.ok(ApiResponse.success(updatedDepartmentResponse, "Department updated successfully"));
    }
    
    /**
     * Delete department
     * 
     * @param departmentId The department ID
     * @return Success response
     */
    @DeleteMapping("/{departmentId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Delete department", description = "Delete department")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Department deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Department not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Cannot delete department with users assigned"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Object>> deleteDepartment(
            @Parameter(description = "Department ID") @PathVariable Long departmentId) {
        
        log.info("Delete department request: {}", departmentId);
        
        departmentService.deleteDepartment(departmentId);
        
        return ResponseEntity.ok(ApiResponse.success(null, "Department deleted successfully"));
    }
    
    /**
     * Activate department
     * 
     * @param departmentId The department ID
     * @return Updated department information
     */
    @PostMapping("/{departmentId}/activate")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Activate department", description = "Activate department")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Department activated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Department not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Department>> activateDepartment(
            @Parameter(description = "Department ID") @PathVariable Long departmentId) {
        
        log.info("Activate department request: {}", departmentId);
        
        Department activatedDepartment = departmentService.activateDepartment(departmentId);
        
        return ResponseEntity.ok(ApiResponse.success(activatedDepartment, "Department activated successfully"));
    }
    
    /**
     * Deactivate department
     * 
     * @param departmentId The department ID
     * @return Updated department information
     */
    @PostMapping("/{departmentId}/deactivate")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Deactivate department", description = "Deactivate department")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Department deactivated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Department not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Department>> deactivateDepartment(
            @Parameter(description = "Department ID") @PathVariable Long departmentId) {
        
        log.info("Deactivate department request: {}", departmentId);
        
        Department deactivatedDepartment = departmentService.deactivateDepartment(departmentId);
        
        return ResponseEntity.ok(ApiResponse.success(deactivatedDepartment, "Department deactivated successfully"));
    }
    
    /**
     * Set department head
     * 
     * @param departmentId The department ID
     * @param headAssignmentRequest The head assignment request
     * @return Updated department information
     */
    @PostMapping("/{departmentId}/head")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Set department head", description = "Set department head")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Department head set successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Department or user not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Department>> setDepartmentHead(
            @Parameter(description = "Department ID") @PathVariable Long departmentId,
            @Valid @RequestBody HeadAssignmentRequest headAssignmentRequest) {
        
        log.info("Set department head request: {} -> {}", departmentId, headAssignmentRequest.getHeadId());
        
        Department updatedDepartment = departmentService.setDepartmentHead(departmentId, headAssignmentRequest.getHeadId());
        
        return ResponseEntity.ok(ApiResponse.success(updatedDepartment, "Department head set successfully"));
    }
    
    /**
     * Remove department head
     * 
     * @param departmentId The department ID
     * @return Updated department information
     */
    @DeleteMapping("/{departmentId}/head")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Remove department head", description = "Remove department head")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Department head removed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Department not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Department>> removeDepartmentHead(
            @Parameter(description = "Department ID") @PathVariable Long departmentId) {
        
        log.info("Remove department head request: {}", departmentId);
        
        Department updatedDepartment = departmentService.removeDepartmentHead(departmentId);
        
        return ResponseEntity.ok(ApiResponse.success(updatedDepartment, "Department head removed successfully"));
    }
    
    /**
     * Initialize default departments
     * 
     * @return Success response
     */
    @PostMapping("/initialize")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    @Operation(summary = "Initialize default departments", description = "Initialize all system-defined departments")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Default departments initialized successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Object>> initializeDefaultDepartments() {
        
        log.info("Initialize default departments request");
        
        departmentService.initializeDefaultDepartments();
        
        return ResponseEntity.ok(ApiResponse.success(null, "Default departments initialized successfully"));
    }
    
    /**
     * Get department statistics
     * 
     * @return Department statistics
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('DEPARTMENT_HEAD')")
    @Operation(summary = "Get department statistics", description = "Get department statistics and counts")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Access denied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<DepartmentService.DepartmentStatistics>> getDepartmentStatistics() {
        
        log.info("Get department statistics request");
        
        DepartmentService.DepartmentStatistics statistics = departmentService.getDepartmentStatistics();
        
        return ResponseEntity.ok(ApiResponse.success(statistics, "Statistics retrieved successfully"));
    }
    
    /**
     * Head assignment request DTO
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class HeadAssignmentRequest {
        @jakarta.validation.constraints.NotNull(message = "Head user ID is required")
        private Long headId;
    }
}
