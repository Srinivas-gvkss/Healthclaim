-- =============================================================================
-- Healthcare Insurance Claim System - Database Migration V10
-- Create healthcare-specific tables and update existing ones
-- =============================================================================

-- Update users table to include healthcare-specific fields
ALTER TABLE users ADD COLUMN IF NOT EXISTS date_of_birth DATE;
ALTER TABLE users ADD COLUMN IF NOT EXISTS gender VARCHAR(10);
ALTER TABLE users ADD COLUMN IF NOT EXISTS address TEXT;
ALTER TABLE users ADD COLUMN IF NOT EXISTS emergency_contact_name VARCHAR(100);
ALTER TABLE users ADD COLUMN IF NOT EXISTS emergency_contact_phone VARCHAR(20);
ALTER TABLE users ADD COLUMN IF NOT EXISTS medical_license_number VARCHAR(50);
ALTER TABLE users ADD COLUMN IF NOT EXISTS specialty VARCHAR(100);
ALTER TABLE users ADD COLUMN IF NOT EXISTS insurance_policy_number VARCHAR(50);
ALTER TABLE users ADD COLUMN IF NOT EXISTS insurance_provider VARCHAR(100);
ALTER TABLE users ADD COLUMN IF NOT EXISTS blood_type VARCHAR(5);
ALTER TABLE users ADD COLUMN IF NOT EXISTS allergies TEXT;
ALTER TABLE users ADD COLUMN IF NOT EXISTS medical_conditions TEXT;

-- Create healthcare providers table
CREATE TABLE IF NOT EXISTS healthcare_providers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    type VARCHAR(50) NOT NULL, -- HOSPITAL, CLINIC, PHARMACY, LAB
    address TEXT,
    phone VARCHAR(20),
    email VARCHAR(100),
    license_number VARCHAR(50),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create medical records table
CREATE TABLE IF NOT EXISTS medical_records (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT,
    provider_id BIGINT,
    record_type VARCHAR(50) NOT NULL, -- CONSULTATION, PRESCRIPTION, LAB_RESULT, IMAGING
    title VARCHAR(200) NOT NULL,
    description TEXT,
    diagnosis TEXT,
    treatment TEXT,
    medications TEXT,
    record_date DATE NOT NULL,
    file_path VARCHAR(500),
    is_emergency BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (provider_id) REFERENCES healthcare_providers(id) ON DELETE SET NULL
);

-- Create insurance claims table
CREATE TABLE IF NOT EXISTS insurance_claims (
    id BIGSERIAL PRIMARY KEY,
    claim_number VARCHAR(50) NOT NULL UNIQUE,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT,
    provider_id BIGINT,
    claim_type VARCHAR(50) NOT NULL, -- MEDICAL, DENTAL, VISION, PHARMACY
    claim_status VARCHAR(50) NOT NULL DEFAULT 'SUBMITTED', -- SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, PAID
    total_amount DECIMAL(10,2) NOT NULL,
    approved_amount DECIMAL(10,2),
    deductible_amount DECIMAL(10,2) DEFAULT 0,
    copay_amount DECIMAL(10,2) DEFAULT 0,
    coinsurance_amount DECIMAL(10,2) DEFAULT 0,
    treatment_date DATE NOT NULL,
    service_description TEXT NOT NULL,
    diagnosis_code VARCHAR(20),
    procedure_code VARCHAR(20),
    is_emergency BOOLEAN NOT NULL DEFAULT FALSE,
    priority VARCHAR(20) NOT NULL DEFAULT 'NORMAL', -- LOW, NORMAL, HIGH, URGENT
    submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMP,
    approved_at TIMESTAMP,
    paid_at TIMESTAMP,
    rejection_reason TEXT,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (provider_id) REFERENCES healthcare_providers(id) ON DELETE SET NULL
);

