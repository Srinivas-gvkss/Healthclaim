package com.gvkss.patil.patient_service.dto;

import com.gvkss.patil.patient_service.entity.Appointment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Appointment Response DTO
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    
    private Long id;
    private Long patientId;
    private Long doctorId;
    private String appointmentNumber;
    private LocalDateTime appointmentDate;
    private Integer durationMinutes;
    private Appointment.AppointmentType appointmentType;
    private Appointment.AppointmentStatus status;
    private String reason;
    private String notes;
    private Boolean reminderSent;
    private String cancellationReason;
    private LocalDateTime cancelledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for UI
    private String patientName;
    private String doctorName;
    private String typeDisplayName;
    private String statusDisplayName;
    private String formattedDate;
    private String formattedTime;
}
