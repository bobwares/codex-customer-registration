# Change Log – Turn 1

## High-level outcome
Generated the initial Customer Registration Spring Boot service with database schema assets, Docker tooling, and CRUD APIs aligned to the customer domain.

## Pattern
spring-boot-mvc-jpa-postgresql (/workspace/codex-agentic-ai-pipeline/application-implementation-patterns/spring-boot-mvc-jpa-postgresql)

## Objective (prompt)
execute turn 1

## Files Added (exclude /ai)
| Path / File | Task Name |
| ----------- | --------- |
| .env.example | TASK — Create Containerized SQL Dialect Environments |
| .env.postgresql | TASK — Create Containerized SQL Dialect Environments |
| .gitignore | TASK 01 – Initialize Project |
| Makefile | TASK - Add Makefile |
| README-config.md | TASK 01 – Initialize Project |
| db/README.md | TASK - Generate Normalized Tables |
| db/migrations/01_customer_profile_tables.sql | TASK - Generate Normalized Tables |
| db/scripts/customer_profile_test_data.sql | TASK - Generate Test Data |
| docker-compose.yml | TASK — Create Containerized SQL Dialect Environments |
| docker/postgresql/Dockerfile | TASK — Create Containerized SQL Dialect Environments |
| docker/postgresql/init-db.sh | TASK — Create Containerized SQL Dialect Environments |
| e2e/actuator.http | TASK 01 – Initialize Project |
| pom.xml | TASK 01 – Initialize Project |
| src/main/java/com/bobwares/customer/registration/Application.java | TASK 01 – Initialize Project |
| src/main/java/com/bobwares/customer/registration/CustomerRepository.java | TASK - Create Persistence Layer |
| src/main/java/com/bobwares/customer/registration/CustomerService.java | TASK - Create Persistence Layer |
| src/main/java/com/bobwares/customer/registration/api/CustomerController.java | TASK - Create REST Service |
| src/main/java/com/bobwares/customer/registration/api/CustomerDto.java | TASK - Create REST Service |
| src/main/java/com/bobwares/customer/registration/config/AppProperties.java | TASK 01 – Initialize Project |
| src/main/java/com/bobwares/customer/registration/model/Customer.java | TASK - Create Persistence Layer |
| src/main/java/com/bobwares/customer/registration/model/CustomerEmail.java | TASK - Create Persistence Layer |
| src/main/java/com/bobwares/customer/registration/model/CustomerPhoneNumber.java | TASK - Create Persistence Layer |
| src/main/java/com/bobwares/customer/registration/model/PhoneType.java | TASK - Create Persistence Layer |
| src/main/java/com/bobwares/customer/registration/model/PostalAddress.java | TASK - Create Persistence Layer |
| src/main/java/com/bobwares/customer/registration/model/PrivacySettings.java | TASK - Create Persistence Layer |
| src/main/java/com/bobwares/customer/registration/web/MetaController.java | TASK 01 – Initialize Project |
| src/main/java/com/bobwares/customer/registration/web/RestExceptionHandler.java | TASK - Create REST Service |
| src/main/resources/application.yml | TASK 01 – Initialize Project |
| src/test/java/com/bobwares/customer/registration/ApplicationSmokeTest.java | TASK 01 – Initialize Project |
| src/test/java/com/bobwares/customer/registration/api/CustomerControllerIT.java | TASK - Create REST Service |
| src/test/java/com/bobwares/customer/registration/service/CustomerServiceTests.java | TASK - Create Persistence Layer |

## Files Updated (exclude /ai)
| Path / File | Task Name |
| ----------- | --------- |
| README.md | TASK — Create Containerized SQL Dialect Environments |
| docker-compose.yml | TASK - Create Docker Compose for PostgreSQL |
| pom.xml | TASK - Create REST Service |

## Turn Files Added (under /ai only)
| Path / File |
| ----------- |
| ai/agentic-pipeline/turns/1/changelog.md |
| ai/agentic-pipeline/turns/1/adr.md |
| ai/agentic-pipeline/turns/1/manifest.json |
