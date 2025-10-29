# Domain Migration

Run the PostgreSQL container via `docker-compose up -d postgres` with the `.env` file providing `DATABASE_USERNAME`, `DATABASE_PASSWORD`, and `DATABASE_NAME`.

Apply migrations manually using `psql`:

```
psql "postgresql://${DATABASE_USERNAME}:${DATABASE_PASSWORD}@localhost:${DATABASE_PORT}/${DATABASE_NAME}" -f db/migrations/01_customer_profile_tables.sql
```

Smoke test the view:

```
psql "postgresql://${DATABASE_USERNAME}:${DATABASE_PASSWORD}@localhost:${DATABASE_PORT}/${DATABASE_NAME}" \
  -c 'SELECT COUNT(*) FROM customer_registration.customer_overview;'
```
