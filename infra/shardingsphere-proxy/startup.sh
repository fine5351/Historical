echo "Starting ShardingSphere-Proxy Docker Compose..."
docker-compose up -d
sleep 10
docker logs shardingsphere-proxy