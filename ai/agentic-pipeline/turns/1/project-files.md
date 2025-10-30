File: project_root/README.md
```text
# Customer Registration

Customer Registration securely captures, validates, and manages customer onboarding data using Spring Boot, Spring Data JPA, and PostgreSQL.

## Prerequisites

* Java 21
* Maven 3.9+
* Docker (for optional PostgreSQL container)

## Running Locally

1. Create an `.env` file with the following variables:
   ```env
   APP_NAME=customer-registration
   APP_PORT=8080
   DATABASE_HOST=localhost
   DATABASE_PORT=5432
   DATABASE_NAME=customer_registration
   DATABASE_USERNAME=postgres
   DATABASE_PASSWORD=postgres
   APP_DEFAULT_TAX_RATE=0.07
   APP_DEFAULT_SHIPPING_COST=4.99
   APP_SUPPORTED_CURRENCIES=USD,EUR
   ```
2. Start PostgreSQL via Docker Compose:
   ```bash
   docker compose up -d postgres
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. Access Swagger UI at `http://localhost:8080/swagger-ui.html`.

## Running Tests

Execute the full test suite (includes Testcontainers-backed integration tests):

```bash
mvn clean verify
```

## Docker Compose

The included `docker-compose.yml` starts a PostgreSQL 16 container seeded with the schema defined under `db/init`.

```bash
docker compose up -d postgres
```

Stop and remove the container when finished:

```bash
docker compose down
```

## Additional Documentation

See [README-config.md](README-config.md) for configuration details and Maven wrapper guidance.
```

File: project_root/README-config.md
```text
# Config usage

Validated configuration is provided via `AppProperties` (`@ConfigurationProperties(prefix = "app")`, `@Validated`).
Bind sources: environment variables (APP_NAME, APP_PORT, APP_DEFAULT_TAX_RATE, APP_DEFAULT_SHIPPING_COST, APP_SUPPORTED_CURRENCIES) or `application.yml`.

## Endpoints
- GET /meta/env → returns current app name, port, tax, shipping, and supported currencies.
- OpenAPI UI at /swagger-ui.html; spec at /api-docs.

## Profiles
- Run with `-Dspring-boot.run.profiles=local` or export `SPRING_PROFILES_ACTIVE=local`.
- Use `application-local.yml` locally (copy from `application.yml`).

## Build & Run (no Maven Wrapper in repo)
- Build: `mvn -q -DskipTests=false clean verify`
- Run:   `mvn spring-boot:run -Dspring-boot.run.profiles=local`

## Add Maven Wrapper (optional, run locally; do not commit binaries via ChatGPT Codex)
- Generate wrapper files with your installed Maven:
  - `mvn -N wrapper:wrapper -Dmaven=3.9.9`
- This creates `mvnw`, `mvnw.cmd`, and `.mvn/wrapper/*` on your machine.
- After generating locally, you may commit these files from your workstation if your policy allows committing binaries.

## Validation test
- Set an invalid `APP_PORT` (e.g., `APP_PORT=0`) and run. Startup should fail fast with a constraint violation referencing `AppProperties.port`.

## Actuator HTTP collection

`./e2e/actuator.http` exercises key actuator endpoints for smoke verification.
```

File: project_root/docker-compose.yml
```text
version: "3.9"

services:
  postgres:
    image: postgres:16
    container_name: postgres-shopping
    restart: unless-stopped
    ports:
      - "${DATABASE_PORT}:5432"
    environment:
      POSTGRES_USER: "${DATABASE_USERNAME}"
      POSTGRES_PASSWORD: "${DATABASE_PASSWORD}"
      POSTGRES_DB: "${DATABASE_NAME}"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${DATABASE_USERNAME} -d ${DATABASE_NAME}"]
      interval: 5s
      timeout: 3s
      retries: 20
    volumes:
      - pgdata:/var/lib/postgresql/data
      - ./db/init:/docker-entrypoint-initdb.d:ro
    env_file:
      - .env

volumes:
  pgdata:
