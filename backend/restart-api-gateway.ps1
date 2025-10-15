# Restart API Gateway only
Write-Host "Stopping API Gateway..."

# Find and stop API Gateway process (running on port 8082)
$apiGatewayProcess = Get-NetTCPConnection -LocalPort 8082 -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess
if ($apiGatewayProcess) {
    Stop-Process -Id $apiGatewayProcess -Force
    Write-Host "API Gateway stopped (PID: $apiGatewayProcess)"
    Start-Sleep -Seconds 5
} else {
    Write-Host "API Gateway process not found on port 8082"
}

Write-Host "Starting API Gateway..."
Start-Process -FilePath "cmd" -ArgumentList "/c", "cd /d `"$PWD\api-gateway`" && .\mvnw.cmd spring-boot:run" -WindowStyle Normal

Write-Host "API Gateway restart initiated. Wait 30 seconds for it to start up."
Write-Host "You can check the status at: http://localhost:8082"
