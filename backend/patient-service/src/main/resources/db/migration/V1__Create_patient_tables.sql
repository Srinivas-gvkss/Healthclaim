-- Create patients table
CREATE TABLE IF NOT EXISTS patients (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    patient_number VARCHAR(50) NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(10) NOT NULL,
    blood_type VARCHAR(5),
    height_cm INTEGER,
    weight_kg DECIMAL(5,2),
    allergies TEXT,
    medical_conditions TEXT,
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    emergency_contact_relationship VARCHAR(50),
    primary_doctor_id BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create medical_records table
CREATE TABLE IF NOT EXISTS medical_records (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    record_type VARCHAR(50) NOT NULL,
    visit_date DATE NOT NULL,
    diagnosis TEXT,
    diagnosis_code VARCHAR(20),
    treatment TEXT,
    prescription TEXT,
    notes TEXT,
    vital_signs TEXT,
    lab_results TEXT,
    imaging_results TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

-- Create appointments table
CREATE TABLE IF NOT EXISTS appointments (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    appointment_number VARCHAR(50) NOT NULL UNIQUE,
    appointment_date TIMESTAMP NOT NULL,
    duration_minutes INTEGER NOT NULL DEFAULT 30,
    appointment_type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    reason TEXT,
    notes TEXT,
    reminder_sent BOOLEAN NOT NULL DEFAULT FALSE,
    cancellation_reason TEXT,
    cancelled_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_patients_user_id ON patients(user_id);
CREATE INDEX IF NOT EXISTS idx_patients_patient_number ON patients(patient_number);
CREATE INDEX IF NOT EXISTS idx_patients_status ON patients(status);
CREATE INDEX IF NOT EXISTS idx_patients_primary_doctor_id ON patients(primary_doctor_id);
CREATE INDEX IF NOT EXISTS idx_patients_gender ON patients(gender);
CREATE INDEX IF NOT EXISTS idx_patients_date_of_birth ON patients(date_of_birth);

CREATE INDEX IF NOT EXISTS idx_medical_records_patient_id ON medical_records(patient_id);
CREATE INDEX IF NOT EXISTS idx_medical_records_doctor_id ON medical_records(doctor_id);
CREATE INDEX IF NOT EXISTS idx_medical_records_visit_date ON medical_records(visit_date);
CREATE INDEX IF NOT EXISTS idx_medical_records_record_type ON medical_records(record_type);
CREATE INDEX IF NOT EXISTS idx_medical_records_status ON medical_records(status);

CREATE INDEX IF NOT EXISTS idx_appointments_patient_id ON appointments(patient_id);
CREATE INDEX IF NOT EXISTS idx_appointments_doctor_id ON appointments(doctor_id);
CREATE INDEX IF NOT EXISTS idx_appointments_appointment_number ON appointments(appointment_number);
CREATE INDEX IF NOT EXISTS idx_appointments_appointment_date ON appointments(appointment_date);
CREATE INDEX IF NOT EXISTS idx_appointments_status ON appointments(status);
CREATE INDEX IF NOT EXISTS idx_appointments_type ON appointments(appointment_type);

-- Add comments for documentation
COMMENT ON TABLE patients IS 'Patient profiles and medical information';
COMMENT ON COLUMN patients.user_id IS 'Reference to user ID from user service';
COMMENT ON COLUMN patients.patient_number IS 'Unique patient identifier';
COMMENT ON COLUMN patients.gender IS 'Patient gender: MALE, FEMALE, OTHER';
COMMENT ON COLUMN patients.status IS 'Patient status: ACTIVE, INACTIVE, DECEASED';

COMMENT ON TABLE medical_records IS 'Medical records and treatment history';
COMMENT ON COLUMN medical_records.record_type IS 'Type of medical record';
COMMENT ON COLUMN medical_records.visit_date IS 'Date of medical visit';
COMMENT ON COLUMN medical_records.diagnosis_code IS 'Medical diagnosis code (ICD-10)';
COMMENT ON COLUMN medical_records.status IS 'Record status: ACTIVE, ARCHIVED, DELETED';

COMMENT ON TABLE appointments IS 'Patient appointments and scheduling';
COMMENT ON COLUMN appointments.appointment_number IS 'Unique appointment identifier';
COMMENT ON COLUMN appointments.appointment_type IS 'Type of appointment: CONSULTATION, FOLLOW_UP, CHECKUP, etc.';
COMMENT ON COLUMN appointments.status IS 'Appointment status: SCHEDULED, CONFIRMED, COMPLETED, CANCELLED, etc.';
