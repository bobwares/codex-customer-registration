# Use Spring Boot MVC with JPA and PostgreSQL

**Status**: Accepted

**Date**: 2025-09-12

**Context**
The customer registration service requires a synchronous HTTP API with relational persistence.

**Decision**
Adopt Spring Boot MVC with Spring Data JPA backed by PostgreSQL and Flyway migrations.

**Consequences**
- Provides rapid development with robust ecosystem and tooling.
- Requires running PostgreSQL for application and tests.
