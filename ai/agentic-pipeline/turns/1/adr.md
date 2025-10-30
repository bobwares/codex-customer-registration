# Architecture Decision Record

Customer Registration Spring Boot Stack

**Turn**: 1

**Date**: 2025-10-30 - 08:15

**Context**
Implement the first production-ready iteration of the Customer Registration service using the mandated Spring Boot MVC + JPA + PostgreSQL pattern. The solution needed to expose validated REST endpoints, persist customer profiles with nested contact structures, and support deterministic database migrations and automated tests within the provided repository skeleton.

**Options Considered**
- Generate code via the prescribed sql-ddl-generator agent and imported scaffolding.
- Hand-craft the Spring Boot project structure, Liquibase changelog, persistence layer, and REST API aligned to the customer schema.
- Postpone API and persistence implementation pending future turns.

**Decision**
Manually implement the full Spring Boot MVC + JPA stack using the pattern’s specifications. This included creating the Maven project configuration, Liquibase change sets, entity/DTO/service/controller layers, exception handling, and supporting documentation while adding automated unit and integration tests driven by Testcontainers. This approach satisfied all execution plan tasks despite the absence of automated code generation for the schema.

**Result**
- Introduced Liquibase migrations and H2-backed test configuration for portable database setup.
- Added JPA entities with embeddable value objects to model complex customer profiles.
- Delivered REST DTOs, controllers, and exception handling aligned with Springdoc annotations.
- Established unit tests (Mockito) and integration tests (Testcontainers PostgreSQL) plus `.http` E2E scripts.

**Consequences**
- Manual implementation increases initial effort but ensures compliance with metadata headers and project conventions.
- Future changes must keep Liquibase changelog and entity mappings synchronized.
- Testcontainers adds execution time to CI runs but validates database behavior faithfully.
