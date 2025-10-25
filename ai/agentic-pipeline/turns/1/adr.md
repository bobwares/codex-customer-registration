# ADR: Customer Profile PostgreSQL schema design

- Turn: 1
- Date: 2025-10-25T09:25:26Z
- Context:
    - Need a normalized PostgreSQL schema that reflects the CustomerProfile JSON schema for downstream Spring Boot persistence.
    - Constraints include PostgreSQL 16 compatibility, third normal form, idempotent migrations, and support for array/nested structures.
- Decision:
    - Model the domain with dedicated tables for core entities (customer, postal_address, privacy_settings) and separate tables for collection properties (emails, phone numbers), using indexes and a reporting view for flattened reads.
- Rationale:
    - This structure preserves referential integrity, keeps the migration idempotent, and maps directly to the JSON schema primitives while remaining friendly to JPA entity modeling.
- Consequences:
    - Positive: Provides a clear normalization path, enforces uniqueness and type constraints, and delivers a reusable flattened view for API responses.
    - Negative: Requires join queries for reads and introduces additional Liquibase configuration work in later turns.
- Links:
    - Pattern: spring-boot-mvc-jpa-postgresql (/workspace/codex-agentic-ai-pipeline/application-implementation-patterns/spring-boot-mvc-jpa-postgresql)
    - Artifacts: db/migrations/01_customer_profile_tables.sql, db/README.md
