# Database Migrations

This directory contains database migration scripts for the IT Support Ticketing System.

## Migration Files

### V1__Create_initial_tables.sql
- Creates all initial database tables (departments, roles, users, user_roles)
- Sets up indexes for better performance
- Adds table and column comments for documentation

### V2__Insert_default_roles.sql
- Inserts default system roles with predefined permissions
- Roles: END_USER, SUPPORT_AGENT, TEAM_LEAD, DEPARTMENT_HEAD, SYSTEM_ADMIN
- Each role has appropriate access level (1-5) and permissions

### V3__Insert_default_departments.sql
- Inserts default organizational departments
- Departments: IT Infrastructure, Application Support, Security Team, SAP Support, General Support
- Each department has contact information and location details

## Migration Execution

These migrations are executed automatically when the application starts up using Flyway (if configured) or can be run manually.

### Manual Execution
```sql
-- Run migrations in order:
\i V1__Create_initial_tables.sql
\i V2__Insert_default_roles.sql
\i V3__Insert_default_departments.sql
```

### Using Flyway (Recommended)
Add Flyway dependency to pom.xml and configure in application.properties:
```properties
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

## Data Initialization Notes

- **Previous DataInitializer.java has been disabled** - data initialization is now handled through database migrations
- Migrations are idempotent - can be run multiple times safely
- Default data is inserted only if it doesn't already exist (ON CONFLICT DO NOTHING)
- System-defined roles and departments cannot be deleted through the application

## Adding New Migrations

1. Create new migration file with version number: `V{version}__{description}.sql`
2. Use incremental version numbers
3. Make migrations idempotent where possible
4. Add appropriate comments and documentation
