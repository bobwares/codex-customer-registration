# ADR 1: Adopt Spring Boot MVC with JPA and PostgreSQL

**Status**: Accepted

**Date**: $(date -u +%Y-%m-%d)

**Context**
The project requires a REST service for customer registration with relational persistence and API documentation.

**Decision**
Use Spring Boot MVC with Spring Data JPA, PostgreSQL database, Flyway migrations, Springdoc OpenAPI, and Testcontainers for testing.

**Consequences**
- Positive: Rapid development, strong ecosystem, manageable schema migrations.
- Negative: Coupling to the Spring ecosystem and relational database.
