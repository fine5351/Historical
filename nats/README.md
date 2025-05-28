# NATS Module

## Overview

This module provides a Spring Boot application with examples of NATS producer and consumer functionalities. It includes:

*   A NATS producer service (`NatsProducerService`).
*   A NATS consumer service (`NatsConsumerService`) that subscribes to a topic.
*   A Spring REST Controller (`NatsController`) with an API endpoint to publish messages to NATS.
*   Aspect-Oriented Programming (AOP) for structured logging of NATS operations (publishing and consumption).
*   Integration with the `normal-core` module (dependency declared, specific usage would be based on `normal-core`'s capabilities).

## Prerequisites

*   Java 21 (as per project parent POM).
*   Maven.
*   A running NATS server. The default configuration expects the server at `nats://localhost:4222`. This can be changed in `src/main/resources/application.properties`. (The `infra/nats/docker-compose.yml` in the root project can be used to start one: `docker-compose -f infra/nats/docker-compose.yml up -d`).

## Building the Module

To build the module, navigate to the root project directory and run:

```bash
mvn clean install
```
Or, to build only this module (after the parent and `normal-core` are built):
```bash
cd nats
mvn clean install
```

## Running the Application

Once built, you can run the Spring Boot application using:

```bash
cd nats
mvn spring-boot:run
```
Alternatively, you can run the packaged JAR (after `mvn package`):
```bash
java -jar target/nats-1.0.0.jar 
```
(Adjust JAR name based on actual artifactId and version from `nats/pom.xml`. `nats-1.0.0.jar` is based on current POM settings).

## NATS Configuration

*   **Producer:** Messages are published via the API.
*   **Consumer:** The `NatsConsumerService` subscribes to the subject: `com.finekuo.nats.example.topic` using the queue group `finekuo-nats-app-group`.
*   **AOP Logging:** NATS publish and consume operations are logged with details including subject, payload summary, and success/failure. Check application logs for these entries.

## API Endpoint

To publish a message to NATS, send a POST request to the following endpoint:

*   **URL:** `POST /api/nats/publish`
*   **Request Body (JSON):**
    ```json
    {
        "subject": "your.target.subject",
        "message": "Hello NATS from API!"
    }
    ```
*   **Success Response (200 OK):**
    ```text
    Event published successfully to subject: your.target.subject
    ```
*   **Example using cURL:**
    ```bash
    curl -X POST -H "Content-Type: application/json"     -d '{"subject":"com.finekuo.nats.test", "message":"Hello via curl"}'     http://localhost:8080/api/nats/publish
    ```
    (Ensure the application is running and listening on port 8080, or adjust as needed. Spring Boot's default port is 8080).

## Logging

The application uses SLF4J with Logback for logging. NATS operations are logged via AOP.
Default logging level for `com.finekuo.nats` is set to `DEBUG` in `application.properties`.
NATS client library (`io.nats`) logging is set to `INFO`.
