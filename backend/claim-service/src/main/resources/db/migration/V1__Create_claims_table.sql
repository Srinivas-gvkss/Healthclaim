-- Create insurance_claims table
CREATE TABLE IF NOT EXISTS insurance_claims (
    id BIGSERIAL PRIMARY KEY,
    claim_number VARCHAR(50) NOT NULL UNIQUE,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT,
    provider_id BIGINT,
    claim_type VARCHAR(50) NOT NULL,
    claim_status VARCHAR(50) NOT NULL DEFAULT 'SUBMITTED',
    total_amount DECIMAL(10,2) NOT NULL,
    approved_amount DECIMAL(10,2),
    deductible_amount DECIMAL(10,2) DEFAULT 0.00,
    copay_amount DECIMAL(10,2) DEFAULT 0.00,
    coinsurance_amount DECIMAL(10,2) DEFAULT 0.00,
    treatment_date DATE NOT NULL,
    service_description TEXT NOT NULL,
    diagnosis_code VARCHAR(20),
    procedure_code VARCHAR(20),
    is_emergency BOOLEAN NOT NULL DEFAULT FALSE,
    priority VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMP,
    approved_at TIMESTAMP,
    paid_at TIMESTAMP,
    rejection_reason TEXT,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_claims_patient_id ON insurance_claims(patient_id);
CREATE INDEX IF NOT EXISTS idx_claims_doctor_id ON insurance_claims(doctor_id);
CREATE INDEX IF NOT EXISTS idx_claims_provider_id ON insurance_claims(provider_id);
CREATE INDEX IF NOT EXISTS idx_claims_status ON insurance_claims(claim_status);
CREATE INDEX IF NOT EXISTS idx_claims_type ON insurance_claims(claim_type);
CREATE INDEX IF NOT EXISTS idx_claims_priority ON insurance_claims(priority);
CREATE INDEX IF NOT EXISTS idx_claims_treatment_date ON insurance_claims(treatment_date);
CREATE INDEX IF NOT EXISTS idx_claims_created_at ON insurance_claims(created_at);
CREATE INDEX IF NOT EXISTS idx_claims_emergency ON insurance_claims(is_emergency);

-- Create unique index on claim_number
CREATE UNIQUE INDEX IF NOT EXISTS idx_claims_claim_number ON insurance_claims(claim_number);

-- Add comments for documentation
COMMENT ON TABLE insurance_claims IS 'Insurance claims for healthcare services';
COMMENT ON COLUMN insurance_claims.claim_number IS 'Unique claim identifier';
COMMENT ON COLUMN insurance_claims.patient_id IS 'Reference to patient user ID';
COMMENT ON COLUMN insurance_claims.doctor_id IS 'Reference to doctor user ID';
COMMENT ON COLUMN insurance_claims.provider_id IS 'Reference to healthcare provider ID';
COMMENT ON COLUMN insurance_claims.claim_type IS 'Type of claim: MEDICAL, DENTAL, VISION, PHARMACY';
COMMENT ON COLUMN insurance_claims.claim_status IS 'Status: SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, PAID';
COMMENT ON COLUMN insurance_claims.priority IS 'Priority level: LOW, NORMAL, HIGH, URGENT';
COMMENT ON COLUMN insurance_claims.is_emergency IS 'Whether this is an emergency claim';
COMMENT ON COLUMN insurance_claims.treatment_date IS 'Date when treatment was provided';
COMMENT ON COLUMN insurance_claims.service_description IS 'Description of the medical service';
COMMENT ON COLUMN insurance_claims.diagnosis_code IS 'Medical diagnosis code (ICD-10)';
COMMENT ON COLUMN insurance_claims.procedure_code IS 'Medical procedure code (CPT)';
