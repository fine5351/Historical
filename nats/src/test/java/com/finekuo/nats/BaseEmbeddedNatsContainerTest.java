package com.finekuo.nats;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class BaseEmbeddedNatsContainerTest {

    protected static GenericContainer<?> natsContainer;
    protected static String natsUrl;
    protected static boolean dockerAvailable = false;

    @BeforeAll
    public static void startNats() {
        try {
            // 检查Docker环境是否可用
            dockerAvailable = isDockerAvailable();
            if (!dockerAvailable) {
                System.out.println("Docker环境不可用，测试将被跳过");
                // 设置一个模拟的URL，这样测试可以继续，但会被跳过
                natsUrl = "nats://localhost:4222";
                return;
            }

            natsContainer = new GenericContainer<>(DockerImageName.parse("nats:2.10.12"))
                    .withExposedPorts(4222)
                    .waitingFor(Wait.forLogMessage(".*Server is ready.*\\n", 1));
            natsContainer.start();
            natsUrl = String.format("nats://localhost:%d", natsContainer.getMappedPort(4222));
            dockerAvailable = true;
        } catch (Exception e) {
            System.err.println("启动NATS容器失败: " + e.getMessage());
            // 提供一个备用URL以便测试可以继续执行
            natsUrl = "nats://localhost:4222";
            dockerAvailable = false;
        }
    }

    /**
     * 检查Docker环境是否可用
     */
    private static boolean isDockerAvailable() {
        try {
            Process process = Runtime.getRuntime().exec("docker info");
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }

    @AfterAll
    public static void stopNats() {
        if (natsContainer != null) {
            natsContainer.stop();
        }
    }

}

