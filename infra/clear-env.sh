# 停止所有 container
docker stop $(docker ps -aq)
# 删除所有 container
docker rm $(docker ps -aq)
# 删除所有 image
docker rmi $(docker images -q)
# 删除所有 network
docker network rm $(docker network ls -q)
# 删除所有 volume
docker volume rm $(docker volume ls -q)