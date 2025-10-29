# codex-customer-registration

codex-customer-registration

## Docker Compose

1. Create an `.env` file with values for `DATABASE_USERNAME`, `DATABASE_PASSWORD`, `DATABASE_NAME`, and `DATABASE_PORT`.
2. Start PostgreSQL with `docker-compose up -d postgres`.
3. Apply migrations from `db/migrations` as needed, e.g. `psql ... -f db/migrations/01_customer_profile_tables.sql`.
4. Stop the container with `docker-compose down` when finished.
