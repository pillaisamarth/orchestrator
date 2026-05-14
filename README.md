# Event-Driven Order Processing System

A distributed microservices demo build with Spring boot and Kafka, demonstrating event driven architecture and resilience.

## Architecture

```mermaid
graph TD
    A[Client] --> |POST /orders| B[Order Service]
    B --> |Rate Limited| C[Kafka - order-placed]
    C --> D[Payment Service]
    D --> |Process| E[Success]
    D --> |Failure| F[DLQ - order-placed-dlt]
```

## Features
- Order Service - REST API with Token Bucket rate limiting
- Payment Service - Consumes events with idempotency check
- Kafka integration - Proper JSON serialization/deserialization
- Resilience Patterns:
  - Idempotency (prevents duplicate processing)
  - Retry mechanism
  - Dead Letter Queue (DLQ) for failed messages
- Observability - Basic stats endpoint
- Containerized - Docker + Docker compose setup

## How to run
### Option 1: Docker Compose (Recommended)
```
git clone https://github.com/pillaisamarth/orchestrator.git
cd orchestrator
docker-compose up --build -d
```

### Option 2: Without Docker
1. Start Kafka locally
2. Run Order Service: 
```
cd simple-kafka-order
./mvnw spring-boot:run
```
3. Run Payment Service:
```
cd pmt-svc
./mvnw spring-boot:run
```

## API Endpoints
- Submit order
```shell
curl -X POST http://localhost:8080/orders -H "Content-Type: application/json" -d '{"userId":"user123", "amount":1000.0}'
```
- Get number of successful orders
```shell
curl -X GET http://localhost:8082/stats/success -H "Content-Type: application/json"
```
- Get total number of orders received
```shell
curl -X GET http://localhost:8082/stats/received -H "Content-Type: application/json"
```
- Get number of orders sent to dead letter topic
```shell
curl -X GET http://localhost:8082/stats/dlt -H "Content-Type: application/json"
```

## Topics
- Asynchronous communication using Kafka
- Rate Limiting (Token Bucket algorithm)
- Idempotency to handle duplicate events
- Error handling & Retries
- Dead Letter queue pattern for failed messages
- Consumer Groups and concurrency control
- JSON Serialization

## Tradeoffs
- Chose at-least-once delivery with idempotency over exactly once
- In-memory structures used for demo

## Stack
- Spring Boot + Java
- Apache Kafka
- Docker + Docker compose
- Maven