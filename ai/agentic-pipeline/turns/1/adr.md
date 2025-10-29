# Architecture Decision Record

Customer registration service foundation

**Turn**: 1

**Date**: 2025-10-29 - 17:04

**Context**
The execution plan required scaffolding a Spring Boot MVC + JPA + PostgreSQL service aligned with the provided customer JSON schema, delivering database migrations, REST endpoints, and testing infrastructure under strict metadata and governance rules.

**Options Considered**
- Use Liquibase change sets driven by formatted SQL stored alongside application resources.
- Rely solely on JPA auto DDL generation for schema creation.
- Maintain SQL migrations outside of the application and apply them manually.

**Decision**
Adopt Liquibase-formatted SQL change sets mirrored in both `/db/migrations` for manual execution and `src/main/resources/db/changelog/sql` for automated application startup. This balances the pattern’s requirement for normalized SQL artifacts with Spring Boot’s need to validate schema on boot without drifting from prescribed DDL.

**Result**
Created Liquibase master changelog and SQL change set, synchronized manual migration scripts, and wired them into application configuration so both manual and automated paths share the same schema definition.

**Consequences**
- ✅ Ensures consistent schema between manual DBA workflows and automated app startup.
- ✅ Enables Testcontainers-based integration tests to run against the intended schema.
- ⚠️ Requires maintaining dual copies of SQL until automation can source from a single location.
