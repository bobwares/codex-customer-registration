# Change Log – Turn 1

## High-level outcome
Generated the Spring Boot customer registration service with REST, persistence, configuration, tests, and supporting developer tooling.

## Pattern
application-implementation-patterns/spring-boot-mvc-jpa-postgresql (/workspace/codex-agentic-ai-pipeline/application-implementation-patterns/spring-boot-mvc-jpa-postgresql)

## Objective (prompt)
execute turn 1

## Files Added (exclude /ai)
| Path / File | Task Name |
| ----------- | --------- |
| .env.example | TASK 01 – Initialize Project |
| .gitignore | TASK 01 – Initialize Project |
| README-config.md | TASK 01 – Initialize Project |
| db/init/001_extensions.sql | TASK - Create_Docker_Compose_for_PostgreSQL |
| e2e/Customer.http | TASK – Generate CRUD with OpenAPI, Unit Tests, and E2E HTTP |
| e2e/actuator.http | TASK 01 – Initialize Project |
| pom.xml | TASK 01 – Initialize Project |
| src/main/java/com/bobwares/customer/registration/Application.java | TASK 01 – Initialize Project |
| src/main/java/com/bobwares/customer/registration/Customer.java | TASK - Create Persistence Layer |
| src/main/java/com/bobwares/customer/registration/CustomerRepository.java | TASK - Create Persistence Layer |
| src/main/java/com/bobwares/customer/registration/CustomerService.java | TASK - Create Persistence Layer |
| src/main/java/com/bobwares/customer/registration/PhoneNumber.java | TASK - Create Persistence Layer |
| src/main/java/com/bobwares/customer/registration/PhoneType.java | TASK - Create Persistence Layer |
| src/main/java/com/bobwares/customer/registration/PostalAddress.java | TASK - Create Persistence Layer |
| src/main/java/com/bobwares/customer/registration/PrivacySettings.java | TASK - Create Persistence Layer |
| src/main/java/com/bobwares/customer/registration/api/CustomerController.java | TASK – Generate CRUD with OpenAPI, Unit Tests, and E2E HTTP |
| src/main/java/com/bobwares/customer/registration/api/CustomerDto.java | TASK – Generate CRUD with OpenAPI, Unit Tests, and E2E HTTP |
| src/main/java/com/bobwares/customer/registration/api/CustomerMapper.java | TASK – Generate CRUD with OpenAPI, Unit Tests, and E2E HTTP |
| src/main/java/com/bobwares/customer/registration/config/AppProperties.java | TASK 01 – Initialize Project |
| src/main/java/com/bobwares/customer/registration/web/MetaController.java | TASK 01 – Initialize Project |
| src/main/java/com/bobwares/customer/registration/web/RestExceptionHandler.java | TASK – Generate CRUD with OpenAPI, Unit Tests, and E2E HTTP |
| src/main/resources/application.yml | TASK 01 – Initialize Project |
| src/main/resources/db/changelog/db.changelog-master.yml | TASK - Create Persistence Layer |
| src/test/java/com/bobwares/customer/registration/ApplicationSmokeTest.java | TASK 01 – Initialize Project |
| src/test/java/com/bobwares/customer/registration/CustomerControllerIT.java | TASK – Generate CRUD with OpenAPI, Unit Tests, and E2E HTTP |
| src/test/java/com/bobwares/customer/registration/CustomerServiceTests.java | TASK – Generate CRUD with OpenAPI, Unit Tests, and E2E HTTP |
| src/test/resources/application-test.yml | TASK – Generate CRUD with OpenAPI, Unit Tests, and E2E HTTP |

## Files Updated (exclude /ai)
| Path / File | Task Name |
| ----------- | --------- |
| README.md | TASK 01 – Initialize Project |
| docker-compose.yml | TASK - Create_Docker_Compose_for_PostgreSQL |

## Turn Files Added (under /ai only)
| Path / File |
| ----------- |
| ai/agentic-pipeline/turns/1/session_context.md |
| ai/agentic-pipeline/turns/1/changelog.md |
