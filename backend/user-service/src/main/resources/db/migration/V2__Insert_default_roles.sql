-- Insert default roles for Healthcare Insurance Claim System
-- Aligned with frontend role definitions

-- Patient role
INSERT INTO roles (name, code, description, access_level, is_active, created_at, updated_at)
VALUES ('Patient', 'PATIENT', 'Can submit and track personal insurance claims', 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Doctor role
INSERT INTO roles (name, code, description, access_level, is_active, created_at, updated_at)
VALUES ('Doctor', 'DOCTOR', 'Healthcare provider who can verify and support patient claims', 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Admin role
INSERT INTO roles (name, code, description, access_level, is_active, created_at, updated_at)
VALUES ('Admin', 'ADMIN', 'Platform administrator with management capabilities', 3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insurance Provider role
INSERT INTO roles (name, code, description, access_level, is_active, created_at, updated_at)
VALUES ('Insurance Provider', 'INSURANCE_PROVIDER', 'Reviews and approves insurance claims', 4, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
