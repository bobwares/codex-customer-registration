/**
 * App: Customer Registration
 * Package: documentation
 * File: README-config.md
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Agent
 * Date: 2025-10-29T22:24:49Z
 * Exports: configuration-guide
 * Description: Documents how to configure and run the Customer Registration service locally.
 */
# Config usage

Validated configuration is provided via `AppProperties` (`@ConfigurationProperties(prefix = "app")`, `@Validated`).
Bind sources: environment variables (APP_NAME, PORT) or `application.yml`.

Endpoints
- GET /meta/env → returns current app name and port.
- OpenAPI UI at /swagger-ui.html; spec at /api-docs.

Profiles
- Run with `-Dspring-boot.run.profiles=local` or export `SPRING_PROFILES_ACTIVE=local`.
- Use `application-local.yml` locally (copy from `application.yml`).

Build & Run (no Maven Wrapper in repo)
- Build: `mvn -q -DskipTests=false clean verify`
- Run:   `mvn spring-boot:run -Dspring-boot.run.profiles=local`

Add Maven Wrapper (optional, run locally; do not commit binaries via ChatGPT Codex)
- Generate wrapper files with your installed Maven:
  - `mvn -N wrapper:wrapper -Dmaven=3.9.9`
- This creates `mvnw`, `mvnw.cmd`, and `.mvn/wrapper/*` on your machine.
- After generating locally, you may commit these files from your workstation if your policy allows committing binaries.

Validation test
- Set an invalid `PORT` (e.g., `PORT=0`) and run. Startup should fail fast with a constraint violation referencing `AppProperties.port`.
