# Customer Registration

Customer Registration is a Spring Boot service that captures and manages customer onboarding data.

## Features
- Spring Boot 3.5, Java 21
- PostgreSQL persistence with Liquibase migrations
- OpenAPI documentation via Springdoc
- Testcontainers-powered unit and integration tests

## Getting Started
1. Export the required environment variables (examples shown below) or create a `.env` file:
   ```bash
   export APP_NAME=customer-registration
   export APP_PORT=8080
   export DATABASE_HOST=localhost
   export DATABASE_PORT=5432
   export DATABASE_NAME=customers
   export DATABASE_USERNAME=postgres
   export DATABASE_PASSWORD=postgres
   export APP_DEFAULT_TAX_RATE=0
   export APP_DEFAULT_SHIPPING_COST=0
   export APP_SUPPORTED_CURRENCIES=USD
   ```
2. Install Java 21 and Maven 3.9+ locally.
3. Build and test the project:
   ```bash
   mvn clean verify
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

OpenAPI UI is available at `http://localhost:${APP_PORT}/swagger-ui.html`.

## Docker Compose
A local PostgreSQL instance is provided via `docker-compose.yml`.

1. Create a `.env` file in the project root with the variables listed above.
2. Start the database:
   ```bash
   docker compose up -d postgres
   ```
3. Stop and remove containers when finished:
   ```bash
   docker compose down
   ```

Database files are stored in a named volume (`pgdata`). Initialization scripts can be added under `db/init`.

## Testing
- `mvn test` runs unit and integration tests.
- E2E HTTP collections are provided in the `e2e` directory for manual verification.

## Configuration
See [README-config.md](README-config.md) for property details and wrapper guidance.
