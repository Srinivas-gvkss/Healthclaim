package com.gvkss.patil.claim_service.repository;

import com.gvkss.patil.claim_service.entity.InsuranceClaim;
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
 * Claim Repository Interface
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Repository
public interface ClaimRepository extends JpaRepository<InsuranceClaim, Long> {
    
    /**
     * Find claim by claim number
     */
    Optional<InsuranceClaim> findByClaimNumber(String claimNumber);
    
    /**
     * Find all claims by patient ID
     */
    Page<InsuranceClaim> findByPatientId(Long patientId, Pageable pageable);
    
    /**
     * Find all claims by doctor ID
     */
    Page<InsuranceClaim> findByDoctorId(Long doctorId, Pageable pageable);
    
    /**
     * Find all claims by provider ID
     */
    Page<InsuranceClaim> findByProviderId(Long providerId, Pageable pageable);
    
    /**
     * Find all claims by status
     */
    Page<InsuranceClaim> findByClaimStatus(InsuranceClaim.ClaimStatus status, Pageable pageable);
    
    /**
     * Find all claims by type
     */
    Page<InsuranceClaim> findByClaimType(InsuranceClaim.ClaimType type, Pageable pageable);
    
    /**
     * Find all claims by priority
     */
    Page<InsuranceClaim> findByPriority(InsuranceClaim.ClaimPriority priority, Pageable pageable);
    
    /**
     * Find all emergency claims
     */
    Page<InsuranceClaim> findByIsEmergencyTrue(Pageable pageable);
    
    /**
     * Find claims by patient ID and status
     */
    Page<InsuranceClaim> findByPatientIdAndClaimStatus(Long patientId, InsuranceClaim.ClaimStatus status, Pageable pageable);
    
    /**
     * Find claims by doctor ID and status
     */
    Page<InsuranceClaim> findByDoctorIdAndClaimStatus(Long doctorId, InsuranceClaim.ClaimStatus status, Pageable pageable);
    
    /**
     * Find claims by treatment date range
     */
    Page<InsuranceClaim> findByTreatmentDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find claims by patient ID and treatment date range
     */
    Page<InsuranceClaim> findByPatientIdAndTreatmentDateBetween(Long patientId, LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Count claims by status
     */
    long countByClaimStatus(InsuranceClaim.ClaimStatus status);
    
    /**
     * Count claims by patient ID
     */
    long countByPatientId(Long patientId);
    
    /**
     * Count claims by doctor ID
     */
    long countByDoctorId(Long doctorId);
    
    /**
     * Get total amount of approved claims by patient ID
     */
    @Query("SELECT COALESCE(SUM(c.approvedAmount), 0) FROM InsuranceClaim c WHERE c.patientId = :patientId AND c.claimStatus = 'APPROVED'")
    Double getTotalApprovedAmountByPatientId(@Param("patientId") Long patientId);
    
    /**
     * Get total amount of claims by patient ID
     */
    @Query("SELECT COALESCE(SUM(c.totalAmount), 0) FROM InsuranceClaim c WHERE c.patientId = :patientId")
    Double getTotalAmountByPatientId(@Param("patientId") Long patientId);
    
    /**
     * Find recent claims by patient ID (last 10)
     */
    @Query("SELECT c FROM InsuranceClaim c WHERE c.patientId = :patientId ORDER BY c.createdAt DESC")
    List<InsuranceClaim> findRecentClaimsByPatientId(@Param("patientId") Long patientId, Pageable pageable);
    
    /**
     * Find pending claims for doctor verification
     */
    @Query("SELECT c FROM InsuranceClaim c WHERE c.doctorId = :doctorId AND c.claimStatus = 'SUBMITTED' ORDER BY c.priority DESC, c.createdAt ASC")
    List<InsuranceClaim> findPendingClaimsForDoctor(@Param("doctorId") Long doctorId);
    
    /**
     * Find claims for insurance provider review
     */
    @Query("SELECT c FROM InsuranceClaim c WHERE c.claimStatus = 'UNDER_REVIEW' ORDER BY c.priority DESC, c.submittedAt ASC")
    Page<InsuranceClaim> findClaimsForInsuranceReview(Pageable pageable);
}
