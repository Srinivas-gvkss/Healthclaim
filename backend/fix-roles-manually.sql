-- Manual fix for roles issue
-- Run this directly in PostgreSQL to ensure roles exist

-- Connect to the postgres database and run these commands:

-- First, check if roles table exists and what's in it
SELECT * FROM roles;

-- If roles table is empty or missing roles, insert them:
INSERT INTO roles (name, code, description, role_type, access_level, is_active, is_system_defined, permissions, created_at, updated_at) 
VALUES 
('Patient', 'patient', 'Healthcare patients who can submit insurance claims', 'HEALTHCARE', 1, true, true, 'VIEW_OWN_CLAIMS,SUBMIT_CLAIMS,VIEW_OWN_MEDICAL_RECORDS,SCHEDULE_APPOINTMENTS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Doctor', 'doctor', 'Medical doctors who can verify claims and manage patients', 'HEALTHCARE', 3, true, true, 'VIEW_PATIENT_CLAIMS,VERIFY_CLAIMS,MANAGE_PATIENTS,VIEW_MEDICAL_RECORDS,PRESCRIBE_MEDICATIONS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Admin', 'admin', 'System administrators with full access', 'HEALTHCARE', 5, true, true, 'MANAGE_USERS,MANAGE_CLAIMS,VIEW_REPORTS,SYSTEM_CONFIGURATION', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Insurance Provider', 'insurance_provider', 'Insurance company representatives who review claims', 'HEALTHCARE', 4, true, true, 'REVIEW_CLAIMS,APPROVE_CLAIMS,MANAGE_POLICIES,VIEW_CLAIM_STATISTICS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (code) DO NOTHING;

-- Verify roles were inserted
SELECT * FROM roles;

-- Also insert departments if missing
INSERT INTO departments (name, code, description, department_type, is_active, created_at, updated_at) 
VALUES 
('Cardiology', 'cardiology', 'Heart and cardiovascular care department', 'MEDICAL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('General Medicine', 'general_medicine', 'General medical care and consultations', 'MEDICAL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Emergency Medicine', 'emergency_medicine', 'Emergency medical care and urgent treatments', 'MEDICAL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Radiology', 'radiology', 'Medical imaging and diagnostic services', 'MEDICAL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (code) DO NOTHING;

-- Verify departments were inserted
SELECT * FROM departments;

