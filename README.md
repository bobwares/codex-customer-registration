<!--
App: Customer Registration
Package: project-root
File: README.md
Version: 0.1.0
Turns: 1
Author: Bobwares (bobwares@outlook.com)
Date: 2025-10-26T00:40:26Z
Exports: Project overview
Description: Summarizes the Customer Registration service, setup steps, and Docker Compose usage.
-->

# Customer Registration

Customer Registration is a Spring Boot service that captures, validates, and manages customer onboarding data. It exposes RESTful APIs for CRUD operations, generates OpenAPI documentation, and persists data in PostgreSQL via Liquibase-managed migrations.

## Getting Started

1. Copy `.env.example` to `.env` (or use `.env.postgresql`).
2. Launch PostgreSQL locally:

   ```bash
   docker compose --env-file .env.postgresql up -d
   ```

3. Apply migrations and seed data if desired:

   ```bash
   psql "postgresql://$DATABASE_USERNAME:$DATABASE_PASSWORD@$DATABASE_HOST:$DATABASE_PORT/$DATABASE_NAME" \
     -f db/migrations/01_customer_profile_tables.sql
   psql "postgresql://$DATABASE_USERNAME:$DATABASE_PASSWORD@$DATABASE_HOST:$DATABASE_PORT/$DATABASE_NAME" \
     -f db/scripts/customer_profile_test_data.sql
   ```

4. Start the application:

   ```bash
   mvn spring-boot:run
   ```

## Useful URLs

- Health: `http://localhost:8080/actuator/health`
- Customer API: `http://localhost:8080/api/customers`
- OpenAPI UI: `http://localhost:8080/swagger-ui.html`

## Tests

Run the full test suite (includes Testcontainers-backed integration tests):

```bash
mvn clean verify
```
