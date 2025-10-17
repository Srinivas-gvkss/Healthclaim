package com.gvkss.patil.patient_service.repository;

import com.gvkss.patil.patient_service.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Patient Repository Interface
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    /**
     * Find patient by user ID
     */
    Optional<Patient> findByUserId(Long userId);
    
    /**
     * Find patient by patient number
     */
    Optional<Patient> findByPatientNumber(String patientNumber);
    
    /**
     * Find all patients by status
     */
    Page<Patient> findByStatus(Patient.PatientStatus status, Pageable pageable);
    
    /**
     * Find all patients by primary doctor ID
     */
    Page<Patient> findByPrimaryDoctorId(Long doctorId, Pageable pageable);
    
    /**
     * Find patients by gender
     */
    Page<Patient> findByGender(Patient.Gender gender, Pageable pageable);
    
    /**
     * Find patients by blood type
     */
    Page<Patient> findByBloodType(String bloodType, Pageable pageable);
    
    /**
     * Find patients by date of birth range
     */
    Page<Patient> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Count patients by status
     */
    long countByStatus(Patient.PatientStatus status);
    
    /**
     * Count patients by primary doctor ID
     */
    long countByPrimaryDoctorId(Long doctorId);
    
    /**
     * Find patients with emergency contacts
     */
    @Query("SELECT p FROM Patient p WHERE p.emergencyContactName IS NOT NULL AND p.emergencyContactPhone IS NOT NULL")
    List<Patient> findPatientsWithEmergencyContacts();
    
    /**
     * Find patients by age range
     */
    @Query("SELECT p FROM Patient p WHERE EXTRACT(YEAR FROM AGE(p.dateOfBirth)) BETWEEN :minAge AND :maxAge")
    List<Patient> findPatientsByAgeRange(@Param("minAge") int minAge, @Param("maxAge") int maxAge);
    
    /**
     * Find patients with allergies
     */
    @Query("SELECT p FROM Patient p WHERE p.allergies IS NOT NULL AND p.allergies != ''")
    List<Patient> findPatientsWithAllergies();
    
    /**
     * Find patients with medical conditions
     */
    @Query("SELECT p FROM Patient p WHERE p.medicalConditions IS NOT NULL AND p.medicalConditions != ''")
    List<Patient> findPatientsWithMedicalConditions();
}
