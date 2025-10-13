package com.gvkss.patil.user_service.config;

import com.gvkss.patil.user_service.entity.Department;
import com.gvkss.patil.user_service.entity.Role;
import com.gvkss.patil.user_service.enums.DepartmentType;
import com.gvkss.patil.user_service.enums.UserRole;
import com.gvkss.patil.user_service.repository.DepartmentRepository;
import com.gvkss.patil.user_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Data initializer that runs on application startup to create default data.
 * This ensures that essential roles and departments exist in the database.
 * 
 * DISABLED: Using SQL migrations (V2, V3) instead to avoid duplicate data insertion.
 * Updated to align with frontend role definitions.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
// @Component  // Disabled - using SQL migrations instead (V2, V3)
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");
        
        initializeRoles();
        initializeDepartments();
        
        log.info("Data initialization completed successfully");
    }
    
    /**
     * Initialize default healthcare roles if they don't exist
     * Updated to match frontend role definitions
     */
    private void initializeRoles() {
        log.info("Initializing default healthcare roles...");
        
        // Check if roles already exist
        if (roleRepository.count() > 0) {
            log.info("Roles already exist, skipping role initialization");
            return;
        }
        
        // Create default healthcare roles (aligned with frontend)
        Role patient = Role.builder()
                .name("Patient")
                .code("PATIENT")
                .description("Can submit and track personal insurance claims")
                .roleType(UserRole.PATIENT)
                .accessLevel(1)
                .isActive(true)
                .isSystemDefined(true)
                .permissions("[\"SUBMIT_CLAIMS\", \"VIEW_OWN_CLAIMS\", \"UPLOAD_DOCUMENTS\", \"UPDATE_PROFILE\", \"VIEW_CLAIM_STATUS\"]")
                .build();
        
        Role doctor = Role.builder()
                .name("Doctor")
                .code("DOCTOR")
                .description("Healthcare provider who can verify and support patient claims")
                .roleType(UserRole.DOCTOR)
                .accessLevel(2)
                .isActive(true)
                .isSystemDefined(true)
                .permissions("[\"VERIFY_CLAIMS\", \"VIEW_PATIENT_CLAIMS\", \"UPLOAD_MEDICAL_RECORDS\", \"PROVIDE_MEDICAL_OPINION\"]")
                .build();
        
        Role admin = Role.builder()
                .name("Admin")
                .code("ADMIN")
                .description("Platform administrator with management capabilities")
                .roleType(UserRole.ADMIN)
                .accessLevel(3)
                .isActive(true)
                .isSystemDefined(true)
                .permissions("[\"USER_MANAGEMENT\", \"ROLE_MANAGEMENT\", \"DEPARTMENT_MANAGEMENT\", \"SYSTEM_CONFIG\", \"ALL_REPORTS\", \"VIEW_ALL_CLAIMS\", \"MANAGE_POLICIES\"]")
                .build();
        
        Role insuranceProvider = Role.builder()
                .name("Insurance Provider")
                .code("INSURANCE_PROVIDER")
                .description("Reviews and approves insurance claims")
                .roleType(UserRole.INSURANCE_PROVIDER)
                .accessLevel(4)
                .isActive(true)
                .isSystemDefined(true)
                .permissions("[\"REVIEW_CLAIMS\", \"APPROVE_CLAIMS\", \"REJECT_CLAIMS\", \"ACCESS_REPORTS\", \"MANAGE_POLICIES\"]")
                .build();
        
        // Save roles
        roleRepository.save(patient);
        roleRepository.save(doctor);
        roleRepository.save(admin);
        roleRepository.save(insuranceProvider);
        
        log.info("Default healthcare roles initialized successfully");
    }
    
    /**
     * Initialize default departments if they don't exist
     * Updated to simplified organizational structure
     */
    private void initializeDepartments() {
        log.info("Initializing default departments...");
        
        // Check if departments already exist
        if (departmentRepository.count() > 0) {
            log.info("Departments already exist, skipping department initialization");
            return;
        }
        
        // Create default departments (simplified)
        Department general = Department.builder()
                .name("General")
                .code("GENERAL")
                .description("General operations and services")
                .departmentType(DepartmentType.GENERAL)
                .isActive(true)
                .contactEmail("general@healthcare.com")
                .contactPhone("+1-555-0100")
                .location("Main Office")
                .build();
        
        Department administration = Department.builder()
                .name("Administration")
                .code("ADMINISTRATION")
                .description("Administrative and management operations")
                .departmentType(DepartmentType.ADMINISTRATION)
                .isActive(true)
                .contactEmail("admin@healthcare.com")
                .contactPhone("+1-555-0101")
                .location("Admin Wing")
                .build();
        
        Department claimsProcessing = Department.builder()
                .name("Claims Processing")
                .code("CLAIMS_PROCESSING")
                .description("Insurance claims processing and review")
                .departmentType(DepartmentType.CLAIMS_PROCESSING)
                .isActive(true)
                .contactEmail("claims@healthcare.com")
                .contactPhone("+1-555-0102")
                .location("Claims Center")
                .build();
        
        // Save departments
        departmentRepository.save(general);
        departmentRepository.save(administration);
        departmentRepository.save(claimsProcessing);
        
        log.info("Default departments initialized successfully");
    }
}
