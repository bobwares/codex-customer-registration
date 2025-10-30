# Architecture Decision Record

Customer Data Service Stack Composition

**Turn**: 1

**Date**: 2025-10-30 - 07:07

**Context**
The project required a complete Spring Boot service for customer registration, including persistence, REST endpoints, and supporting infrastructure. The design needed to satisfy the spring-boot-mvc-jpa-postgresql implementation pattern while remaining testable in environments without Docker.

**Options Considered**
1. Generate entities and services using Spring Data JPA with Postgres-specific constructs only.
2. Implement persistence logic manually without Spring Data abstractions.
3. Use Spring Data JPA with a Postgres schema and provide an H2-based fallback for automated tests where Docker is unavailable.

**Decision**
Option 3 was chosen. Entities, repositories, services, and controllers were built with Spring Data JPA aligned to the Postgres schema defined in the execution plan. Integration tests default to Testcontainers when Docker is available but transparently fall back to an H2 in-memory database to keep the suite executable inside the sandbox. This satisfies the pattern’s persistence and testing requirements without blocking local automation.

**Result**
The decision produced a full Maven project (`pom.xml`), domain entities, repositories, services, DTOs, controllers, and tests under `src/main` and `src/test`. Liquibase changelogs and seed SQL were generated, along with Docker Compose and documentation artifacts. Test infrastructure now lives in `AbstractIntegrationTest` to manage the database strategy.

**Consequences**
*Positive*
- Maintains compatibility with the mandated Postgres schema and Liquibase migrations.
- Keeps integration tests runnable in constrained CI sandboxes thanks to the H2 fallback.
- Centralizes persistence rules through Spring Data JPA, reducing boilerplate.

*Negative*
- Additional logic is required in the DTO layer to reconcile child collections safely.
- Differences between Postgres and H2 may mask edge cases that only appear on the real database.
