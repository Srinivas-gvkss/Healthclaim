package com.gvkss.patil.patient_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Medical Record Entity
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "medical_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "patient_id", nullable = false)
    private Long patientId;
    
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;
    
    @Column(name = "record_type", nullable = false, length = 50)
    private String recordType;
    
    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;
    
    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;
    
    @Column(name = "diagnosis_code", length = 20)
    private String diagnosisCode;
    
    @Column(name = "treatment", columnDefinition = "TEXT")
    private String treatment;
    
    @Column(name = "prescription", columnDefinition = "TEXT")
    private String prescription;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "vital_signs", columnDefinition = "TEXT")
    private String vitalSigns;
    
    @Column(name = "lab_results", columnDefinition = "TEXT")
    private String labResults;
    
    @Column(name = "imaging_results", columnDefinition = "TEXT")
    private String imagingResults;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RecordStatus status = RecordStatus.ACTIVE;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Enums
    public enum RecordStatus {
        ACTIVE, ARCHIVED, DELETED
    }
}
