# Customer Registration Service

A Spring Boot service that manages the lifecycle of customer registrations. It exposes REST APIs for creating, retrieving, updating, listing, and deleting customer records while persisting data with JPA.

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.9+

### Running Locally

```bash
./mvnw spring-boot:run
```

The application starts on <http://localhost:8080> and exposes a health endpoint at `/health`.

### Running Tests

```bash
./mvnw test
```

### Docker

A `Dockerfile` is provided for containerized builds. To run with PostgreSQL locally, use the supplied `docker-compose.yml` file:

```bash
export DB_NAME=registration
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export COMPOSE_PROJECT_NAME=customer-registration
docker compose up --build
```

The application will be available on port `8080`.
