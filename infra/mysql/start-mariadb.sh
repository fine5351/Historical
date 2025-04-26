#!/bin/bash
echo "Starting MariaDB Docker Compose..."
cd "$(dirname "$0")"
docker-compose up -d
echo "MariaDB Docker Compose started successfully."
echo "You can access MariaDB at localhost:3306"
echo "Username: root"
echo "Password: 123456"
echo "Database: hackathon"