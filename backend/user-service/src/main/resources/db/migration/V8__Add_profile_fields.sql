-- Add profile-specific fields to users table
-- For doctor and patient role-specific information

-- Add medical license number for doctors
ALTER TABLE users ADD COLUMN IF NOT EXISTS medical_license_number VARCHAR(100);

-- Add specialty for doctors
ALTER TABLE users ADD COLUMN IF NOT EXISTS specialty VARCHAR(100);

-- Add insurance policy number for patients
ALTER TABLE users ADD COLUMN IF NOT EXISTS insurance_policy_number VARCHAR(100);

-- Add comments
COMMENT ON COLUMN users.medical_license_number IS 'Medical license number (required for doctors)';
COMMENT ON COLUMN users.specialty IS 'Medical specialty (required for doctors)';
COMMENT ON COLUMN users.insurance_policy_number IS 'Insurance policy number (required for patients)';

