# Database Migrations

## Domain Migration

This project stores normalized database migrations for the customer registration domain under `db/migrations`.

To apply the initial schema locally using Dockerized PostgreSQL:

1. Start a PostgreSQL container or compose stack that exposes port 5432.
2. Run the migration using `psql` and point it at the running database:
   ```bash
   PGPASSWORD=<password> psql \
     --host=localhost \
     --port=5432 \
     --username=<user> \
     --dbname=<database> \
     --file=db/migrations/01_customer_profile_tables.sql
   ```
3. Verify the schema using `\dn` and `\dt customer_profile.*` within `psql`.
4. Inspect the flattened view with `SELECT * FROM customer_profile.customer_profile_v LIMIT 10;`.

The migration is idempotent thanks to `CREATE TABLE IF NOT EXISTS` statements, so re-running it will leave the schema unchanged.
