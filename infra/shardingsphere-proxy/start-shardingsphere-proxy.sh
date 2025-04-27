#!/bin/sh
echo "Starting ShardingSphere-Proxy Docker Compose..."
# Get the directory of the script and change to it
# Run docker-compose
docker-compose up -d
echo "ShardingSphere-Proxy Docker Compose started successfully."
echo "You can access ShardingSphere-Proxy at localhost:3307"
echo "Username: root"
echo "Password: 123456"
echo "Database: hackathon"
