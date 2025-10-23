# Architecture Decision Record – Turn 1

## Context
The Customer Registration project requires a normalized relational model for customer data
based on `ai/context/schemas/customer.schema.json`. The migration must support historical email
and phone data, enforce validation, and provide a view usable by downstream services.

## Options Considered
| Option | Pros | Cons |
|--------|------|------|
| Single table with JSON columns | Simplifies schema, minimal joins | Violates normalization goals, hard to index emails/phones |
| Fully normalized tables per value object (chosen) | Aligns with schema, enforces constraints, scalable | Requires more joins and migration complexity |

## Decision
**Chosen**: Fully normalized tables per value object. This keeps contact methods, addresses, and
privacy preferences in dedicated tables while exposing an aggregated view for API reads.

## Result
Artifacts created:
- `db/migrations/01_customer_profile_tables.sql`
- `db/README.md`

## Consequences
- Additional migrations are needed to seed reference data.
- Service layer must join or query the provided `customer_overview` view for read models.
