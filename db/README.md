# Database Migrations

## Domain Migration

This project ships with normalized DDL for the `CustomerProfile` domain in `db/migrations/01_customer_profile_tables.sql`. The script creates the `customer_profile` schema along with normalized tables, supporting indexes, and a flattened reporting view.

### Prerequisites

- PostgreSQL 16 or newer
- A connection URL with privileges to create schemas, tables, views, and indexes

### Apply the Migration

Run the migration inside a transaction using `psql` or any PostgreSQL client. Replace the connection string with your environment-specific values.

```bash
psql "$DATABASE_URL" -v ON_ERROR_STOP=1 -f db/migrations/01_customer_profile_tables.sql
```

### Smoke Test Checklist

After applying the migration, run the following quick checks:

1. `SELECT schema_name FROM information_schema.schemata WHERE schema_name = 'customer_profile';`
2. `\dt customer_profile.*` to list the newly created tables.
3. `SELECT * FROM customer_profile.customer_profile_flat_view LIMIT 1;` to verify the view compiles.
4. Optionally insert a sample customer, email, and phone record to confirm constraints and cascading behavior.

### Next Steps

Subsequent turns will integrate the migration with Spring Boot’s migration tooling (Liquibase) and wire the persistence layer into the application startup sequence.
