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
 * Patient Entity
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "patients")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    @Column(name = "patient_number", unique = true, nullable = false, length = 50)
    private String patientNumber;
    
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    private Gender gender;
    
    @Column(name = "blood_type", length = 5)
    private String bloodType;
    
    @Column(name = "height_cm")
    private Integer heightCm;
    
    @Column(name = "weight_kg")
    private Double weightKg;
    
    @Column(name = "allergies", columnDefinition = "TEXT")
    private String allergies;
    
    @Column(name = "medical_conditions", columnDefinition = "TEXT")
    private String medicalConditions;
    
    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;
    
    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;
    
    @Column(name = "emergency_contact_relationship", length = 50)
    private String emergencyContactRelationship;
    
    @Column(name = "primary_doctor_id")
    private Long primaryDoctorId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PatientStatus status = PatientStatus.ACTIVE;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Enums
    public enum Gender {
        MALE, FEMALE, OTHER
    }
    
    public enum PatientStatus {
        ACTIVE, INACTIVE, DECEASED
    }
}
