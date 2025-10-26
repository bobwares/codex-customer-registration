<!--
App: Customer Registration
Package: db
File: README.md
Version: 0.1.0
Turns: 1
Author: Bobwares (bobwares@outlook.com)
Date: 2025-10-26T00:40:26Z
Exports: Migration guide for customer tables and seed data
Description: Documents how to run database migrations and load seed data for local development.
-->

# Database Migrations

This project stores migration assets under `db/migrations` and sample data under `db/scripts`.

## Apply Schema

```bash
psql "$DATABASE_URL" -f db/migrations/01_customer_profile_tables.sql
```

## Seed Test Data

```bash
psql "$DATABASE_URL" -f db/scripts/customer_profile_test_data.sql
```

Both scripts are idempotent and can be re-run safely. The seed script finishes with a smoke-test query printing the number of customers inserted.
