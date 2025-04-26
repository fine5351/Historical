#!/bin/bash
echo "Starting Redis Sentinel Docker Compose..."
cd "$(dirname "$0")"
docker-compose up -d
echo "Redis Sentinel Docker Compose started successfully."
echo "Redis Master: localhost:6379"
echo "Redis Slaves: localhost:6380, localhost:6381"
echo "Redis Sentinels: localhost:26379, localhost:26380, localhost:26381"
echo "Password: hackathon"
echo "Master name: mymaster"