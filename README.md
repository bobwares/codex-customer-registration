# Customer Registration

Customer Registration securely captures, validates, and manages customer onboarding data using Spring Boot, Spring Data JPA, and PostgreSQL.

## Prerequisites

* Java 21
* Maven 3.9+
* Docker (for optional PostgreSQL container)

## Running Locally

1. Create an `.env` file with the following variables:
   ```env
   APP_NAME=customer-registration
   APP_PORT=8080
   DATABASE_HOST=localhost
   DATABASE_PORT=5432
   DATABASE_NAME=customer_registration
   DATABASE_USERNAME=postgres
   DATABASE_PASSWORD=postgres
   APP_DEFAULT_TAX_RATE=0.07
   APP_DEFAULT_SHIPPING_COST=4.99
   APP_SUPPORTED_CURRENCIES=USD,EUR
   ```
2. Start PostgreSQL via Docker Compose:
   ```bash
   docker compose up -d postgres
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. Access Swagger UI at `http://localhost:8080/swagger-ui.html`.

## Running Tests

Execute the full test suite (includes Testcontainers-backed integration tests):

```bash
mvn clean verify
```

## Docker Compose

The included `docker-compose.yml` starts a PostgreSQL 16 container seeded with the schema defined under `db/init`.

```bash
docker compose up -d postgres
```

Stop and remove the container when finished:

```bash
docker compose down
```

## Additional Documentation

See [README-config.md](README-config.md) for configuration details and Maven wrapper guidance.
