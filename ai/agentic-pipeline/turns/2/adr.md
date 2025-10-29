# Architecture Decision Record

Adopt Maven Surefire 3.2.5 for JUnit Platform support

**Turn**: 2

**Date**: 2025-10-29 - 05:48

**Context**
`mvn test` executed zero tests because the project does not inherit the Spring Boot parent POM, leaving Maven Surefire at its legacy 2.x default that only discovers JUnit 4 tests. All existing tests use JUnit Jupiter annotations, so the suite silently skipped execution.

**Options Considered**
- Add the Spring Boot starter parent to inherit modern plugin management.
- Configure Maven Surefire explicitly at version 3.x with JUnit Platform support.
- Replace the JUnit 5 tests with JUnit 4 annotations.

**Decision**
Configure `maven-surefire-plugin` version 3.2.5 directly in the build to enable JUnit Platform execution while keeping the current dependency management structure mandated by the pattern.

**Result**
Updated `pom.xml` to declare Surefire 3.2.5 with module path disabled, allowing the four existing tests to run successfully under `./mvnw test`.

**Consequences**
- The build now executes and fails fast on JUnit 5 regressions during `mvn test`.
- Future test additions can rely on Jupiter APIs without extra configuration.
- Maintaining plugin versions manually is now required because we do not inherit the Spring Boot parent.