```

File: project_root/pom.xml
```text
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.bobwares.customer</groupId>
  <artifactId>registration</artifactId>
  <version>0.1.0-SNAPSHOT</version>
  <name>Customer Registration</name>
  <description>Customer Registration is a service that securely captures, validates, and manages new customer data, exposing standardized APIs for onboarding and downstream integrations.</description>
  <packaging>jar</packaging>

  <properties>
    <java.version>21</java.version>
    <spring-boot.version>3.3.4</spring-boot.version>
    <springdoc.version>2.6.0</springdoc.version>
    <testcontainers.version>1.20.3</testcontainers.version>
    <maven.compiler.release>${java.version}</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
  </properties>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring-boot.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
      <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>testcontainers-bom</artifactId>
        <version>${testcontainers.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <dependencies>
    <!-- Web + Validation -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- Configuration metadata generation -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-configuration-processor</artifactId>
      <optional>true</optional>
    </dependency>
    <dependency>
      <groupId>org.liquibase</groupId>
      <artifactId>liquibase-core</artifactId>
    </dependency>
    <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
    </dependency>

    <!-- Actuator -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>

    <!-- OpenAPI UI -->
    <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
      <version>${springdoc.version}</version>
    </dependency>

    <!-- Lombok (ergonomics) -->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <optional>true</optional>
    </dependency>

    <!-- Test -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>junit-jupiter</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>postgresql</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <!-- Spring Boot -->
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <configuration>
          <mainClass>com.bobwares.customer.registration.Application</mainClass>
        </configuration>
      </plugin>

      <!-- Compiler -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.13.0</version>
        <configuration>
          <release>${maven.compiler.release}</release>
        </configuration>
      </plugin>

      <!-- Spotless -->
      <plugin>
        <groupId>com.diffplug.spotless</groupId>
        <artifactId>spotless-maven-plugin</artifactId>
        <version>2.45.0</version>
        <configuration>
          <java>
            <googleJavaFormat/>
            <removeUnusedImports/>
          </java>
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>apply</goal>
              <goal>check</goal>
            </goals>
          </execution>
        </executions>
      </plugin>

      <!-- Checkstyle -->
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>3.5.0</version>
        <dependencies>
          <dependency>
            <groupId>com.puppycrawl.tools</groupId>
            <artifactId>checkstyle</artifactId>
            <version>10.17.0</version>
          </dependency>
          <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>33.2.1-jre</version>
          </dependency>
        </dependencies>
        <configuration>
          <configLocation>config/checkstyle/checkstyle.xml</configLocation>
          <encoding>${project.build.sourceEncoding}</encoding>
          <consoleOutput>true</consoleOutput>
          <failsOnError>true</failsOnError>
        </configuration>
        <executions>
          <execution>
            <phase>validate</phase>
            <goals>
              <goal>check</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

  <profiles>
    <profile>
      <id>local</id>
      <properties>
        <spring.profiles.active>local</spring.profiles.active>
      </properties>
    </profile>
  </profiles>
</project>
```

File: project_root/src/main/resources/application.yml
```text
# App: Customer Registration
# Package: com.bobwares.customer.registration
# File: application.yml
# Version: 0.1.0
# Turns: Turn 1
# Author: Bobwares
# Date: 2025-10-30T06:53:03Z
# Exports: SpringBootConfiguration
# Description: Configures datasource, JPA, Liquibase, actuator, and application defaults via environment variables.
spring:
  application:
    name: ${APP_NAME}
  datasource:
    url: jdbc:postgresql://${DATABASE_HOST}:${DATABASE_PORT}/${DATABASE_NAME}
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false
    properties:
      hibernate:
        format_sql: true
        jdbc.time_zone: UTC
  liquibase:
    change-log: classpath:db/changelog/db.changelog-master.yml
    enabled: true
  main:
    allow-bean-definition-overriding: false

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html

server:
  port: ${APP_PORT}

management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      probes:
        enabled: true
  observations:
    key-values:
      application: ${APP_NAME}

app:
  name: ${APP_NAME}
  default-tax-rate: ${APP_DEFAULT_TAX_RATE}
  default-shipping-cost: ${APP_DEFAULT_SHIPPING_COST}
  supported-currencies: ${APP_SUPPORTED_CURRENCIES}
```

File: project_root/src/main/java/com/bobwares/customer/registration/service/CustomerService.java
```text
/**
 * App: Customer Registration Package: com.bobwares.customer.registration.service File:
 * CustomerService.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares Date: 2025-10-30T06:53:03Z
 * Exports: CustomerService Description: Implements transactional operations for managing customer
 * aggregates and enforcing uniqueness guarantees.
 */
package com.bobwares.customer.registration.service;

import com.bobwares.customer.registration.api.CustomerDto;
import com.bobwares.customer.registration.model.Customer;
import com.bobwares.customer.registration.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  private final CustomerRepository repository;

  public CustomerService(CustomerRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public CustomerDto.Response create(CustomerDto.CreateRequest request) {
    ensureEmailsAreUnique(request.getEmails(), null);
    Customer customer = CustomerDto.toEntity(request);
    Customer saved = repository.save(customer);
    return CustomerDto.fromEntity(saved);
  }

  @Transactional
  public CustomerDto.Response update(UUID id, CustomerDto.UpdateRequest request) {
    Customer customer = repository.findById(id).orElseThrow(() -> missingCustomer(id));
    ensureEmailsAreUnique(request.getEmails(), id);
    CustomerDto.updateEntity(customer, request);
    return CustomerDto.fromEntity(customer);
  }

  @Transactional
  public void delete(UUID id) {
    if (!repository.existsById(id)) {
      throw missingCustomer(id);
    }
    repository.deleteById(id);
  }

  @Transactional(Transactional.TxType.SUPPORTS)
  public CustomerDto.Response get(UUID id) {
    Customer customer = repository.findById(id).orElseThrow(() -> missingCustomer(id));
    return CustomerDto.fromEntity(customer);
  }

  @Transactional(Transactional.TxType.SUPPORTS)
  public List<CustomerDto.Response> list() {
    return repository.findAll().stream().map(CustomerDto::fromEntity).toList();
  }

  private void ensureEmailsAreUnique(List<String> emails, UUID currentCustomerId) {
    if (emails == null) {
      return;
    }
    emails.stream()
        .distinct()
        .forEach(
            email -> {
              if (repository.existsByEmailsEmailIgnoreCase(email)
                  && (currentCustomerId == null
                      || repository
                          .findByEmailsEmailIgnoreCase(email)
                          .map(Customer::getId)
                          .filter(existingId -> !existingId.equals(currentCustomerId))
                          .isPresent())) {
                throw new IllegalArgumentException("Email already registered: " + email);
              }
            });
  }

  private EntityNotFoundException missingCustomer(UUID id) {
    return new EntityNotFoundException("Customer not found: " + id);
  }
}
```

File: project_root/src/test/java/com/bobwares/customer/registration/service/CustomerServiceTests.java
```text
/**
 * App: Customer Registration Package: com.bobwares.customer.registration.service File:
 * CustomerServiceTests.java Version: 0.1.0 Turns: Turn 1 Author: Bobwares Date:
 * 2025-10-30T06:53:03Z Exports: CustomerServiceTests Description: Exercises customer service CRUD
 * flows and uniqueness validation against PostgreSQL Testcontainers.
 */
package com.bobwares.customer.registration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bobwares.customer.registration.AbstractIntegrationTest;
import com.bobwares.customer.registration.api.CustomerDto;
import com.bobwares.customer.registration.api.CustomerDto.Address;
import com.bobwares.customer.registration.api.CustomerDto.PhoneNumber;
import com.bobwares.customer.registration.api.CustomerDto.Privacy;
import com.bobwares.customer.registration.repository.CustomerRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerServiceTests extends AbstractIntegrationTest {

  @Autowired private CustomerService service;

  @Autowired private CustomerRepository repository;

  @BeforeEach
  void clean() {
    repository.deleteAll();
  }

  @Test
  void createCustomerPersistsAggregate() {
    CustomerDto.CreateRequest request = sampleCreateRequest("test@example.com");

    CustomerDto.Response response = service.create(request);

    assertThat(response.getId()).isNotNull();
    assertThat(response.getEmails()).containsExactly("test@example.com");
    assertThat(repository.count()).isEqualTo(1);
  }

  @Test
  void createCustomerRejectsDuplicateEmail() {
    CustomerDto.CreateRequest request = sampleCreateRequest("dup@example.com");
    service.create(request);

    assertThatThrownBy(() -> service.create(sampleCreateRequest("dup@example.com")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("dup@example.com");
  }

  @Test
  void updateCustomerMutatesExistingRecord() {
    CustomerDto.Response created = service.create(sampleCreateRequest("update@example.com"));

    CustomerDto.UpdateRequest updateRequest = new CustomerDto.UpdateRequest();
    updateRequest.setFirstName("Allison");
    updateRequest.setMiddleName("B.");
    updateRequest.setLastName("Walker");
    updateRequest.setAddress(sampleAddress());
    updateRequest.setPrivacy(samplePrivacy());
    updateRequest.setEmails(List.of("update@example.com"));
    updateRequest.setPhoneNumbers(List.of(samplePhone()));

    CustomerDto.Response updated = service.update(created.getId(), updateRequest);

    assertThat(updated.getFirstName()).isEqualTo("Allison");
    assertThat(updated.getPhoneNumbers()).hasSize(1);
  }

  @Test
  void deleteCustomerRemovesRecord() {
    CustomerDto.Response created = service.create(sampleCreateRequest("delete@example.com"));

    service.delete(created.getId());

    assertThat(repository.existsById(created.getId())).isFalse();
  }

  private CustomerDto.CreateRequest sampleCreateRequest(String email) {
    CustomerDto.CreateRequest request = new CustomerDto.CreateRequest();
    request.setFirstName("Alice");
    request.setMiddleName("Q.");
    request.setLastName("Smith");
    request.setAddress(sampleAddress());
    request.setPrivacy(samplePrivacy());
    request.setEmails(List.of(email));
    request.setPhoneNumbers(List.of(samplePhone()));
    return request;
  }

  private Address sampleAddress() {
    Address address = new Address();
    address.setLine1("123 Main St");
    address.setLine2("Apt 4");
    address.setCity("Springfield");
    address.setState("IL");
    address.setPostalCode("62704");
    address.setCountry("US");
    return address;
  }

  private Privacy samplePrivacy() {
    Privacy privacy = new Privacy();
    privacy.setMarketingEmailsEnabled(true);
    privacy.setTwoFactorEnabled(true);
    return privacy;
  }

  private PhoneNumber samplePhone() {
    PhoneNumber phone = new PhoneNumber();
    phone.setType(PhoneNumber.Type.MOBILE);
    phone.setNumber("+15551234567");
    return phone;
  }
}
```

File: project_root/e2e/Customer.http
```text
### App: Customer Registration
### Package: com.bobwares.customer.registration
### File: Customer.http
### Version: 0.1.0
### Turns: Turn 1
### Author: Bobwares
### Date: 2025-10-30T06:53:03Z
### Exports: CustomerCrudScenario
### Description: Demonstrates full customer CRUD workflow via HTTP requests.
@host = http://localhost:{{port=8080}}
@customerId = {{customerId}}

### Create customer
POST {{host}}/api/customers
Content-Type: application/json

{
  "firstName": "Taylor",
  "middleName": "J.",
  "lastName": "Morgan",
  "address": {
    "line1": "42 Galaxy Way",
    "line2": "",
    "city": "Austin",
    "state": "TX",
    "postalCode": "73301",
    "country": "US"
  },
  "privacy": {
    "marketingEmailsEnabled": true,
    "twoFactorEnabled": true
  },
  "emails": [
    "taylor.morgan@example.com"
  ],
  "phoneNumbers": [
    {
      "type": "MOBILE",
      "number": "+15125550123"
    }
  ]
}
> {% client.global.set("customerId", response.body.id); %}

### Get created customer
GET {{host}}/api/customers/{{customerId}}

### Update customer
PUT {{host}}/api/customers/{{customerId}}
Content-Type: application/json

{
  "firstName": "Taylor",
  "middleName": "James",
  "lastName": "Morgan",
  "address": {
    "line1": "42 Galaxy Way",
    "line2": "Suite 900",
    "city": "Austin",
    "state": "TX",
    "postalCode": "73301",
    "country": "US"
  },
  "privacy": {
    "marketingEmailsEnabled": false,
    "twoFactorEnabled": true
  },
  "emails": [
    "taylor.morgan@example.com",
    "taylor.alt@example.com"
  ],
  "phoneNumbers": [
    {
      "type": "WORK",
      "number": "+15125550666"
    }
  ]
}

### Delete customer
DELETE {{host}}/api/customers/{{customerId}}

### Confirm deletion
GET {{host}}/api/customers/{{customerId}}

```

File: project_root/ai/agentic-pipeline/turns/1/session_context.md
```text
# Session Context

| Variable | Value |
| --- | --- |
| TURN_ID | 1 |
| TURN_START_TIME | 2025-10-30T06:53:03Z |
| TURN_END_TIME | 2025-10-30T07:07:43Z |
| TURN_ELAPSED_TIME | PT14M40S |
| SANDBOX_BASE_DIRECTORY | /workspace |
| AGENTIC_PIPELINE_PROJECT | /workspace/codex-agentic-ai-pipeline |
| SESSION_CONTEXT | /workspace/codex-agentic-ai-pipeline/agentic-pipeline/context/session_context.md |
| TARGET_PROJECT | /workspace/codex-customer-registration-java |
| PROJECT_CONTEXT | /workspace/codex-customer-registration-java/ai/context/project_context.md |
| ACTIVE_PATTERN_NAME | spring-boot-mvc-jpa-postgresql |
| ACTIVE_PATTERN_PATH | /workspace/codex-agentic-ai-pipeline/application-implementation-patterns/spring-boot-mvc-jpa-postgresql |
| EXECUTION_PLAN | /workspace/codex-agentic-ai-pipeline/application-implementation-patterns/spring-boot-mvc-jpa-postgresql/execution-plan.md |
| TEMPLATES | /workspace/codex-agentic-ai-pipeline/agentic-pipeline/templates |
| TEMPLATE_METADATA_HEADER | /workspace/codex-agentic-ai-pipeline/agentic-pipeline/templates/governance/metadata_header.md |
| TEMPLATE_BRANCH_NAMING | /workspace/codex-agentic-ai-pipeline/agentic-pipeline/templates/governance/branch_naming.md |
| TEMPLATE_COMMIT_MESSAGE | /workspace/codex-agentic-ai-pipeline/agentic-pipeline/templates/governance/commit_message.md |
| TEMPLATE_PULL_REQUEST | /workspace/codex-agentic-ai-pipeline/agentic-pipeline/templates/pr/pull_request_template.md |
| TEMPLATE_SESSION_CONTEXT | /workspace/codex-agentic-ai-pipeline/agentic-pipeline/templates/contexts/session_context.md |
| TEMPLATE_ADR | /workspace/codex-agentic-ai-pipeline/agentic-pipeline/templates/adr/adr_template.md |
| TEMPLATE_MANIFEST_SCHEMA | /workspace/codex-agentic-ai-pipeline/agentic-pipeline/templates/turn/manifest.schema.json |
| CODING_AGENTS_DIRECTORY | /workspace/codex-agentic-ai-pipeline/coding-agents |
| CONTAINER_TASKS | /workspace/codex-agentic-ai-pipeline/agentic-pipeline/container/tasks |
| CURRENT_TURN_DIRECTORY | /workspace/codex-customer-registration-java/ai/agentic-pipeline/turns/1 |
```

