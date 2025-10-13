-- Insert default departments for Healthcare Insurance Claim System
-- Simplified organizational structure

-- General Department
INSERT INTO departments (name, code, description, department_type, is_active, created_at, updated_at)
VALUES ('General', 'GENERAL', 'General operations and services', 'GENERAL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Administration Department
INSERT INTO departments (name, code, description, department_type, is_active, created_at, updated_at)
VALUES ('Administration', 'ADMINISTRATION', 'Administrative and management operations', 'ADMINISTRATION', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Claims Processing Department
INSERT INTO departments (name, code, description, department_type, is_active, created_at, updated_at)
VALUES ('Claims Processing', 'CLAIMS_PROCESSING', 'Insurance claims processing and review', 'CLAIMS_PROCESSING', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
