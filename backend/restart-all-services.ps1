# Comprehensive service restart script
Write-Host "=== Healthcare Claim App - Service Restart ===" -ForegroundColor Green
Write-Host ""

# Function to stop processes on specific ports
function Stop-ServiceOnPort {
    param([int]$Port, [string]$ServiceName)
    
    $processes = Get-NetTCPConnection -LocalPort $Port -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess
    if ($processes) {
        foreach ($process in $processes) {
            Write-Host "Stopping $ServiceName (PID: $process) on port $Port..." -ForegroundColor Yellow
            Stop-Process -Id $process -Force -ErrorAction SilentlyContinue
        }
        Start-Sleep -Seconds 3
    } else {
        Write-Host "$ServiceName not running on port $Port" -ForegroundColor Gray
    }
}

Write-Host "Stopping all services..." -ForegroundColor Red

# Stop all services in reverse order
Stop-ServiceOnPort -Port 8082 -ServiceName "API Gateway"
Stop-ServiceOnPort -Port 8087 -ServiceName "Reporting Service"
Stop-ServiceOnPort -Port 8086 -ServiceName "Notification Service"
Stop-ServiceOnPort -Port 8085 -ServiceName "Doctor Service"
Stop-ServiceOnPort -Port 8084 -ServiceName "Patient Service"
Stop-ServiceOnPort -Port 8083 -ServiceName "Claim Service"
Stop-ServiceOnPort -Port 8080 -ServiceName "User Service"
Stop-ServiceOnPort -Port 8761 -ServiceName "Service Discovery"

Write-Host ""
Write-Host "Waiting 10 seconds for all processes to stop..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host ""
Write-Host "Starting services in correct order..." -ForegroundColor Green

# Start services in correct order
Write-Host "1. Starting Service Discovery (Eureka) on port 8761..." -ForegroundColor Cyan
Start-Process -FilePath "cmd" -ArgumentList "/c", "cd /d `"$PWD\service-discovery`" && .\mvnw.cmd spring-boot:run" -WindowStyle Normal
Start-Sleep -Seconds 15

Write-Host "2. Starting User Service on port 8080..." -ForegroundColor Cyan
Start-Process -FilePath "cmd" -ArgumentList "/c", "cd /d `"$PWD\user-service`" && .\mvnw.cmd spring-boot:run" -WindowStyle Normal
Start-Sleep -Seconds 20

Write-Host "3. Starting Claim Service on port 8083..." -ForegroundColor Cyan
Start-Process -FilePath "cmd" -ArgumentList "/c", "cd /d `"$PWD\claim-service`" && .\mvnw.cmd spring-boot:run" -WindowStyle Normal
Start-Sleep -Seconds 10

Write-Host "4. Starting Patient Service on port 8084..." -ForegroundColor Cyan
Start-Process -FilePath "cmd" -ArgumentList "/c", "cd /d `"$PWD\patient-service`" && .\mvnw.cmd spring-boot:run" -WindowStyle Normal
Start-Sleep -Seconds 10

Write-Host "5. Starting Doctor Service on port 8085..." -ForegroundColor Cyan
Start-Process -FilePath "cmd" -ArgumentList "/c", "cd /d `"$PWD\doctor-service`" && .\mvnw.cmd spring-boot:run" -WindowStyle Normal
Start-Sleep -Seconds 10

Write-Host "6. Starting Notification Service on port 8086..." -ForegroundColor Cyan
Start-Process -FilePath "cmd" -ArgumentList "/c", "cd /d `"$PWD\notification-service`" && .\mvnw.cmd spring-boot:run" -WindowStyle Normal
Start-Sleep -Seconds 10

Write-Host "7. Starting Reporting Service on port 8087..." -ForegroundColor Cyan
Start-Process -FilePath "cmd" -ArgumentList "/c", "cd /d `"$PWD\reporting-service`" && .\mvnw.cmd spring-boot:run" -WindowStyle Normal
Start-Sleep -Seconds 10

Write-Host "8. Starting API Gateway on port 8082..." -ForegroundColor Cyan
Start-Process -FilePath "cmd" -ArgumentList "/c", "cd /d `"$PWD\api-gateway`" && .\mvnw.cmd spring-boot:run" -WindowStyle Normal

Write-Host ""
Write-Host "=== All services are starting ===" -ForegroundColor Green
Write-Host ""
Write-Host "Service URLs:" -ForegroundColor White
Write-Host "- Service Discovery: http://localhost:8761" -ForegroundColor Gray
Write-Host "- API Gateway: http://localhost:8082" -ForegroundColor Gray
Write-Host "- User Service: http://localhost:8080" -ForegroundColor Gray
Write-Host "- Claim Service: http://localhost:8083" -ForegroundColor Gray
Write-Host "- Patient Service: http://localhost:8084" -ForegroundColor Gray
Write-Host "- Doctor Service: http://localhost:8085" -ForegroundColor Gray
Write-Host "- Notification Service: http://localhost:8086" -ForegroundColor Gray
Write-Host "- Reporting Service: http://localhost:8087" -ForegroundColor Gray
Write-Host ""
Write-Host "Wait 60 seconds for all services to fully start, then test:" -ForegroundColor Yellow
Write-Host "- Service Discovery: http://localhost:8761" -ForegroundColor White
Write-Host "- API Gateway Health: http://localhost:8082/health" -ForegroundColor White
Write-Host "- API Gateway → User Service: http://localhost:8082/api/users/health" -ForegroundColor White
Write-Host ""
Write-Host "Press any key to continue..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
