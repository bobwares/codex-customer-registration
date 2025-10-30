# Architecture Decision Record

Graceful handling for Docker-dependent integration test

**Turn**: 2

**Date**: 2025-10-30 - 08:27

**Context**
CustomerControllerIT previously invoked DockerClientFactory during static initialization. In environments without Docker, the call raised an InvalidConfigurationException before the @EnabledIf guard could skip the class, failing the Maven verify phase.

**Options Considered**
- Keep existing logic and rely on @EnabledIf to skip, accepting build failures when Docker is missing.
- Wrap the Docker availability check and container startup in defensive guards that swallow environment-specific failures.
- Replace the Testcontainers-based test with an in-memory database variant.

**Decision**
Implemented defensive guards that wrap Docker availability detection and container startup so the integration test initializes only when Docker is truly usable. This keeps the Testcontainers-based coverage for compliant environments while allowing Docker-less runs to proceed.

**Result**
- Updated CustomerControllerIT to start the PostgreSQL container lazily with error handling.
- Added a resilient dockerAvailable() helper that safely returns false on any detection issues.

**Consequences**
- ✅ Maven verify succeeds in Docker-less CI environments while retaining the Testcontainers scenario for full integration coverage.
- ⚠️ Logs from Testcontainers may still indicate detection attempts when Docker is absent, but they no longer break the build.
- ➕ Future work can add alternative in-memory integration coverage without urgency.
