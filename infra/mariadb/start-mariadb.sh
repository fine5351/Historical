#!/bin/sh
echo "Starting MariaDB Docker Compose..."
# Get the directory of the script and change to it
SCRIPT_DIR=$(dirname "$0")
cd "$SCRIPT_DIR"
# Run docker-compose
docker-compose up -d
echo "MariaDB Docker Compose started successfully."
echo "You can access MariaDB at localhost:3306"
echo "Username: root"
echo "Password: 123456"
echo "Database: hackathon"
