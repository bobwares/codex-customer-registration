# Customer Registration

Customer Registration securely captures, validates, and persists onboarding data for new customers. It exposes a standards-based REST API backed by Spring Boot MVC, Spring Data JPA, and PostgreSQL.

## Features
- Java 21 + Spring Boot 3.3
- Validated configuration properties (`AppProperties`)
- Liquibase-managed PostgreSQL schema for customers, emails, and phone numbers
- OpenAPI documentation via springdoc
- RESTful CRUD endpoints with comprehensive error handling
- Testcontainers-powered integration tests

## Getting Started
1. Ensure Java 21 and Maven 3.9+ are installed.
2. Export required environment variables before running:
   ```bash
   export APP_NAME=customer-registration
   export APP_PORT=8080
   export DATABASE_HOST=localhost
   export DATABASE_PORT=5432
   export DATABASE_NAME=customer_registration
   export DATABASE_USERNAME=postgres
   export DATABASE_PASSWORD=postgres
   export APP_DEFAULT_TAX_RATE=0.00
   export APP_DEFAULT_SHIPPING_COST=0.00
   export APP_SUPPORTED_CURRENCIES=USD
   ```
3. Build and run tests:
   ```bash
   mvn clean verify
   ```
4. Launch the application:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=local
   ```

## Docker Compose
A PostgreSQL 16 instance is provided via Docker Compose.

1. Create a `.env` file with database credentials:
   ```env
   DATABASE_PORT=5432
   DATABASE_NAME=customer_registration
   DATABASE_USERNAME=postgres
   DATABASE_PASSWORD=postgres
   ```
2. Start the database:
   ```bash
   docker compose up -d postgres
   ```
3. Connect from the application using the same credentials.

## API Reference
- Swagger UI: `http://localhost:${APP_PORT}/swagger-ui.html`
- OpenAPI JSON: `http://localhost:${APP_PORT}/api-docs`
- Health endpoint: `http://localhost:${APP_PORT}/actuator/health`
- Metadata endpoint: `http://localhost:${APP_PORT}/meta/env`

## Testing
- Unit tests: `mvn test`
- Integration flow sample: run `mvn verify` to execute Testcontainers-backed tests.
- Manual E2E: use `e2e/customer.http` with an HTTP client supporting `.http` files.

## Additional Documentation
- Configuration details: see `README-config.md`.
