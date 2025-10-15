# Healthcare Insurance Claim System - Service Startup Script
Write-Host "Starting Healthcare Insurance Claim System Services..." -ForegroundColor Green
Write-Host ""

$basePath = "C:\Users\pc\Desktop\HealthcareClaimApp\backend"
$mvnwPath = "$basePath\user-service\mvnw.cmd"

# Function to start a service
function Start-Service {
    param(
        [string]$ServiceName,
        [string]$ServicePath,
        [int]$Port,
        [int]$WaitTime = 10
    )
    
    Write-Host "Starting $ServiceName on port $Port..." -ForegroundColor Yellow
    Start-Process -FilePath "cmd" -ArgumentList "/k", "cd /d `"$ServicePath`" && `"$mvnwPath`" spring-boot:run" -WindowStyle Normal
    Write-Host "Waiting $WaitTime seconds..." -ForegroundColor Gray
    Start-Sleep -Seconds $WaitTime
}

# Start services in order
Start-Service -ServiceName "Service Discovery (Eureka)" -ServicePath "$basePath\service-discovery" -Port 8761 -WaitTime 15
Start-Service -ServiceName "User Service" -ServicePath "$basePath\user-service" -Port 8080 -WaitTime 20
Start-Service -ServiceName "Claim Service" -ServicePath "$basePath\claim-service" -Port 8083 -WaitTime 10
Start-Service -ServiceName "Patient Service" -ServicePath "$basePath\patient-service" -Port 8084 -WaitTime 10
Start-Service -ServiceName "Doctor Service" -ServicePath "$basePath\doctor-service" -Port 8085 -WaitTime 10
Start-Service -ServiceName "API Gateway" -ServicePath "$basePath\api-gateway" -Port 8082 -WaitTime 10

Write-Host ""
Write-Host "All services are starting..." -ForegroundColor Green
Write-Host ""
Write-Host "Service URLs:" -ForegroundColor Cyan
Write-Host "- Service Discovery: http://localhost:8761" -ForegroundColor White
Write-Host "- API Gateway: http://localhost:8082" -ForegroundColor White
Write-Host "- User Service: http://localhost:8080" -ForegroundColor White
Write-Host "- Claim Service: http://localhost:8083" -ForegroundColor White
Write-Host "- Patient Service: http://localhost:8084" -ForegroundColor White
Write-Host "- Doctor Service: http://localhost:8085" -ForegroundColor White
Write-Host ""
Write-Host "Wait for all services to start before testing the frontend." -ForegroundColor Yellow
Write-Host "Check the service discovery dashboard at http://localhost:8761" -ForegroundColor Yellow
Write-Host ""
Write-Host "Press any key to continue..." -ForegroundColor Gray
Read-Host
