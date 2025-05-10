# 检查网络是否已存在，如不存在则创建
if ! docker network ls | grep -q shared_network; then
  echo "Creating shared_network..."
  # 创建带有子网和网关配置的网络
  docker network create \
  --driver bridge \
  --subnet 172.20.0.0/16 \
  --gateway 172.20.0.1 \
  shared_network
else
  echo "Network shared_network already exists. Skipping creation."
fi

echo "Starting MariaDB Docker Compose..."
docker-compose up -d