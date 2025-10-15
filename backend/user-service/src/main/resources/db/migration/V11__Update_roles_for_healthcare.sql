-- =============================================================================
-- Healthcare Insurance Claim System - Database Migration V11
-- Update roles to match healthcare domain
-- =============================================================================

-- Update existing roles to healthcare-specific roles
UPDATE roles SET 
    name = 'Patient',
    code = 'patient',
    description = 'Healthcare patients who can submit insurance claims',
    role_type = 'HEALTHCARE',
    access_level = 1,
    permissions = 'VIEW_OWN_CLAIMS,SUBMIT_CLAIMS,VIEW_OWN_MEDICAL_RECORDS,SCHEDULE_APPOINTMENTS'
WHERE code = 'user';

UPDATE roles SET 
    name = 'Doctor',
    code = 'doctor',
    description = 'Medical doctors who can verify claims and manage patients',
    role_type = 'HEALTHCARE',
    access_level = 3,
    permissions = 'VIEW_PATIENT_CLAIMS,VERIFY_CLAIMS,MANAGE_PATIENTS,VIEW_MEDICAL_RECORDS,PRESCRIBE_MEDICATIONS'
WHERE code = 'admin';

-- Insert new healthcare-specific roles
INSERT INTO roles (name, code, description, role_type, access_level, is_active, is_system_defined, permissions) VALUES
('Admin', 'admin', 'System administrators with full access', 'HEALTHCARE', 5, true, true, 'MANAGE_USERS,MANAGE_CLAIMS,VIEW_REPORTS,SYSTEM_CONFIGURATION'),
('Insurance Provider', 'insurance_provider', 'Insurance company representatives who review claims', 'HEALTHCARE', 4, true, true, 'REVIEW_CLAIMS,APPROVE_CLAIMS,MANAGE_POLICIES,VIEW_CLAIM_STATISTICS')
ON CONFLICT (code) DO NOTHING;

-- Update departments to healthcare departments
UPDATE departments SET 
    name = 'Cardiology',
    code = 'cardiology',
    description = 'Heart and cardiovascular care department',
    department_type = 'MEDICAL'
WHERE code = 'it_infrastructure';

UPDATE departments SET 
    name = 'General Medicine',
    code = 'general_medicine',
    description = 'General medical care and consultations',
    department_type = 'MEDICAL'
WHERE code = 'application_support';

UPDATE departments SET 
    name = 'Emergency Medicine',
    code = 'emergency_medicine',
    description = 'Emergency medical care and urgent treatments',
    department_type = 'MEDICAL'
WHERE code = 'security';

UPDATE departments SET 
    name = 'Radiology',
    code = 'radiology',
    description = 'Medical imaging and diagnostic services',
    department_type = 'MEDICAL'
WHERE code = 'sap_support';

-- Insert additional healthcare departments
INSERT INTO departments (name, code, description, department_type, is_active) VALUES
('Pediatrics', 'pediatrics', 'Medical care for children and adolescents', 'MEDICAL', true),
('Orthopedics', 'orthopedics', 'Bone, joint, and muscle care', 'MEDICAL', true),
('Dermatology', 'dermatology', 'Skin, hair, and nail care', 'MEDICAL', true),
('Psychiatry', 'psychiatry', 'Mental health and psychological care', 'MEDICAL', true),
('Insurance Claims', 'insurance_claims', 'Insurance claim processing and management', 'ADMINISTRATIVE', true)
ON CONFLICT (code) DO NOTHING;
