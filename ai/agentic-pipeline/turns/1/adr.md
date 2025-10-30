# Architecture Decision Record

Spring Boot Postgres foundation with Liquibase and conditional Testcontainers

**Turn**: 1

**Date**: 2025-10-30 - 01:26

**Context**
The execution plan required a Spring Boot MVC service with Postgres persistence, Liquibase migrations, REST APIs, and automated tests. The repository previously lacked any Spring Boot scaffold, database schema, or build configuration. The container environment does not expose Docker, so Testcontainers-based tests must gracefully skip when the engine is unavailable.

**Options Considered**
- Use an in-memory database (H2) for development and testing.
- Rely solely on Testcontainers-based PostgreSQL for all automated validation.
- Implement Liquibase migrations targeting PostgreSQL while conditionally enabling Testcontainers when Docker is present.

**Decision**
Adopt Liquibase migrations and Spring Data JPA entities for PostgreSQL while configuring Testcontainers-backed tests that automatically skip when Docker is absent. This keeps the code and schema aligned with production requirements and still allows continuous integration environments with Docker to execute full database-backed tests.

**Result**
- Added Liquibase change log files creating the customer, email, and phone tables.
- Implemented domain entity, repository, service, DTOs, controller, and exception handler aligned with the schema.
- Configured Testcontainers support utilities that provide database properties when Docker is available and skip tests otherwise.

**Consequences**
- Developers with Docker receive realistic Postgres-backed tests.
- Environments without Docker complete the Maven build with tests marked as skipped, so manual or alternate verification is required there.
- Liquibase migrations become the single source of truth for schema evolution, requiring future changes to be expressed as migrations.
