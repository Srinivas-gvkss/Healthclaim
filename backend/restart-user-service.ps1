# Restart User Service to apply database migrations
Write-Host "=== Restarting User Service ===" -ForegroundColor Green
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

Write-Host "Stopping User Service..." -ForegroundColor Red
Stop-ServiceOnPort -Port 8080 -ServiceName "User Service"

Write-Host ""
Write-Host "Waiting 5 seconds for process to stop..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

Write-Host ""
Write-Host "Starting User Service with new database migration..." -ForegroundColor Green
Start-Process -FilePath "cmd" -ArgumentList "/c", "cd /d `"$PWD\user-service`" && .\mvnw.cmd spring-boot:run" -WindowStyle Normal

Write-Host ""
Write-Host "User Service restart initiated. Wait 30 seconds for it to start up and apply migrations." -ForegroundColor Cyan
Write-Host "You can check the status at: http://localhost:8080/api/health" -ForegroundColor White
Write-Host ""
Write-Host "After restart, test doctor signup - the 'roles not found doctor' error should be resolved!" -ForegroundColor Green
