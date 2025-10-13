-- =============================================================================
-- IT Support Ticketing System - Database Migration V2
-- Add performance indexes for authentication and user queries
-- =============================================================================

-- Add composite indexes for authentication queries
-- This index will speed up the findByEmailOrUsernameWithRolesAndDepartment query
CREATE INDEX IF NOT EXISTS idx_users_email_username_composite ON users(email, username);

-- Add index for user status and enabled fields (commonly used in authentication)
CREATE INDEX IF NOT EXISTS idx_users_status_enabled ON users(status, enabled);

-- Add index for account lock status (used in authentication checks)
CREATE INDEX IF NOT EXISTS idx_users_account_status ON users(enabled, account_non_locked, credentials_non_expired);

-- Add composite index for user_roles to optimize role loading
CREATE INDEX IF NOT EXISTS idx_user_roles_user_active ON user_roles(user_id, is_active);

-- Add index for role code lookups
CREATE INDEX IF NOT EXISTS idx_user_roles_role_active ON user_roles(role_id, is_active);

-- Add index for department lookups
CREATE INDEX IF NOT EXISTS idx_users_department_status ON users(department_id, status);

-- Add index for last login tracking (useful for analytics)
CREATE INDEX IF NOT EXISTS idx_users_last_login ON users(last_login_at);

-- Add index for failed login attempts (security monitoring)
CREATE INDEX IF NOT EXISTS idx_users_failed_logins ON users(failed_login_attempts);

-- Add comments for documentation
COMMENT ON INDEX idx_users_email_username_composite IS 'Composite index for email/username authentication queries';
COMMENT ON INDEX idx_users_status_enabled IS 'Index for user status and enabled field combinations';
COMMENT ON INDEX idx_users_account_status IS 'Index for account lock and credential status checks';
COMMENT ON INDEX idx_user_roles_user_active IS 'Index for active user role lookups';
COMMENT ON INDEX idx_user_roles_role_active IS 'Index for active role assignments';
COMMENT ON INDEX idx_users_department_status IS 'Index for department-based user queries';
COMMENT ON INDEX idx_users_last_login IS 'Index for last login tracking and analytics';
COMMENT ON INDEX idx_users_failed_logins IS 'Index for security monitoring of failed login attempts';
