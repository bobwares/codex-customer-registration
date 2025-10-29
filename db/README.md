/**
 * App: Customer Registration
 * Package: db
 * File: README.md
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Agent
 * Date: 2025-10-29T22:24:49Z
 * Exports: migration-guide
 * Description: Describes how to run and validate database migrations for the Customer Registration service.
 */
# Database Migrations

## Customer Profile Domain

The `db/migrations/01_customer_profile_tables.sql` migration creates the normalized tables that
back the Customer domain. The script targets PostgreSQL 16 and must be executed inside the
`customer_registration` schema.

### Apply the Migration Locally

1. Start PostgreSQL via `docker compose up postgres`.
2. Apply the migration: `psql "$DATABASE_URL" -f db/migrations/01_customer_profile_tables.sql`.
3. Verify tables with `\dt customer_registration.*`.

### Smoke Test

After applying the migration, insert a sample customer and ensure related collections enforce
uniqueness:

```sql
INSERT INTO customer_registration.privacy_settings (marketing_emails_enabled, two_factor_enabled)
VALUES (true, true)
RETURNING privacy_settings_id;
```

Use the returned key to insert into `customer`, `customer_email`, and `customer_phone_number`.
Attempting to reuse the same email or phone number for the same customer should fail with a
unique constraint violation.
