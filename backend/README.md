# Healthcare Insurance Claim System - Backend

## Overview

This is the backend microservices architecture for the Healthcare Insurance Claim Management System. The system has been transformed from an IT Support Ticketing System to a comprehensive healthcare solution.

## Architecture

The system consists of the following microservices:

1. **Service Discovery (Eureka)** - Port 8761
2. **API Gateway** - Port 8082
3. **User Service** - Port 8080
4. **Claim Service** - Port 8083
5. **Patient Service** - Port 8084
6. **Doctor Service** - Port 8085

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Node.js 16+ (for frontend)

## Database Setup

1. Create a PostgreSQL database named `postgres`
2. Update database credentials in `user-service/src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
   spring.datasource.username=postgres
   spring.datasource.password=1234
   ```

## Quick Start

### Windows
```bash
# Run the startup script
start-services.bat
```

### Linux/Mac
```bash
# Make script executable and run
chmod +x start-services.sh
./start-services.sh
```

### Manual Start (if scripts don't work)

Start services in this order:

1. **Service Discovery**
   ```bash
   cd service-discovery
   mvn spring-boot:run
   ```

2. **User Service** (wait 10 seconds after step 1)
   ```bash
   cd user-service
   mvn spring-boot:run
   ```

3. **Claim Service** (wait 15 seconds after step 2)
   ```bash
   cd claim-service
   mvn spring-boot:run
   ```

4. **Patient Service** (wait 10 seconds after step 3)
   ```bash
   cd patient-service
   mvn spring-boot:run
   ```

5. **Doctor Service** (wait 10 seconds after step 4)
   ```bash
   cd doctor-service
   mvn spring-boot:run
   ```

6. **API Gateway** (wait 10 seconds after step 5)
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```

## Service URLs

- **Service Discovery Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8082
- **User Service**: http://localhost:8080
- **Claim Service**: http://localhost:8083
- **Patient Service**: http://localhost:8084
- **Doctor Service**: http://localhost:8085

## API Endpoints

### Authentication (via API Gateway)
- `POST /api/auth/login` - User login
- `POST /api/auth/signup` - User registration
- `POST /api/auth/logout` - User logout
- `POST /api/auth/refresh` - Refresh token

### Healthcare Services (via API Gateway)
- `GET /api/claims` - Get all claims
- `POST /api/claims` - Create new claim
- `GET /api/patients` - Get all patients
- `GET /api/doctors` - Get all doctors

## Frontend Integration

The frontend is configured to connect to the API Gateway on port 8082. Make sure to:

1. Start all backend services first
2. Verify services are registered in Eureka (http://localhost:8761)
3. Start the frontend React Native app

## Healthcare Domain Features

### User Roles
- **Patient**: Submit claims, view medical records, manage appointments
- **Doctor**: Verify claims, manage patients, upload medical records
- **Admin**: System administration, user management, reports
- **Insurance Provider**: Review and approve/reject claims

### Database Schema
The system includes healthcare-specific tables:
- `users` (with healthcare fields)
- `healthcare_providers`
- `medical_records`
- `insurance_claims`
- `claim_documents`
- `appointments`
- `notifications`

## Troubleshooting

### Common Issues

1. **Services not starting**
   - Check if PostgreSQL is running
   - Verify database credentials
   - Check port availability

2. **Service Discovery issues**
   - Ensure Eureka starts first
   - Check network connectivity
   - Verify service registration

3. **Frontend connection issues**
   - Verify API Gateway is running on port 8082
   - Check CORS configuration
   - Ensure all services are registered in Eureka

### Logs
Check individual service logs for detailed error information. Each service runs in its own terminal window.

## Development

### Adding New Services
1. Create new service directory
2. Add to parent `pom.xml` modules
3. Update API Gateway routes
4. Add service to startup scripts

### Database Migrations
Database migrations are handled by Flyway in the user-service. New migrations should be added to `user-service/src/main/resources/db/migration/`.

## Production Deployment

For production deployment:
1. Use external PostgreSQL database
2. Configure proper JWT secrets
3. Set up load balancer
4. Configure monitoring and logging
5. Set up CI/CD pipeline

## Support

For issues or questions, check the service logs and ensure all prerequisites are met.
