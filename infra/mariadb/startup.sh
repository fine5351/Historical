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

# 等待 MariaDB 完全启动并可接受连接
echo "Waiting for MariaDB to be ready..."
MAX_TRIES=30
COUNT=0
while ! docker exec mariadb mysqladmin -uroot -p123456 ping --silent; do
  COUNT=$((COUNT+1))
  if [ $COUNT -ge $MAX_TRIES ]; then
    echo "MariaDB did not become ready in time. Exiting."
    exit 1
  fi
  echo "Waiting for MariaDB to be ready... ($COUNT/$MAX_TRIES)"
  sleep 2
done

echo "MariaDB is ready! Executing SQL files..."

# 手动执行 SQL 文件（先执行 schema.sql，再执行 data.sql）
docker exec -i mariadb mysql -uroot -p123456 < ./init/schema.sql
echo "Schema creation completed."

docker exec -i mariadb mysql -uroot -p123456 < ./init/data.sql
echo "Data insertion completed."

echo "Setup completed successfully!"
docker logs mariadb
