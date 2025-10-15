-- =============================================================================
-- Healthcare Insurance Claim System - Database Migration V13
-- Fix role_type enum mismatch between database and Java code
-- =============================================================================

-- The issue is that the database has role_type = 'HEALTHCARE' but the Java UserRole enum
-- doesn't have a HEALTHCARE constant. The role_type field is mapped to the UserRole enum
-- which only has: PATIENT, DOCTOR, ADMIN, INSURANCE_PROVIDER.

-- Update all roles to have role_type that matches the UserRole enum constants
UPDATE roles SET role_type = 'PATIENT' WHERE code = 'patient';
UPDATE roles SET role_type = 'DOCTOR' WHERE code = 'doctor';
UPDATE roles SET role_type = 'ADMIN' WHERE code = 'admin';
UPDATE roles SET role_type = 'INSURANCE_PROVIDER' WHERE code = 'insurance_provider';

-- Ensure all roles have valid role_type values
UPDATE roles SET role_type = 'PATIENT' WHERE role_type = 'HEALTHCARE' AND code = 'patient';
UPDATE roles SET role_type = 'DOCTOR' WHERE role_type = 'HEALTHCARE' AND code = 'doctor';
UPDATE roles SET role_type = 'ADMIN' WHERE role_type = 'HEALTHCARE' AND code = 'admin';
UPDATE roles SET role_type = 'INSURANCE_PROVIDER' WHERE role_type = 'HEALTHCARE' AND code = 'insurance_provider';

-- Add comments for documentation
COMMENT ON COLUMN roles.role_type IS 'Role type mapped to UserRole enum - must be one of: PATIENT, DOCTOR, ADMIN, INSURANCE_PROVIDER';
