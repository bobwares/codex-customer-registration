<!--
App: Customer Registration
Package: db
File: README.md
Version: 0.1.0
Turns: 1
Author: ChatGPT Codex
Date: 2025-02-14T00:00:00Z
Exports: db-docs
Description: Operational guidance for database migrations and test data seeding.
-->
# Customer Registration Database Assets

## Domain Migration

Run the normalized Customer Profile migration before starting the Spring Boot application:

```bash
psql "$DATABASE_URL" -f db/migrations/01_customer_profile_tables.sql
```

The script is idempotent—tables are created with `IF NOT EXISTS` and indexes guard duplicates.

## Test Data

Seed the domain with representative records:

```bash
psql "$DATABASE_URL" -f db/scripts/customer_profile_test_data.sql
```

The script uses `ON CONFLICT DO NOTHING` for safe re-runs and finishes with a smoke-test query.
