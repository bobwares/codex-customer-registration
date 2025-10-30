# Architecture Decision Record

ADR 001 – Customer Registration Persistence and API Stack

**Turn**: 1

**Date**: 2025-10-30 - 02:16

**Context**
The customer registration service must capture profile, contact, and privacy preferences while exposing a predictable CRUD API and deterministic database migrations. Governance requires mirroring DDL under `db/migrations` for operators and Liquibase SQL change sets for runtime bootstrapping.

**Options Considered**
- Rely on Hibernate auto-DDL for schema evolution without source-controlled SQL artifacts.
- Maintain a single denormalized `customer` table with JSON blobs for contacts and preferences.
- Author normalized SQL migrations plus Liquibase change sets and map them to a rich JPA aggregate exposed by Spring MVC.

**Decision**
Adopt normalized PostgreSQL tables for customers, addresses, emails, phone numbers, and privacy preferences, materialized through handwritten SQL migration scripts and mirrored Liquibase change sets. Pair the schema with an explicit JPA aggregate (`Customer` root with value objects) and Spring MVC controller/service/DTO layers to enforce validation, deterministic ordering, and OpenAPI documentation.

**Result**
- Authored DDL in `db/migrations/01_customer_profile_tables.sql` and a matching Liquibase master/change set under `src/main/resources/db/changelog`.
- Implemented the JPA aggregate (`Customer`, `CustomerEmail`, `CustomerPhoneNumber`, `PostalAddress`, `PrivacySettings`) with validation and repository/service logic.
- Exposed CRUD endpoints with `CustomerController` and DTO mapper to isolate transport contracts.

**Consequences**
- ✅ Database state is reproducible across environments because both operational SQL and Liquibase share the same source.
- ✅ Domain logic remains testable thanks to explicit service validation and DTO mapping.
- ⚠️ Requires engineers to update SQL and Liquibase in tandem for future schema changes.
- ⚠️ The normalized structure introduces join overhead for read-heavy reporting use cases; caching or projections may be needed later.
