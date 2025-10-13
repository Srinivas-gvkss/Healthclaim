-- =============================================================================
-- IT Support Ticketing System - Database Migration V4
-- Setup initial system configuration
-- =============================================================================

-- Note: No hardcoded test users created in migration
-- Users should be created through the application signup process
-- or admin panel for better security

-- Add comment
COMMENT ON TABLE users IS 'User table configured for the ticketing system';
