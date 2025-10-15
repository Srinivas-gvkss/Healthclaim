@echo off
echo Starting Healthcare Insurance Claim System Services...
echo.

echo Starting Service Discovery (Eureka) on port 8761...
start "Service Discovery" cmd /k "cd service-discovery && ..\user-service\mvnw.cmd spring-boot:run"

timeout /t 10 /nobreak >nul

echo Starting User Service on port 8080...
start "User Service" cmd /k "cd user-service && .\mvnw.cmd spring-boot:run"

timeout /t 15 /nobreak >nul

echo Starting Claim Service on port 8083...
start "Claim Service" cmd /k "cd claim-service && ..\user-service\mvnw.cmd spring-boot:run"

timeout /t 10 /nobreak >nul

echo Starting Patient Service on port 8084...
start "Patient Service" cmd /k "cd patient-service && ..\user-service\mvnw.cmd spring-boot:run"

timeout /t 10 /nobreak >nul

echo Starting Doctor Service on port 8085...
start "Doctor Service" cmd /k "cd doctor-service && ..\user-service\mvnw.cmd spring-boot:run"

timeout /t 10 /nobreak >nul

echo Starting API Gateway on port 8082...
start "API Gateway" cmd /k "cd api-gateway && ..\user-service\mvnw.cmd spring-boot:run"

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
echo.
echo Wait for all services to start before testing the frontend.
echo Check the service discovery dashboard at http://localhost:8761
echo.
pause
