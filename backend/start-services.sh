#!/bin/bash

echo "Starting Healthcare Insurance Claim System Services..."
echo

echo "Starting Service Discovery (Eureka) on port 8761..."
cd service-discovery
mvn spring-boot:run &
SERVICE_DISCOVERY_PID=$!
cd ..

sleep 10

echo "Starting User Service on port 8080..."
cd user-service
mvn spring-boot:run &
USER_SERVICE_PID=$!
cd ..

sleep 15

echo "Starting Claim Service on port 8083..."
cd claim-service
mvn spring-boot:run &
CLAIM_SERVICE_PID=$!
cd ..

sleep 10

echo "Starting Patient Service on port 8084..."
cd patient-service
mvn spring-boot:run &
PATIENT_SERVICE_PID=$!
cd ..

sleep 10

echo "Starting Doctor Service on port 8085..."
cd doctor-service
mvn spring-boot:run &
DOCTOR_SERVICE_PID=$!
cd ..

sleep 10

echo "Starting API Gateway on port 8082..."
cd api-gateway
mvn spring-boot:run &
API_GATEWAY_PID=$!
cd ..

echo
echo "All services are starting..."
echo
echo "Service URLs:"
echo "- Service Discovery: http://localhost:8761"
echo "- API Gateway: http://localhost:8082"
echo "- User Service: http://localhost:8080"
echo "- Claim Service: http://localhost:8083"
echo "- Patient Service: http://localhost:8084"
echo "- Doctor Service: http://localhost:8085"
echo
echo "Wait for all services to start before testing the frontend."
echo "Check the service discovery dashboard at http://localhost:8761"
echo
echo "Press Ctrl+C to stop all services"

# Function to cleanup processes on exit
cleanup() {
    echo "Stopping all services..."
    kill $SERVICE_DISCOVERY_PID 2>/dev/null
    kill $USER_SERVICE_PID 2>/dev/null
    kill $CLAIM_SERVICE_PID 2>/dev/null
    kill $PATIENT_SERVICE_PID 2>/dev/null
    kill $DOCTOR_SERVICE_PID 2>/dev/null
    kill $API_GATEWAY_PID 2>/dev/null
    exit
}

# Set trap to cleanup on script exit
trap cleanup SIGINT SIGTERM

# Wait for all background processes
wait
