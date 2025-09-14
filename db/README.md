<!--
App: Customer Registration
Package: db
File: README.md
Version: 0.1.0
Turns: 1
Author: Codex Agent
Date: 2025-09-14T03:38:27Z
Exports: none
Description: Instructions for running database migrations and test data.
-->

# Domain Migration

```bash
psql "$POSTGRES_URL" -f db/migrations/01_customer_tables.sql
psql "$POSTGRES_URL" -f db/scripts/customer_test_data.sql
```

The migration script creates the required tables and indexes. The test data script inserts 20 sample customers.
