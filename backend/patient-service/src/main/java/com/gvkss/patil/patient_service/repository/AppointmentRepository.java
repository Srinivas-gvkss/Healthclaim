package com.gvkss.patil.patient_service.repository;

import com.gvkss.patil.patient_service.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Appointment Repository Interface
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    /**
     * Find appointment by appointment number
     */
    Appointment findByAppointmentNumber(String appointmentNumber);
    
    /**
     * Find all appointments by patient ID
     */
    Page<Appointment> findByPatientId(Long patientId, Pageable pageable);
    
    /**
     * Find all appointments by doctor ID
     */
    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);
    
    /**
     * Find appointments by status
     */
    Page<Appointment> findByStatus(Appointment.AppointmentStatus status, Pageable pageable);
    
    /**
     * Find appointments by type
     */
    Page<Appointment> findByAppointmentType(Appointment.AppointmentType type, Pageable pageable);
    
    /**
     * Find appointments by date range
     */
    Page<Appointment> findByAppointmentDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Find appointments by patient ID and status
     */
    Page<Appointment> findByPatientIdAndStatus(Long patientId, Appointment.AppointmentStatus status, Pageable pageable);
    
    /**
     * Find appointments by doctor ID and status
     */
    Page<Appointment> findByDoctorIdAndStatus(Long doctorId, Appointment.AppointmentStatus status, Pageable pageable);
    
    /**
     * Find today's appointments for doctor
     */
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND DATE(a.appointmentDate) = CURRENT_DATE ORDER BY a.appointmentDate ASC")
    List<Appointment> findTodayAppointmentsForDoctor(@Param("doctorId") Long doctorId);
    
    /**
     * Find upcoming appointments for patient
     */
    @Query("SELECT a FROM Appointment a WHERE a.patientId = :patientId AND a.appointmentDate > CURRENT_TIMESTAMP AND a.status IN ('SCHEDULED', 'CONFIRMED') ORDER BY a.appointmentDate ASC")
    List<Appointment> findUpcomingAppointmentsForPatient(@Param("patientId") Long patientId);
    
    /**
     * Find appointments requiring reminders
     */
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN CURRENT_TIMESTAMP AND :reminderTime AND a.reminderSent = false AND a.status IN ('SCHEDULED', 'CONFIRMED')")
    List<Appointment> findAppointmentsRequiringReminders(@Param("reminderTime") LocalDateTime reminderTime);
    
    /**
     * Count appointments by patient ID
     */
    long countByPatientId(Long patientId);
    
    /**
     * Count appointments by doctor ID
     */
    long countByDoctorId(Long doctorId);
    
    /**
     * Count appointments by status
     */
    long countByStatus(Appointment.AppointmentStatus status);
    
    /**
     * Find available time slots for doctor
     */
    @Query("SELECT a.appointmentDate FROM Appointment a WHERE a.doctorId = :doctorId AND a.appointmentDate BETWEEN :startDate AND :endDate AND a.status IN ('SCHEDULED', 'CONFIRMED', 'IN_PROGRESS')")
    List<LocalDateTime> findBookedTimeSlotsForDoctor(@Param("doctorId") Long doctorId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
