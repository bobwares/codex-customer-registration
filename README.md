# Customer Registration Service

The Customer Registration service provides REST APIs for onboarding and managing customer profiles. It persists customer data in PostgreSQL using Spring Data JPA and secures schema evolution with Liquibase.

## Features

- CRUD APIs for customer profiles, including email addresses, phone numbers, postal address, and privacy preferences.
- PostgreSQL persistence with Liquibase-managed migrations.
- OpenAPI documentation via Springdoc and operational health checks through Spring Boot Actuator.
- Testcontainers-based integration test for full lifecycle validation.

## Prerequisites

- Java 21
- Docker (for containerized development)
- Maven Wrapper (`./mvnw`) is included.

## Running the Application

```bash
./mvnw spring-boot:run
```

### Using Docker Compose

```bash
export COMPOSE_PROJECT_NAME=customer-registration
export DB_NAME=customer_registration
export DB_USERNAME=customer
export DB_PASSWORD=customer
export APP_PORT=8080

docker compose up --build
```

The API will be available at `http://localhost:8080/api/customers` and the Swagger UI at `http://localhost:8080/swagger-ui.html`.

## Running Tests

```bash
./mvnw test
```

## API Overview

| Method | Endpoint                | Description                  |
| ------ | ----------------------- | ---------------------------- |
| GET    | `/api/customers`        | List all customers           |
| GET    | `/api/customers/{id}`   | Retrieve a customer by ID    |
| POST   | `/api/customers`        | Create a new customer        |
| PUT    | `/api/customers/{id}`   | Update an existing customer  |
| DELETE | `/api/customers/{id}`   | Remove a customer by ID      |

## Database Migration

Liquibase change logs reside in `src/main/resources/db/changelog`. The initial changelog (`db.changelog-001-init.sql`) creates normalized tables for customers, emails, and phone numbers.

## End-to-End Checks

The `CustomerControllerIntegrationTest` spins up a PostgreSQL Testcontainer to ensure the entire REST stack works against a real database instance.
