# ADR: Establish normalized customer profile migration

- Turn: 1
- Date: 2025-10-27T19:23:31Z
- Context:
    - We needed a repeatable migration to create normalized tables for customer addresses, contacts, and metadata.
    - The project requires PostgreSQL-compatible DDL that can be executed locally via the provided docker-compose stack.
- Decision:
    - Create a SQL migration under `db/migrations/01_customer_profile_tables.sql` along with documentation for running it.
- Rationale:
    - Capturing the schema in version-controlled SQL ensures future turns can extend the database predictably and auditors can review the baseline structure.
- Consequences:
    - Positive: Provides a canonical starting point for future migrations and aligns with PostgreSQL best practices.
    - Negative: Requires contributors to run SQL scripts manually until automated tooling is introduced.
- Links:
    - Pattern: spring-boot-mvc-jpa-postgresql (/workspace/codex-agentic-ai-pipeline/application-implementation-patterns/spring-boot-mvc-jpa-postgresql)
    - Artifacts: db/migrations/01_customer_profile_tables.sql; db/README.md
