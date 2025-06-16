# Docker 环境配置指南

## 测试容器需求

项目使用TestContainers框架进行集成测试，需要在本地环境中正确配置Docker。

## Docker配置步骤

### Windows用户

1. 安装Docker Desktop for Windows
    - 下载地址：https://www.docker.com/products/docker-desktop
    - 确保系统支持硬件虚拟化（通常需要在BIOS中启用）
    - 安装时选择合适的WSL 2后端或Hyper-V后端

2. 启动Docker Desktop并验证
    - 打开命令行，运行 `docker info` 应显示Docker环境信息
    - 运行 `docker run hello-world` 测试Docker是否工作正常

### Linux用户

```bash
# 安装Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# 将当前用户添加到docker组（避免每次使用sudo）
sudo usermod -aG docker $USER

# 重新登录或执行以下命令应用组更改
newgrp docker

# 测试安装
docker run hello-world
```

### MacOS用户

1. 安装Docker Desktop for Mac
    - 下载地址：https://www.docker.com/products/docker-desktop
    - 安装并启动Docker Desktop

2. 验证安装
    - 打开终端，运行 `docker info`
    - 运行 `docker run hello-world`

## 常见问题

1. **Docker守护进程未运行**
    - Windows/Mac: 启动Docker Desktop应用
    - Linux: `sudo systemctl start docker`

2. **权限问题**
    - Linux: 确保用户在docker组中 `sudo usermod -aG docker $USER`

3. **内存限制**
    - 在Docker Desktop设置中增加分配的内存

4. **网络问题**
    - 确保没有防火墙阻止Docker网络
    - 检查Docker网络配置

## 测试容器跳过策略

如果您不想或无法设置Docker环境，可以修改测试类使用条件测试：

```java

@Test
void testWithContainer() {
    // 如果Docker不可用，跳过测试
    org.junit.jupiter.api.Assumptions.assumeTrue(isDockerAvailable(),
            "跳过测试，Docker不可用");

    // 测试代码...
}
```
