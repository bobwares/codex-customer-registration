# Customer Registration

The Customer Registration service securely captures customer details, validates inputs, persists the
records in PostgreSQL, and exposes RESTful APIs for downstream onboarding flows.

## Prerequisites

- Java 21
- Maven 3.9+
- Docker (for optional Postgres container)

## Configuration

The application reads configuration via environment variables and `application.yml`.

| Variable | Description |
| --- | --- |
| `APP_NAME` | Application display name |
| `APP_PORT` | HTTP port exposed by the service |
| `DATABASE_HOST` | PostgreSQL host |
| `DATABASE_PORT` | PostgreSQL port |
| `DATABASE_NAME` | Database name |
| `DATABASE_USERNAME` | Database user |
| `DATABASE_PASSWORD` | Database password |

Additional application specific defaults (tax rate, shipping cost, currencies) can be overridden via the
`app.*` namespace.

## Build & Test

```bash
mvn -DskipTests=false clean verify
```

### Unit Tests

```bash
mvn -Dtest=CustomerServiceTests test
```

### Integration Tests

```bash
mvn -Dit.test=CustomerControllerIT verify
```

Integration tests use Testcontainers to provision PostgreSQL automatically.

## Run Locally

Start the application with local profile variables:

```bash
export APP_NAME=customer-registration
export APP_PORT=8080
export DATABASE_HOST=localhost
export DATABASE_PORT=5432
export DATABASE_NAME=registration
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=postgres
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### API Endpoints

- `GET /meta/env` — environment metadata
- `GET /api/customers` — list customers
- `POST /api/customers` — create a customer
- `PUT /api/customers/{id}` — update a customer
- `DELETE /api/customers/{id}` — delete a customer
- OpenAPI UI: `/swagger-ui.html`

## Docker Compose

Spin up PostgreSQL with the provided compose file:

```bash
cp .env.example .env  # ensure variables DATABASE_* are defined
docker compose up -d postgres
```

The compose file mounts `db/init` to bootstrap extensions. Point the Spring Boot application at
`DATABASE_HOST=localhost` and `DATABASE_PORT` defined in `.env`.

## Additional Docs

See [`README-config.md`](README-config.md) for configuration validation guidance and Maven Wrapper
instructions.
