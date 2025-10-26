<!--
App: Customer Registration
Package: project-root
File: README-config.md
Version: 0.1.0
Turns: 1
Author: Bobwares (bobwares@outlook.com)
Date: 2025-10-26T00:40:26Z
Exports: Configuration usage guide
Description: Documents environment variables, endpoints, and wrapper instructions for the service.
-->

# Configuration and Runtime Notes

Validated configuration is provided through `AppProperties` (`@ConfigurationProperties(prefix = "app")`, `@Validated`).
Bind sources: environment variables (`APP_NAME`, `APP_PORT`) or `application.yml` overrides.

## Endpoints

- `GET /meta/env` → returns current app name and port.
- `GET /api/customers` → lists registered customers.
- `GET /v3/api-docs` → OpenAPI document.
- `GET /swagger-ui.html` → Interactive API explorer.

## Profiles

- Run with `-Dspring-boot.run.profiles=local` or export `SPRING_PROFILES_ACTIVE=local`.
- Copy `src/main/resources/application.yml` to `application-local.yml` for local overrides.

## Build & Run (no Maven Wrapper committed by Codex)

- Build: `mvn -q -DskipTests=false clean verify`
- Run: `mvn spring-boot:run -Dspring-boot.run.profiles=local`

## Add Maven Wrapper (optional)

Generate wrapper files locally (do not commit binary jars through Codex):

```bash
mvn -N wrapper:wrapper -Dmaven=3.9.9
```

This command creates `mvnw`, `mvnw.cmd`, and `.mvn/wrapper/*` on your workstation.
Commit them manually if your policy allows binary artifacts.

## Validation Smoke Test

Set an invalid `APP_PORT` (e.g., `APP_PORT=0`) and start the application. Startup should fail fast with a constraint violation referencing `AppProperties.port`.
