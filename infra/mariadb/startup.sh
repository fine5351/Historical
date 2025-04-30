# 创建基本的 bridge 网络
docker network create shared_network

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