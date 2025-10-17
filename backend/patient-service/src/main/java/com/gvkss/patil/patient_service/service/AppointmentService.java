package com.gvkss.patil.patient_service.service;

import com.gvkss.patil.patient_service.dto.AppointmentResponse;
import com.gvkss.patil.patient_service.dto.ScheduleAppointmentRequest;
import com.gvkss.patil.patient_service.entity.Appointment;
import com.gvkss.patil.patient_service.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Appointment Service Implementation
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AppointmentService {
    
    private final AppointmentRepository appointmentRepository;
    
    /**
     * Schedule appointment
     */
    public AppointmentResponse scheduleAppointment(ScheduleAppointmentRequest request) {
        log.info("Scheduling appointment for patient: {}", request.getPatientId());
        
        // Generate unique appointment number
        String appointmentNumber = generateAppointmentNumber();
        
        // Create appointment entity
        Appointment appointment = Appointment.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .appointmentNumber(appointmentNumber)
                .appointmentDate(request.getAppointmentDate())
                .durationMinutes(request.getDurationMinutes() != null ? request.getDurationMinutes() : 30)
                .appointmentType(request.getAppointmentType())
                .status(Appointment.AppointmentStatus.SCHEDULED)
                .reason(request.getReason())
                .notes(request.getNotes())
                .reminderSent(false)
                .build();
        
        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Appointment scheduled successfully with ID: {}", savedAppointment.getId());
        
        return convertToResponse(savedAppointment);
    }
    
    /**
     * Get appointment by ID
     */
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long id) {
        log.info("Fetching appointment by ID: {}", id);
        
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + id));
        
        return convertToResponse(appointment);
    }
    
    /**
     * Get appointments by patient ID
     */
    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getAppointmentsByPatientId(Long patientId, int page, int size, String sortBy, String sortDir) {
        log.info("Fetching appointments for patient: {}", patientId);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Appointment> appointments = appointmentRepository.findByPatientId(patientId, pageable);
        return appointments.map(this::convertToResponse);
    }
    
    /**
     * Get appointments by doctor ID
     */
    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getAppointmentsByDoctorId(Long doctorId, int page, int size, String sortBy, String sortDir) {
        log.info("Fetching appointments for doctor: {}", doctorId);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId, pageable);
        return appointments.map(this::convertToResponse);
    }
    
    /**
     * Get today's appointments for doctor
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getTodayAppointmentsForDoctor(Long doctorId) {
        log.info("Fetching today's appointments for doctor: {}", doctorId);
        
        List<Appointment> appointments = appointmentRepository.findTodayAppointmentsForDoctor(doctorId);
        
        return appointments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get upcoming appointments for patient
     */
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getUpcomingAppointmentsForPatient(Long patientId) {
        log.info("Fetching upcoming appointments for patient: {}", patientId);
        
        List<Appointment> appointments = appointmentRepository.findUpcomingAppointmentsForPatient(patientId);
        
        return appointments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Update appointment status
     */
    public AppointmentResponse updateAppointmentStatus(Long id, Appointment.AppointmentStatus status, String notes) {
        log.info("Updating appointment status: {} to {}", id, status);
        
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + id));
        
        existingAppointment.setStatus(status);
        
        if (notes != null) {
            existingAppointment.setNotes(notes);
        }
        
        if (status == Appointment.AppointmentStatus.CANCELLED) {
            existingAppointment.setCancelledAt(LocalDateTime.now());
        }
        
        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
        log.info("Appointment status updated successfully: {}", updatedAppointment.getId());
        
        return convertToResponse(updatedAppointment);
    }
    
    /**
     * Cancel appointment
     */
    public AppointmentResponse cancelAppointment(Long id, String cancellationReason) {
        log.info("Cancelling appointment: {}", id);
        
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + id));
        
        existingAppointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        existingAppointment.setCancellationReason(cancellationReason);
        existingAppointment.setCancelledAt(LocalDateTime.now());
        
        Appointment cancelledAppointment = appointmentRepository.save(existingAppointment);
        log.info("Appointment cancelled successfully: {}", cancelledAppointment.getId());
        
        return convertToResponse(cancelledAppointment);
    }
    
    /**
     * Generate unique appointment number
     */
    private String generateAppointmentNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String randomSuffix = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "APT-" + timestamp + "-" + randomSuffix;
    }
    
    /**
     * Convert entity to response DTO
     */
    private AppointmentResponse convertToResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .appointmentNumber(appointment.getAppointmentNumber())
                .appointmentDate(appointment.getAppointmentDate())
                .durationMinutes(appointment.getDurationMinutes())
                .appointmentType(appointment.getAppointmentType())
                .status(appointment.getStatus())
                .reason(appointment.getReason())
                .notes(appointment.getNotes())
                .reminderSent(appointment.getReminderSent())
                .cancellationReason(appointment.getCancellationReason())
                .cancelledAt(appointment.getCancelledAt())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .typeDisplayName(getTypeDisplayName(appointment.getAppointmentType()))
                .statusDisplayName(getStatusDisplayName(appointment.getStatus()))
                .formattedDate(formatDate(appointment.getAppointmentDate()))
                .formattedTime(formatTime(appointment.getAppointmentDate()))
                .build();
    }
    
    private String getTypeDisplayName(Appointment.AppointmentType type) {
        switch (type) {
            case CONSULTATION: return "Consultation";
            case FOLLOW_UP: return "Follow-up";
            case CHECKUP: return "Check-up";
            case EMERGENCY: return "Emergency";
            case SURGERY: return "Surgery";
            case LAB_TEST: return "Lab Test";
            case IMAGING: return "Imaging";
            default: return type.toString();
        }
    }
    
    private String getStatusDisplayName(Appointment.AppointmentStatus status) {
        switch (status) {
            case SCHEDULED: return "Scheduled";
            case CONFIRMED: return "Confirmed";
            case IN_PROGRESS: return "In Progress";
            case COMPLETED: return "Completed";
            case CANCELLED: return "Cancelled";
            case NO_SHOW: return "No Show";
            default: return status.toString();
        }
    }
    
    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }
    
    private String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern("hh:mm a"));
    }
}
