# Initialize Customer Registration Service

**Status**: Accepted

**Date**: 2025-09-12

**Context**
The project requires a Spring Boot service exposing CRUD operations for customer registrations backed by PostgreSQL.

**Decision**
Bootstrap a Java 21 Spring Boot application using Spring Data JPA and Spring MVC. Use PostgreSQL for persistence with H2 for tests.

**Consequences**
- Positive: Standard stack with rich ecosystem and rapid development.
- Negative: Additional complexity in mapping nested domain structures.
- Follow-ups: Extend validation and error handling as domain grows.
