# Database Artifacts

## Domain Migration

The normalized PostgreSQL objects for the Customer Registration service are defined in
`db/migrations/01_customer_profile_tables.sql`. Apply the migration against a PostgreSQL 16+
instance using the commands below.

```bash
psql "$DATABASE_URL" -f db/migrations/01_customer_profile_tables.sql
```

The migration is idempotent and can be rerun safely. After applying the migration, smoke-test
that the schema is present and basic joins work:

```bash
psql "$DATABASE_URL" <<'SQL'
SET search_path TO customer_profile, public;
\dt
SELECT COUNT(*) FROM customer;
SELECT * FROM customer_overview LIMIT 5;
SQL
```

The generated view `customer_overview` flattens addresses, emails, and privacy preferences for
API consumption.
