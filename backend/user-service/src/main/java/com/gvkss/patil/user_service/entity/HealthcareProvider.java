package com.gvkss.patil.user_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Healthcare Provider entity representing hospitals, clinics, pharmacies, etc.
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Entity
@Table(name = "healthcare_providers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthcareProvider {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    
    @Column(name = "type", nullable = false, length = 50)
    private String type; // HOSPITAL, CLINIC, PHARMACY, LAB
    
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "license_number", length = 50)
    private String licenseNumber;
    
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
