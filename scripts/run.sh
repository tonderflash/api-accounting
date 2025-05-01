#!/bin/sh

set -e

# Wait for dependencies if needed (database, etc.)
echo "Waiting for dependencies..."
sleep 5

# Start the Spring Boot application
echo "Starting Spring Boot application..."
java -jar /app/app.jar
