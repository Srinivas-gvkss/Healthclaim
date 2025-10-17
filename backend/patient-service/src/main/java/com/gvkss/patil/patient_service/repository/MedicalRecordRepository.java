package com.gvkss.patil.patient_service.repository;

import com.gvkss.patil.patient_service.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Medical Record Repository Interface
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    
    /**
     * Find all medical records by patient ID
     */
    Page<MedicalRecord> findByPatientId(Long patientId, Pageable pageable);
    
    /**
     * Find all medical records by doctor ID
     */
    Page<MedicalRecord> findByDoctorId(Long doctorId, Pageable pageable);
    
    /**
     * Find medical records by record type
     */
    Page<MedicalRecord> findByRecordType(String recordType, Pageable pageable);
    
    /**
     * Find medical records by status
     */
    Page<MedicalRecord> findByStatus(MedicalRecord.RecordStatus status, Pageable pageable);
    
    /**
     * Find medical records by visit date range
     */
    Page<MedicalRecord> findByVisitDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find medical records by patient ID and visit date range
     */
    Page<MedicalRecord> findByPatientIdAndVisitDateBetween(Long patientId, LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find medical records by doctor ID and visit date range
     */
    Page<MedicalRecord> findByDoctorIdAndVisitDateBetween(Long doctorId, LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find recent medical records for patient
     */
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.patientId = :patientId ORDER BY mr.visitDate DESC")
    List<MedicalRecord> findRecentMedicalRecordsByPatientId(@Param("patientId") Long patientId, Pageable pageable);
    
    /**
     * Count medical records by patient ID
     */
    long countByPatientId(Long patientId);
    
    /**
     * Count medical records by doctor ID
     */
    long countByDoctorId(Long doctorId);
    
    /**
     * Find medical records with diagnosis
     */
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.diagnosis IS NOT NULL AND mr.diagnosis != ''")
    List<MedicalRecord> findMedicalRecordsWithDiagnosis();
    
    /**
     * Find medical records by diagnosis code
     */
    List<MedicalRecord> findByDiagnosisCode(String diagnosisCode);
}
