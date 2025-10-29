# Architecture Decision Record

Customer domain modeled with embeddables and collections

**Turn**: 1

**Date**: 2025-10-29 - 05:30

**Context**
The customer JSON schema includes nested objects (address, privacy settings) and collections (emails, phone numbers). The service must expose CRUD APIs with validation while persisting to PostgreSQL using JPA. We need to represent these structures efficiently without creating excessive entity fragmentation, and enforce email uniqueness across all customers.

**Options Considered**
- Map nested structures as separate entities with bidirectional relationships.
- Use JPA embeddables for value objects and `@ElementCollection` for collections.
- Serialize complex fields into JSON columns.

**Decision**
Model address and privacy settings as embeddable value objects and represent emails/phone numbers as element collections backed by dedicated tables. This aligns with the pattern guidance, keeps aggregate boundaries simple, and still normalizes the database schema. Email uniqueness is enforced via repository queries and database constraints.

**Result**
Created `Customer`, `PostalAddress`, `PrivacySettings`, and `PhoneNumber` classes with corresponding Liquibase change sets, repository, service, controller, DTOs, tests, and HTTP flows.

**Consequences**
- Straightforward mapping between API DTOs and domain model with minimal join complexity.
- Element collections load eagerly to avoid detached entity issues in REST responses.
- Requires Liquibase change sets for supporting tables and unique constraints.
- Future extension to support additional nested structures can follow the same pattern.
