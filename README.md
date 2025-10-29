<!--
App: Customer Registration
Package: documentation
File: README.md
Version: 0.1.0
Turns: 1
Author: AI Agent
Date: 2025-10-29T22:24:49Z
Exports: project-overview
Description: Project overview and local development instructions.
-->
# Customer Registration

Customer Registration is a Spring Boot service for securely onboarding new customers and exposing
standardized APIs for downstream integrations.

## Local Development

1. Create a `.env` file with the following variables:
   ```bash
   DATABASE_USERNAME=postgres
   DATABASE_PASSWORD=postgres
   DATABASE_NAME=customer_registration
   DATABASE_PORT=5432
   APP_NAME=customer-registration
   APP_PORT=8080
   APP_DEFAULT_TAX_RATE=0.0
   APP_DEFAULT_SHIPPING_COST=0.0
   APP_SUPPORTED_CURRENCIES=["USD"]
   ```
2. Start infrastructure: `docker compose up -d postgres`
3. Run migrations: `psql postgresql://$DATABASE_USERNAME:$DATABASE_PASSWORD@localhost:$DATABASE_PORT/$DATABASE_NAME -f db/migrations/01_customer_profile_tables.sql`
4. Launch the service: `mvn spring-boot:run`

## Docker Compose

The provided `docker-compose.yml` spins up PostgreSQL 16 with health checks and persistent
storage. Mount initial SQL scripts from `db/init` if needed. Ensure the `.env` file is present to
configure credentials and ports.

## Testing

Run the full test suite with:

```bash
mvn -q -DskipTests=false clean verify
```
