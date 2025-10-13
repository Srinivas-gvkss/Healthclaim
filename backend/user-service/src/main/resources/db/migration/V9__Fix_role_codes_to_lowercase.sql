-- Fix role codes to use lowercase (matching UserRole enum)
-- This aligns database with frontend role definitions

UPDATE roles SET code = 'patient' WHERE code = 'PATIENT';
UPDATE roles SET code = 'doctor' WHERE code = 'DOCTOR';
UPDATE roles SET code = 'admin' WHERE code = 'ADMIN';
UPDATE roles SET code = 'insurance_provider' WHERE code = 'INSURANCE_PROVIDER';

