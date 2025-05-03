
# 檢查是否已經有 shared_network 存在
if docker network ls | grep -q "shared_network"; then
    echo "Network shared_network already exists."
else
    echo "Creating shared_network..."
fi

# 创建带有子网和网关配置的网络
docker network create \
--driver bridge \
--subnet 172.20.0.0/16 \
--gateway 172.20.0.1 \
shared_network

echo "Starting MariaDB Docker Compose..."
docker-compose up -d
sleep 5
docker logs mariadb