-- Create claim documents table
CREATE TABLE IF NOT EXISTS claim_documents (
    id BIGSERIAL PRIMARY KEY,
    claim_id BIGINT NOT NULL,
    document_type VARCHAR(50) NOT NULL, -- RECEIPT, PRESCRIPTION, LAB_RESULT, MEDICAL_REPORT
    file_name VARCHAR(200) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT,
    mime_type VARCHAR(100),
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    uploaded_by BIGINT,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    verification_notes TEXT,
    FOREIGN KEY (claim_id) REFERENCES insurance_claims(id) ON DELETE CASCADE,
    FOREIGN KEY (uploaded_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Create appointments table
CREATE TABLE IF NOT EXISTS appointments (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    provider_id BIGINT,
    appointment_type VARCHAR(50) NOT NULL, -- CONSULTATION, FOLLOW_UP, EMERGENCY, SURGERY
    appointment_date TIMESTAMP NOT NULL,
    duration_minutes INTEGER NOT NULL DEFAULT 30,
    status VARCHAR(50) NOT NULL DEFAULT 'SCHEDULED', -- SCHEDULED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW
    reason TEXT,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (provider_id) REFERENCES healthcare_providers(id) ON DELETE SET NULL
);

-- Create notifications table
CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL, -- CLAIM_UPDATE, APPOINTMENT_REMINDER, PAYMENT_DUE, SYSTEM_ALERT
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    priority VARCHAR(20) NOT NULL DEFAULT 'NORMAL', -- LOW, NORMAL, HIGH, URGENT
    action_url VARCHAR(500),
    expires_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_medical_records_patient_id ON medical_records(patient_id);
CREATE INDEX IF NOT EXISTS idx_medical_records_doctor_id ON medical_records(doctor_id);
CREATE INDEX IF NOT EXISTS idx_medical_records_record_date ON medical_records(record_date);
CREATE INDEX IF NOT EXISTS idx_insurance_claims_patient_id ON insurance_claims(patient_id);
CREATE INDEX IF NOT EXISTS idx_insurance_claims_claim_status ON insurance_claims(claim_status);
CREATE INDEX IF NOT EXISTS idx_insurance_claims_treatment_date ON insurance_claims(treatment_date);
CREATE INDEX IF NOT EXISTS idx_insurance_claims_claim_number ON insurance_claims(claim_number);
CREATE INDEX IF NOT EXISTS idx_claim_documents_claim_id ON claim_documents(claim_id);
CREATE INDEX IF NOT EXISTS idx_appointments_patient_id ON appointments(patient_id);
CREATE INDEX IF NOT EXISTS idx_appointments_doctor_id ON appointments(doctor_id);
CREATE INDEX IF NOT EXISTS idx_appointments_appointment_date ON appointments(appointment_date);
CREATE INDEX IF NOT EXISTS idx_notifications_user_id ON notifications(user_id);
CREATE INDEX IF NOT EXISTS idx_notifications_is_read ON notifications(is_read);
CREATE INDEX IF NOT EXISTS idx_healthcare_providers_type ON healthcare_providers(type);

-- Add comments for documentation
COMMENT ON TABLE healthcare_providers IS 'Healthcare providers like hospitals, clinics, pharmacies';
COMMENT ON TABLE medical_records IS 'Medical records and documents for patients';
COMMENT ON TABLE insurance_claims IS 'Insurance claims submitted by patients';
COMMENT ON TABLE claim_documents IS 'Documents attached to insurance claims';
COMMENT ON TABLE appointments IS 'Medical appointments between patients and doctors';
COMMENT ON TABLE notifications IS 'System notifications for users';

COMMENT ON COLUMN users.medical_license_number IS 'Medical license number for doctors';
COMMENT ON COLUMN users.specialty IS 'Medical specialty for doctors';
COMMENT ON COLUMN users.insurance_policy_number IS 'Insurance policy number for patients';
COMMENT ON COLUMN users.insurance_provider IS 'Insurance provider name';
COMMENT ON COLUMN insurance_claims.claim_status IS 'Status: SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, PAID';
COMMENT ON COLUMN insurance_claims.priority IS 'Priority: LOW, NORMAL, HIGH, URGENT';
COMMENT ON COLUMN appointments.status IS 'Status: SCHEDULED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW';
