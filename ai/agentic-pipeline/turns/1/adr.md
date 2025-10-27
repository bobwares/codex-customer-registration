# Architecture Decision Record

Spring Boot MVC + JPA implementation for customer onboarding

**Turn**: 1

**Date**: 2025-10-27 - 23:33

**Context**
The project required a full Spring Boot service following the spring-boot-mvc-jpa-postgresql pattern. We needed to scaffold the application, persist customers per the JSON schema, expose REST endpoints, and provide automated tests that run in the constrained Codex environment.

**Options Considered**
- Generate the application with full Postgres/Testcontainers coverage
- Use an embedded database for automated tests to avoid Docker requirements
- Build a lighter scaffold without Liquibase-managed schema

**Decision**
Implement the complete Spring Boot stack with Liquibase migrations targeting PostgreSQL while configuring automated tests to run on an H2 database in PostgreSQL compatibility mode. This preserves production fidelity for schema and runtime configuration while ensuring that CI-style tests succeed without Docker access.

**Result**
- Created Maven project with Spring Boot 3.3.4 dependencies, configuration properties, and metadata headers
- Added Liquibase changelog and database initialization assets
- Implemented `Customer` aggregate, repositories, service, controller, DTOs, and exception handling aligned to the schema
- Delivered integration (`CustomerControllerIT`) and unit tests (`CustomerServiceTests`, `ApplicationSmokeTest`) using H2 with `ddl-auto=create-drop` to validate the stack without Docker

**Consequences**
- Production deployments still rely on PostgreSQL and Liquibase migrations
- Test suite trades exact PostgreSQL behavior for H2 compatibility, so edge cases specific to PostgreSQL must be covered separately
- The project gains full REST + persistence coverage, developer documentation, Docker Compose tooling, and E2E HTTP scripts in a single turn
