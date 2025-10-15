-- =============================================================================
-- Healthcare Insurance Claim System - Database Migration V12
-- Fix roles creation and ensure all required roles exist
-- =============================================================================

-- Delete any existing roles to start fresh
DELETE FROM user_roles;
DELETE FROM roles;

-- Insert all required healthcare roles with correct codes
INSERT INTO roles (name, code, description, role_type, access_level, is_active, is_system_defined, permissions, created_at, updated_at) VALUES
('Patient', 'patient', 'Healthcare patients who can submit insurance claims', 'HEALTHCARE', 1, true, true, 'VIEW_OWN_CLAIMS,SUBMIT_CLAIMS,VIEW_OWN_MEDICAL_RECORDS,SCHEDULE_APPOINTMENTS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Doctor', 'doctor', 'Medical doctors who can verify claims and manage patients', 'HEALTHCARE', 3, true, true, 'VIEW_PATIENT_CLAIMS,VERIFY_CLAIMS,MANAGE_PATIENTS,VIEW_MEDICAL_RECORDS,PRESCRIBE_MEDICATIONS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Admin', 'admin', 'System administrators with full access', 'HEALTHCARE', 5, true, true, 'MANAGE_USERS,MANAGE_CLAIMS,VIEW_REPORTS,SYSTEM_CONFIGURATION', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Insurance Provider', 'insurance_provider', 'Insurance company representatives who review claims', 'HEALTHCARE', 4, true, true, 'REVIEW_CLAIMS,APPROVE_CLAIMS,MANAGE_POLICIES,VIEW_CLAIM_STATISTICS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Delete any existing departments to start fresh
DELETE FROM departments;

-- Insert healthcare departments
INSERT INTO departments (name, code, description, department_type, is_active, created_at, updated_at) VALUES
('Cardiology', 'cardiology', 'Heart and cardiovascular care department', 'MEDICAL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('General Medicine', 'general_medicine', 'General medical care and consultations', 'MEDICAL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Emergency Medicine', 'emergency_medicine', 'Emergency medical care and urgent treatments', 'MEDICAL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Radiology', 'radiology', 'Medical imaging and diagnostic services', 'MEDICAL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Pediatrics', 'pediatrics', 'Medical care for children and adolescents', 'MEDICAL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Orthopedics', 'orthopedics', 'Bone, joint, and muscle care', 'MEDICAL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dermatology', 'dermatology', 'Skin, hair, and nail care', 'MEDICAL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Psychiatry', 'psychiatry', 'Mental health and psychological care', 'MEDICAL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Insurance Claims', 'insurance_claims', 'Insurance claim processing and management', 'ADMINISTRATIVE', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Add comments for documentation
COMMENT ON TABLE roles IS 'User roles with specific permissions and access levels for healthcare system';
COMMENT ON TABLE departments IS 'Medical departments and administrative units in the healthcare system';

COMMENT ON COLUMN roles.code IS 'Role code used in application (patient, doctor, admin, insurance_provider)';
COMMENT ON COLUMN roles.access_level IS 'Access level from 1-5, where 5 is highest (admin)';
COMMENT ON COLUMN departments.department_type IS 'Type of department (MEDICAL, ADMINISTRATIVE)';
