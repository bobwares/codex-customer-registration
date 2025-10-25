<!--
App: Customer Registration
Package: db
File: README.md
Version: 0.1.0
Turns: 1
Author: AI Agent
Date: 2025-10-25T09:49:02Z
Exports: Database documentation
Description: Explains how to apply migrations, seed test data, and execute smoke tests for the PostgreSQL profile.
-->

# Database Operations

## Domain Migration

Use the provided Makefile targets to bootstrap PostgreSQL with the normalized customer registration schema.

```bash
# Start the PostgreSQL container (profile: postgresql)
make docker-up

# Apply migration scripts
make db-migrate

# Seed reference data and run the smoke-test query
make db-seed
make db-query
```

The commands rely on `.env.postgresql`. Update the file (or create a copy from `.env.example`) with credentials that match your local setup.

## Smoke Testing

The `make db-query` target executes `SELECT COUNT(*) AS customer_count FROM customer_registration.customer;` to verify that at least 20 records are available after seeding. Adjust or extend the query as needed for additional validation.

