package com.gvkss.patil.patient_service.dto;

import com.gvkss.patil.patient_service.entity.Appointment;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Schedule Appointment Request DTO
 * 
 * @author gvkss team
 * @version 1.0
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleAppointmentRequest {
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
    
    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime appointmentDate;
    
    @Min(value = 15, message = "Duration must be at least 15 minutes")
    @Max(value = 240, message = "Duration cannot exceed 240 minutes")
    private Integer durationMinutes = 30;
    
    @NotNull(message = "Appointment type is required")
    private Appointment.AppointmentType appointmentType;
    
    @Size(max = 2000, message = "Reason cannot exceed 2000 characters")
    private String reason;
    
    @Size(max = 2000, message = "Notes cannot exceed 2000 characters")
    private String notes;
}
