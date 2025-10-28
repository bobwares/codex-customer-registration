# Architecture Decision Record

Establish Customer Registration Spring Boot stack for PostgreSQL persistence

**Turn**: 1

**Date**: 2025-10-28 - 15:32

**Context**
The project requires a production-ready Spring Boot REST API that persists customer onboarding data aligned with the provided JSON schema while supporting PostgreSQL, Liquibase migrations, and containerized development.

**Options Considered**
- Use Spring Boot with Spring Data JPA entities mirroring the schema and manage DDL with Liquibase.
- Implement a lighter-weight framework (e.g., Micronaut) with manual JDBC persistence and SQL scripts.
- Postpone persistence and expose in-memory storage with stub endpoints.

**Decision**
Adopt Spring Boot 3.5.5 with Spring Data JPA entities that reflect the normalized relational model, backed by Liquibase change logs. This aligns with the spring-boot-mvc-jpa-postgresql pattern guidance, enabling Hibernate validation and Liquibase-driven schema management while keeping the implementation maintainable.

**Result**
Generated Maven configuration, application bootstrap code, Liquibase changelog files, JPA entities, repositories, service and controller layers, Docker assets, and Testcontainers-backed integration tests.

**Consequences**
- Positive: Automatic schema validation, structured migrations, and familiar Spring ecosystem patterns speed future enhancements.
- Positive: Integration tests using Testcontainers validate the stack against real PostgreSQL behavior.
- Negative: Spring Boot with Liquibase and Testcontainers increases build time and resource usage versus an in-memory stub, but the trade-off delivers production fidelity.
