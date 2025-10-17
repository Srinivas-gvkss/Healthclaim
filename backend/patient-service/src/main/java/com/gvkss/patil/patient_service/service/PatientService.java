package com.gvkss.patil.patient_service.service;

import com.gvkss.patil.patient_service.dto.*;
import com.gvkss.patil.patient_service.entity.Patient;
import com.gvkss.patil.patient_service.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Patient Service Implementation
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PatientService {
    
    private final PatientRepository patientRepository;
    
    /**
     * Get patient by ID
     */
    @Transactional(readOnly = true)
    public PatientResponse getPatientById(Long id) {
        log.info("Fetching patient by ID: {}", id);
        
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));
        
        return convertToResponse(patient);
    }
    
    /**
     * Get patient by user ID
     */
    @Transactional(readOnly = true)
    public PatientResponse getPatientByUserId(Long userId) {
        log.info("Fetching patient by user ID: {}", userId);
        
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Patient not found with user ID: " + userId));
        
        return convertToResponse(patient);
    }
    
    /**
     * Get patient by patient number
     */
    @Transactional(readOnly = true)
    public PatientResponse getPatientByNumber(String patientNumber) {
        log.info("Fetching patient by number: {}", patientNumber);
        
        Patient patient = patientRepository.findByPatientNumber(patientNumber)
                .orElseThrow(() -> new RuntimeException("Patient not found with number: " + patientNumber));
        
        return convertToResponse(patient);
    }
    
    /**
     * Get all patients with pagination
     */
    @Transactional(readOnly = true)
    public Page<PatientResponse> getAllPatients(int page, int size, String sortBy, String sortDir) {
        log.info("Fetching all patients - page: {}, size: {}, sortBy: {}, sortDir: {}", page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Patient> patients = patientRepository.findAll(pageable);
        return patients.map(this::convertToResponse);
    }
    
    /**
     * Get patients by status
     */
    @Transactional(readOnly = true)
    public Page<PatientResponse> getPatientsByStatus(Patient.PatientStatus status, int page, int size, String sortBy, String sortDir) {
        log.info("Fetching patients with status: {}", status);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Patient> patients = patientRepository.findByStatus(status, pageable);
        return patients.map(this::convertToResponse);
    }
    
    /**
     * Get patients by primary doctor ID
     */
    @Transactional(readOnly = true)
    public Page<PatientResponse> getPatientsByDoctorId(Long doctorId, int page, int size, String sortBy, String sortDir) {
        log.info("Fetching patients for doctor: {}", doctorId);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Patient> patients = patientRepository.findByPrimaryDoctorId(doctorId, pageable);
        return patients.map(this::convertToResponse);
    }
    
    /**
     * Update patient information
     */
    public PatientResponse updatePatient(Long id, UpdatePatientRequest request) {
        log.info("Updating patient: {}", id);
        
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + id));
        
        // Update fields if provided
        if (request.getDateOfBirth() != null) {
            existingPatient.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            existingPatient.setGender(request.getGender());
        }
        if (request.getBloodType() != null) {
            existingPatient.setBloodType(request.getBloodType());
        }
        if (request.getHeightCm() != null) {
            existingPatient.setHeightCm(request.getHeightCm());
        }
        if (request.getWeightKg() != null) {
            existingPatient.setWeightKg(request.getWeightKg());
        }
        if (request.getAllergies() != null) {
            existingPatient.setAllergies(request.getAllergies());
        }
        if (request.getMedicalConditions() != null) {
            existingPatient.setMedicalConditions(request.getMedicalConditions());
        }
        if (request.getEmergencyContactName() != null) {
            existingPatient.setEmergencyContactName(request.getEmergencyContactName());
        }
        if (request.getEmergencyContactPhone() != null) {
            existingPatient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        }
        if (request.getEmergencyContactRelationship() != null) {
            existingPatient.setEmergencyContactRelationship(request.getEmergencyContactRelationship());
        }
        if (request.getPrimaryDoctorId() != null) {
            existingPatient.setPrimaryDoctorId(request.getPrimaryDoctorId());
        }
        if (request.getStatus() != null) {
            existingPatient.setStatus(request.getStatus());
        }
        
        Patient updatedPatient = patientRepository.save(existingPatient);
        log.info("Patient updated successfully: {}", updatedPatient.getId());
        
        return convertToResponse(updatedPatient);
    }
    
    /**
     * Create patient profile
     */
    public PatientResponse createPatient(Long userId, UpdatePatientRequest request) {
        log.info("Creating patient profile for user: {}", userId);
        
        // Check if patient already exists
        if (patientRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("Patient profile already exists for user ID: " + userId);
        }
        
        // Generate unique patient number
        String patientNumber = generatePatientNumber();
        
        // Create patient entity
        Patient patient = Patient.builder()
                .userId(userId)
                .patientNumber(patientNumber)
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bloodType(request.getBloodType())
                .heightCm(request.getHeightCm())
                .weightKg(request.getWeightKg())
                .allergies(request.getAllergies())
                .medicalConditions(request.getMedicalConditions())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .emergencyContactRelationship(request.getEmergencyContactRelationship())
                .primaryDoctorId(request.getPrimaryDoctorId())
                .status(request.getStatus() != null ? request.getStatus() : Patient.PatientStatus.ACTIVE)
                .build();
        
        Patient savedPatient = patientRepository.save(patient);
        log.info("Patient created successfully with ID: {}", savedPatient.getId());
        
        return convertToResponse(savedPatient);
    }
    
    /**
     * Get patient statistics
     */
    @Transactional(readOnly = true)
    public PatientStatisticsResponse getPatientStatistics() {
        log.info("Fetching patient statistics");
        
        long totalPatients = patientRepository.count();
        long activePatients = patientRepository.countByStatus(Patient.PatientStatus.ACTIVE);
        long inactivePatients = patientRepository.countByStatus(Patient.PatientStatus.INACTIVE);
        long patientsWithAllergies = patientRepository.findPatientsWithAllergies().size();
        long patientsWithConditions = patientRepository.findPatientsWithMedicalConditions().size();
        
        return PatientStatisticsResponse.builder()
                .totalPatients(totalPatients)
                .activePatients(activePatients)
                .inactivePatients(inactivePatients)
                .patientsWithAllergies(patientsWithAllergies)
                .patientsWithMedicalConditions(patientsWithConditions)
                .build();
    }
    
    /**
     * Generate unique patient number
     */
    private String generatePatientNumber() {
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "PAT-" + timestamp + "-" + randomSuffix;
    }
    
    /**
     * Convert entity to response DTO
     */
    private PatientResponse convertToResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .userId(patient.getUserId())
                .patientNumber(patient.getPatientNumber())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .bloodType(patient.getBloodType())
                .heightCm(patient.getHeightCm())
                .weightKg(patient.getWeightKg())
                .allergies(patient.getAllergies())
                .medicalConditions(patient.getMedicalConditions())
                .emergencyContactName(patient.getEmergencyContactName())
                .emergencyContactPhone(patient.getEmergencyContactPhone())
                .emergencyContactRelationship(patient.getEmergencyContactRelationship())
                .primaryDoctorId(patient.getPrimaryDoctorId())
                .status(patient.getStatus())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .genderDisplayName(getGenderDisplayName(patient.getGender()))
                .statusDisplayName(getStatusDisplayName(patient.getStatus()))
                .age(calculateAge(patient.getDateOfBirth()))
                .build();
    }
    
    private String getGenderDisplayName(Patient.Gender gender) {
        switch (gender) {
            case MALE: return "Male";
            case FEMALE: return "Female";
            case OTHER: return "Other";
            default: return gender.toString();
        }
    }
    
    private String getStatusDisplayName(Patient.PatientStatus status) {
        switch (status) {
            case ACTIVE: return "Active";
            case INACTIVE: return "Inactive";
            case DECEASED: return "Deceased";
            default: return status.toString();
        }
    }
    
    private Integer calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) return null;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}
