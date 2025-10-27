<!--
App: Customer Registration
Package: root
File: README.md
Version: 0.1.0
Turns: 1
Author: ChatGPT Codex
Date: 2025-02-14T00:00:00Z
Exports: project-readme
Description: Project overview and local development instructions for the Customer Registration service.
-->
# Customer Registration

Customer Registration is a Spring Boot service that securely captures, validates, and manages new customer data. It exposes REST APIs for onboarding workflows and downstream integrations.

## Prerequisites
- Java 21
- Maven 3.9+
- Docker & Docker Compose (for optional PostgreSQL container)

## Local Configuration
1. Copy `.env.example` to `.env.postgresql` and adjust credentials as needed.
2. Export runtime variables when launching the application:
   ```bash
   export APP_NAME=customer-registration
   export APP_PORT=8080
   export APP_DEFAULT_TAX_RATE=0.0000
   export APP_DEFAULT_SHIPPING_COST=0.00
   export APP_SUPPORTED_CURRENCIES=USD,EUR
   export DATABASE_HOST=localhost
   export DATABASE_PORT=5432
   export DATABASE_NAME=customer_registration
   export DATABASE_USERNAME=postgres
   export DATABASE_PASSWORD=postgres
   ```

## Database Tooling
Use the provided Makefile targets to manage PostgreSQL locally:

```bash
make up       # start the postgres profile
make migrate  # apply schema objects from db/migrations
make seed     # insert 20 sample customer records
make status   # show running containers
make down     # stop and remove the postgres profile
```

The database assets live under `db/` and include normalized DDL, seed data, and operational notes.

## Running the Application
```bash
mvn -q -DskipTests=false clean verify
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## API Discovery
- Health: `GET /actuator/health`
- Metadata: `GET /meta/env`
- OpenAPI UI: `GET /swagger-ui.html`

An HTTP scratch file is available at `e2e/actuator.http` for quick smoke tests.
