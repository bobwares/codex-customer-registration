# Customer Registration

Customer Registration is a Spring Boot service that securely captures, validates, and manages new customer data while exposing standardized REST APIs for onboarding and downstream integrations.

## Prerequisites

- Java 21
- Maven 3.9+
- Docker (for running PostgreSQL locally via Compose)

## Building and Testing

```bash
mvn -q -DskipTests=false clean verify
```

Run only unit tests:

```bash
mvn -q test
```

## Running the Application

1. Export the required environment variables (see `README-config.md`).
2. Start the application:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=local
   ```
3. OpenAPI UI is available at `http://localhost:8080/swagger-ui.html` once the app starts.

## Docker Compose

A PostgreSQL container is available for local development.

```bash
docker compose up -d postgres
```

The service reads credentials from the `.env` file referenced in `docker-compose.yml`. The compose file also mounts `db/init` for bootstrapping schema objects.

## End-to-End Requests

HTTP request collections live in the `e2e/` directory. They cover actuator checks and a full customer CRUD flow.

## Database Migrations

Liquibase change logs are located under `src/main/resources/db/changelog`. They align with the SQL bootstrap script at `db/init/001-create-customer-tables.sql`.
