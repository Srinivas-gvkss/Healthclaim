package com.gvkss.patil.patient_service.service;

import com.gvkss.patil.patient_service.dto.CreateMedicalRecordRequest;
import com.gvkss.patil.patient_service.dto.MedicalRecordResponse;
import com.gvkss.patil.patient_service.entity.MedicalRecord;
import com.gvkss.patil.patient_service.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Medical Record Service Implementation
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MedicalRecordService {
    
    private final MedicalRecordRepository medicalRecordRepository;
    
    /**
     * Create medical record
     */
    public MedicalRecordResponse createMedicalRecord(CreateMedicalRecordRequest request) {
        log.info("Creating medical record for patient: {}", request.getPatientId());
        
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .recordType(request.getRecordType())
                .visitDate(request.getVisitDate())
                .diagnosis(request.getDiagnosis())
                .diagnosisCode(request.getDiagnosisCode())
                .treatment(request.getTreatment())
                .prescription(request.getPrescription())
                .notes(request.getNotes())
                .vitalSigns(request.getVitalSigns())
                .labResults(request.getLabResults())
                .imagingResults(request.getImagingResults())
                .status(MedicalRecord.RecordStatus.ACTIVE)
                .build();
        
        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        log.info("Medical record created successfully with ID: {}", savedRecord.getId());
        
        return convertToResponse(savedRecord);
    }
    
    /**
     * Get medical record by ID
     */
    @Transactional(readOnly = true)
    public MedicalRecordResponse getMedicalRecordById(Long id) {
        log.info("Fetching medical record by ID: {}", id);
        
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record not found with ID: " + id));
        
        return convertToResponse(record);
    }
    
    /**
     * Get medical records by patient ID
     */
    @Transactional(readOnly = true)
    public Page<MedicalRecordResponse> getMedicalRecordsByPatientId(Long patientId, int page, int size, String sortBy, String sortDir) {
        log.info("Fetching medical records for patient: {}", patientId);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<MedicalRecord> records = medicalRecordRepository.findByPatientId(patientId, pageable);
        return records.map(this::convertToResponse);
    }
    
    /**
     * Get medical records by doctor ID
     */
    @Transactional(readOnly = true)
    public Page<MedicalRecordResponse> getMedicalRecordsByDoctorId(Long doctorId, int page, int size, String sortBy, String sortDir) {
        log.info("Fetching medical records for doctor: {}", doctorId);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<MedicalRecord> records = medicalRecordRepository.findByDoctorId(doctorId, pageable);
        return records.map(this::convertToResponse);
    }
    
    /**
     * Get recent medical records for patient
     */
    @Transactional(readOnly = true)
    public List<MedicalRecordResponse> getRecentMedicalRecordsByPatientId(Long patientId, int limit) {
        log.info("Fetching recent medical records for patient: {}", patientId);
        
        Pageable pageable = PageRequest.of(0, limit, Sort.by("visitDate").descending());
        List<MedicalRecord> records = medicalRecordRepository.findRecentMedicalRecordsByPatientId(patientId, pageable);
        
        return records.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert entity to response DTO
     */
    private MedicalRecordResponse convertToResponse(MedicalRecord record) {
        return MedicalRecordResponse.builder()
                .id(record.getId())
                .patientId(record.getPatientId())
                .doctorId(record.getDoctorId())
                .recordType(record.getRecordType())
                .visitDate(record.getVisitDate())
                .diagnosis(record.getDiagnosis())
                .diagnosisCode(record.getDiagnosisCode())
                .treatment(record.getTreatment())
                .prescription(record.getPrescription())
                .notes(record.getNotes())
                .vitalSigns(record.getVitalSigns())
                .labResults(record.getLabResults())
                .imagingResults(record.getImagingResults())
                .status(record.getStatus())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .statusDisplayName(getStatusDisplayName(record.getStatus()))
                .build();
    }
    
    private String getStatusDisplayName(MedicalRecord.RecordStatus status) {
        switch (status) {
            case ACTIVE: return "Active";
            case ARCHIVED: return "Archived";
            case DELETED: return "Deleted";
            default: return status.toString();
        }
    }
}
