# Architecture Decision Record

Customer persistence and REST layering for Customer Registration

**Turn**: 1

**Date**: 2025-10-29 - 22:24

**Context**
The execution plan required implementing a full Spring Boot MVC + JPA stack using the customer JSON schema.
We needed to choose how to model nested objects and arrays (address, emails, phone numbers, privacy settings)
while satisfying metadata header governance, Liquibase integration, and uniqueness constraints.

**Options Considered**
1. Model collections as JSON columns within the `customer` table.
2. Use `@ElementCollection` with embeddables for emails and phone numbers.
3. Normalize each aggregate component into dedicated entities and tables with explicit repositories.

**Decision**
We normalized the domain (option 3), mapping each collection/object to dedicated JPA entities backed by
PostgreSQL tables. This aligns with the spring-boot-mvc-jpa-postgresql pattern emphasis on relational
fidelity and Liquibase-driven schema management. Repositories and services enforce uniqueness via Spring Data
query derivation, enabling consistent validation paths for the REST layer.

**Result**
Generated entities (`Customer`, `CustomerEmail`, `CustomerPhoneNumber`, `CustomerAddress`, `PrivacySettings`),
service and controller layers, Liquibase changelog plus SQL migration mirrors, and supporting tests/HTTP specs.

**Consequences**
- Positive: Clear relational schema, easy enforcement of unique emails/phone numbers, compatibility with
  Liquibase/Testcontainers.
- Positive: Service encapsulates mapping logic, simplifying controller handlers.
- Negative: Additional entities increase code volume and object mapping overhead.
- Negative: Requires cascades/orphan removal considerations to avoid orphaned rows during updates.
