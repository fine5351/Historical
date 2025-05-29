package com.finekuo.nats;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class BaseEmbeddedNatsContainerTest {

    protected static GenericContainer<?> natsContainer;
    protected static String natsUrl;

    @BeforeAll
    public static void startNats() {
        natsContainer = new GenericContainer<>(DockerImageName.parse("nats:2.10.12"))
                .withExposedPorts(4222)
                .waitingFor(Wait.forLogMessage(".*Server is ready.*\\n", 1));
        natsContainer.start();
        natsUrl = String.format("nats://localhost:%d", natsContainer.getMappedPort(4222));
    }

    @AfterAll
    public static void stopNats() {
        if (natsContainer != null) {
            natsContainer.stop();
        }
    }

}

