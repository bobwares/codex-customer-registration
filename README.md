<!--
App: Customer Registration
Package: docs
File: README.md
Version: 0.1.0
Turns: 1
Author: AI Agent
Date: 2025-10-25T09:49:02Z
Exports: Project overview and setup instructions
Description: Describes the Customer Registration service and outlines local environment setup steps.
-->

# Customer Registration Service

The Customer Registration project provides a standardized service for securely onboarding new customers. It manages the registrat
ion lifecycle—from capturing customer details and validating inputs, to persisting records in PostgreSQL and exposing CRUD operat
ions through a REST API.

## Prerequisites

- Docker 24+
- Docker Compose v2
- GNU Make 4+

## Environment Configuration

1. Copy the example environment variables file:

   ```bash
   cp .env.example .env.postgresql
   ```

2. Update `.env.postgresql` with credentials that suit your local environment. The Compose profile expects values for:

   - `COMPOSE_PROJECT_NAME`
   - `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`
   - `DB_HOST`, `DB_PORT`
   - `APP_PORT`

## Running the Database Profile

Use the Makefile targets to manage the PostgreSQL container, run migrations, seed data, and verify the installation.

```bash
# Start the PostgreSQL container profile
make docker-up

# Apply the normalized schema
make db-migrate

# Seed realistic customer records
make db-seed

# Run the smoke-test query (expects ≥ 20 customers)
make db-query

# Tear everything down when finished
make docker-down
```

Additional details on the schema and smoke tests are documented in [`db/README.md`](db/README.md).

