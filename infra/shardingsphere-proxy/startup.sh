echo "Starting ShardingSphere-Proxy Docker Compose..."
docker-compose up -d
sleep 5
docker logs shardingsphere-proxy