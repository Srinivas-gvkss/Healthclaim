@echo off
echo Starting Healthcare Insurance Claim System Services with Maven Wrapper...
echo.

echo Starting Service Discovery (Eureka) on port 8761...
start "Service Discovery" cmd /k "cd /d %~dp0service-discovery && .\mvnw.cmd spring-boot:run"

timeout /t 15 /nobreak >nul

echo Starting User Service on port 8080...
start "User Service" cmd /k "cd /d %~dp0user-service && .\mvnw.cmd spring-boot:run"

timeout /t 20 /nobreak >nul

echo Starting Claim Service on port 8083...
start "Claim Service" cmd /k "cd /d %~dp0claim-service && .\mvnw.cmd spring-boot:run"

timeout /t 10 /nobreak >nul

echo Starting Patient Service on port 8084...
start "Patient Service" cmd /k "cd /d %~dp0patient-service && .\mvnw.cmd spring-boot:run"

timeout /t 10 /nobreak >nul

echo Starting Doctor Service on port 8085...
start "Doctor Service" cmd /k "cd /d %~dp0doctor-service && .\mvnw.cmd spring-boot:run"

timeout /t 10 /nobreak >nul

echo Starting Notification Service on port 8086...
start "Notification Service" cmd /k "cd /d %~dp0notification-service && .\mvnw.cmd spring-boot:run"

timeout /t 10 /nobreak >nul

echo Starting Reporting Service on port 8087...
start "Reporting Service" cmd /k "cd /d %~dp0reporting-service && .\mvnw.cmd spring-boot:run"

timeout /t 10 /nobreak >nul

echo Starting API Gateway on port 8082...
start "API Gateway" cmd /k "cd /d %~dp0api-gateway && .\mvnw.cmd spring-boot:run"

echo.
echo All services are starting...
echo.
echo Service URLs:
echo - Service Discovery: http://localhost:8761
echo - API Gateway: http://localhost:8082
echo - User Service: http://localhost:8080
echo - Claim Service: http://localhost:8083
echo - Patient Service: http://localhost:8084
echo - Doctor Service: http://localhost:8085
echo - Notification Service: http://localhost:8086
echo - Reporting Service: http://localhost:8087
echo.
echo Wait for all services to start before testing the frontend.
echo Check the service discovery dashboard at http://localhost:8761
echo.
echo Press any key to continue...
pause
