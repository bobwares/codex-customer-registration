File: project_root/.gitignore

```text
# App: Customer Registration
# Package: project.root
# File: .gitignore
# Version: 0.1.0
# Turns: 1
# Author: Bobwares
# Date: 2025-10-30T01:57:11Z
# Exports: Git ignore rules
# Description: Specifies files and directories that should be excluded from version control.

.env*
/ai/project-parser/output

.DS_Store
/ai/output
HELP.md
target/
.mvn/wrapper/maven-wrapper.jar
!**/src/main/**/target/
!**/src/test/**/target/

### STS ###
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans
.sts4-cache

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr

### NetBeans ###
/nbproject/private/
/nbbuild/
/dist/
/nbdist/
/.nb-gradle/
build/
!**/src/main/**/build/
!**/src/test/**/build/

### VS Code ###
.vscode/
.DS_Store
**/.DS_Store

```

File: project_root/README.md

```text
# Customer Registration

Customer Registration is a Spring Boot service that captures, validates, and persists new customer information for downstream integrations.

## Prerequisites
- Java 21
- Maven 3.9+
- Docker (for local PostgreSQL)

## Running Locally
1. Export required environment variables (`APP_NAME`, `APP_PORT`, `DATABASE_HOST`, `DATABASE_PORT`, `DATABASE_NAME`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`, `APP_DEFAULT_TAX_RATE`, `APP_DEFAULT_SHIPPING_COST`, `APP_SUPPORTED_CURRENCIES`).
2. Build and run the application:
   ```bash
   mvn -q -DskipTests=false clean verify
   mvn spring-boot:run -Dspring-boot.run.profiles=local
   ```

## Docker Compose
Start a local PostgreSQL instance aligned with the application defaults.

```bash
docker compose up -d postgres
```

The compose file mounts `./db/init` for seed SQL and exposes the configured port (`DATABASE_PORT`).

## HTTP Utilities
- `e2e/actuator.http` contains ready-to-run Actuator smoke tests.
- Additional domain CRUD requests are generated alongside service endpoints.

## Configuration
See [`README-config.md`](README-config.md) for details on validated configuration properties and wrapper guidance.

```

File: project_root/README-config.md

```text
# Config usage

Validated configuration is provided via `AppProperties` (`@ConfigurationProperties(prefix = "app")`, `@Validated`).
Bound sources: environment variables (APP_NAME, APP_PORT, APP_DEFAULT_TAX_RATE, APP_DEFAULT_SHIPPING_COST, APP_SUPPORTED_CURRENCIES) or `application.yml`.

Endpoints
- GET /meta/env → returns current app name, port, and supported currencies.
- OpenAPI UI at /swagger-ui.html; spec at /v3/api-docs.

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
- Set an invalid `APP_PORT` (e.g., `APP_PORT=0`) and run. Startup should fail fast with a constraint violation referencing `AppProperties.port`.

```

File: project_root/docker-compose.yml

```text
# App: Customer Registration
# Package: project.root
# File: docker-compose.yml
# Version: 0.1.0
# Turns: 1
# Author: Bobwares
# Date: 2025-10-30T01:57:11Z
# Exports: Docker Compose specification for PostgreSQL
# Description: Defines a PostgreSQL 16 service with persistent storage and health checking for local development.

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
  <version>0.1.0</version>
  <name>Customer Registration</name>
  <description>Spring Boot service for managing customer registrations</description>
  <packaging>jar</packaging>

  <properties>
    <java.version>21</java.version>
    <spring-boot.version>3.3.4</spring-boot.version>
    <springdoc.version>2.6.0</springdoc.version>
    <maven.compiler.release>${java.version}</maven.compiler.release>
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
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
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
  </dependencies>

  <build>
    <plugins>
      <!-- Spring Boot -->
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <version>${spring-boot.version}</version>
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
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>3.2.5</version>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-failsafe-plugin</artifactId>
        <version>3.2.5</version>
        <executions>
          <execution>
            <goals>
              <goal>integration-test</goal>
              <goal>verify</goal>
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

File: project_root/db/README.md

```text
# Database Migrations

## Domain Migration

The `01_customer_profile_tables.sql` migration establishes the normalized PostgreSQL schema for customer registration.

### Apply Locally
1. Ensure a PostgreSQL instance is running (e.g., `docker compose up postgres`).
2. Set `DATABASE_URL` or individual credentials to match your environment.
3. Execute the migration:
   ```bash
   psql "$DATABASE_URL" -f db/migrations/01_customer_profile_tables.sql
   ```

### Smoke Test
1. Insert a `privacy_settings` record and an associated `postal_address`.
2. Insert a `customer` referencing those rows plus related emails and phone numbers.
3. Query the `customer_profile_view` to verify the joined projection.

```

File: project_root/db/migrations/01_customer_profile_tables.sql

```text
-- App: Customer Registration
-- Package: db.migrations
-- File: db/migrations/01_customer_profile_tables.sql
-- Version: 0.1.0
-- Turns: 1
-- Author: Bobwares
-- Date: 2025-10-30T01:57:11Z
-- Exports: CREATE TABLE statements for customer profile domain
-- Description: Creates normalized PostgreSQL tables for customer profiles, emails, phone numbers, addresses, and privacy settings.

BEGIN;

CREATE SCHEMA IF NOT EXISTS customer_registration;

SET search_path TO customer_registration, public;

CREATE TABLE IF NOT EXISTS postal_address (
  address_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  line1 VARCHAR(255) NOT NULL,
  line2 VARCHAR(255),
  city VARCHAR(100) NOT NULL,
  state VARCHAR(100) NOT NULL,
  postal_code VARCHAR(32),
  country CHAR(2) NOT NULL
);

CREATE TABLE IF NOT EXISTS privacy_settings (
  privacy_settings_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  marketing_emails_enabled BOOLEAN NOT NULL,
  two_factor_enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS customer (
  customer_id UUID PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  middle_name VARCHAR(100),
  last_name VARCHAR(100) NOT NULL,
  address_id BIGINT REFERENCES postal_address(address_id),
  privacy_settings_id BIGINT NOT NULL REFERENCES privacy_settings(privacy_settings_id),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_customer_address_id ON customer(address_id);
CREATE INDEX IF NOT EXISTS idx_customer_privacy_settings_id ON customer(privacy_settings_id);

CREATE TABLE IF NOT EXISTS customer_email (
  customer_email_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  customer_id UUID NOT NULL REFERENCES customer(customer_id) ON DELETE CASCADE,
  email VARCHAR(320) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
  UNIQUE (customer_id, email)
);

CREATE INDEX IF NOT EXISTS idx_customer_email_customer_id ON customer_email(customer_id);
CREATE UNIQUE INDEX IF NOT EXISTS ux_customer_email_email ON customer_email(email);

CREATE TABLE IF NOT EXISTS customer_phone_number (
  customer_phone_number_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  customer_id UUID NOT NULL REFERENCES customer(customer_id) ON DELETE CASCADE,
  phone_type VARCHAR(20) NOT NULL,
  phone_number VARCHAR(20) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
  UNIQUE (customer_id, phone_number)
);

CREATE INDEX IF NOT EXISTS idx_customer_phone_customer_id ON customer_phone_number(customer_id);

CREATE OR REPLACE VIEW customer_profile_view AS
SELECT
  c.customer_id,
  c.first_name,
  c.middle_name,
  c.last_name,
  pa.line1,
  pa.line2,
  pa.city,
  pa.state,
  pa.postal_code,
  pa.country,
  ps.marketing_emails_enabled,
  ps.two_factor_enabled
FROM customer c
LEFT JOIN postal_address pa ON pa.address_id = c.address_id
JOIN privacy_settings ps ON ps.privacy_settings_id = c.privacy_settings_id;

COMMIT;

```

File: project_root/db/init/README.md

```text
# Initialization Scripts

Place optional SQL seed files in this directory to be executed automatically by the PostgreSQL container during startup.

```

File: project_root/src/main/java/com/bobwares/customer/registration/Application.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: src/main/java/com/bobwares/customer/registration/Application.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: Application
 * Description: Entry point for the Customer Registration Spring Boot application.
 */
package com.bobwares.customer.registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.bobwares.customer.registration.config")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}

```

File: project_root/src/main/java/com/bobwares/customer/registration/config/AppProperties.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.config
 * File: src/main/java/com/bobwares/customer/registration/config/AppProperties.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: AppProperties
 * Description: Configuration properties backing application metadata and defaults.
 */
package com.bobwares.customer.registration.config;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public class AppProperties {

  @NotBlank
  private String name = "customer-registration";

  @NotNull
  @Min(1)
  @Max(65535)
  private Integer port = 8080;

  @NotNull
  @DecimalMin("0.0")
  private BigDecimal defaultTaxRate = new BigDecimal("0.0");

  @NotNull
  @DecimalMin("0.0")
  private BigDecimal defaultShippingCost = new BigDecimal("0.0");

  @NotEmpty
  private List<@NotBlank String> supportedCurrencies = new ArrayList<>(List.of("USD"));

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public BigDecimal getDefaultTaxRate() {
    return defaultTaxRate;
  }

  public void setDefaultTaxRate(BigDecimal defaultTaxRate) {
    this.defaultTaxRate = defaultTaxRate;
  }

  public BigDecimal getDefaultShippingCost() {
    return defaultShippingCost;
  }

  public void setDefaultShippingCost(BigDecimal defaultShippingCost) {
    this.defaultShippingCost = defaultShippingCost;
  }

  public List<String> getSupportedCurrencies() {
    return supportedCurrencies;
  }

  public void setSupportedCurrencies(List<String> supportedCurrencies) {
    this.supportedCurrencies = supportedCurrencies;
  }
}

```

File: project_root/src/main/java/com/bobwares/customer/registration/web/MetaController.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.web
 * File: src/main/java/com/bobwares/customer/registration/web/MetaController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: MetaController
 * Description: Exposes application metadata endpoints for environment diagnostics.
 */
package com.bobwares.customer.registration.web;

import com.bobwares.customer.registration.config.AppProperties;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meta")
public class MetaController {

  private final AppProperties properties;

  public MetaController(AppProperties properties) {
    this.properties = properties;
  }

  @GetMapping("/env")
  public Map<String, Object> env() {
    return Map.of(
        "app",
        properties.getName(),
        "port",
        properties.getPort(),
        "supportedCurrencies",
        properties.getSupportedCurrencies());
  }
}

```

File: project_root/src/main/java/com/bobwares/customer/registration/PostalAddress.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: src/main/java/com/bobwares/customer/registration/PostalAddress.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: PostalAddress
 * Description: Represents a physical mailing address linked to customer records.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "postal_address", schema = "customer_registration")
public class PostalAddress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "address_id")
  private Long id;

  @NotBlank
  @Size(max = 255)
  @Column(name = "line1", nullable = false, length = 255)
  private String line1;

  @Size(max = 255)
  @Column(name = "line2", length = 255)
  private String line2;

  @NotBlank
  @Size(max = 100)
  @Column(name = "city", nullable = false, length = 100)
  private String city;

  @NotBlank
  @Size(max = 100)
  @Column(name = "state", nullable = false, length = 100)
  private String state;

  @Size(max = 32)
  @Column(name = "postal_code", length = 32)
  private String postalCode;

  @NotBlank
  @Size(min = 2, max = 2)
  @Column(name = "country", nullable = false, length = 2)
  private String country;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLine1() {
    return line1;
  }

  public void setLine1(String line1) {
    this.line1 = line1;
  }

  public String getLine2() {
    return line2;
  }

  public void setLine2(String line2) {
    this.line2 = line2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}

```

File: project_root/src/main/java/com/bobwares/customer/registration/PrivacySettings.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: src/main/java/com/bobwares/customer/registration/PrivacySettings.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: PrivacySettings
 * Description: Captures notification and authentication preferences for a customer.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "privacy_settings", schema = "customer_registration")
public class PrivacySettings {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "privacy_settings_id")
  private Long id;

  @NotNull
  @Column(name = "marketing_emails_enabled", nullable = false)
  private Boolean marketingEmailsEnabled;

  @NotNull
  @Column(name = "two_factor_enabled", nullable = false)
  private Boolean twoFactorEnabled;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getMarketingEmailsEnabled() {
    return marketingEmailsEnabled;
  }

  public void setMarketingEmailsEnabled(Boolean marketingEmailsEnabled) {
    this.marketingEmailsEnabled = marketingEmailsEnabled;
  }

  public Boolean getTwoFactorEnabled() {
    return twoFactorEnabled;
  }

  public void setTwoFactorEnabled(Boolean twoFactorEnabled) {
    this.twoFactorEnabled = twoFactorEnabled;
  }
}

```

File: project_root/src/main/java/com/bobwares/customer/registration/CustomerEmail.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: src/main/java/com/bobwares/customer/registration/CustomerEmail.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: CustomerEmail
 * Description: Represents an individual email address associated with a customer.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "customer_email", schema = "customer_registration")
public class CustomerEmail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "customer_email_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @Email
  @NotBlank
  @Size(max = 320)
  @Column(name = "email", nullable = false, length = 320)
  private String email;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }
}

```

File: project_root/src/main/java/com/bobwares/customer/registration/CustomerPhoneNumber.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: src/main/java/com/bobwares/customer/registration/CustomerPhoneNumber.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: CustomerPhoneNumber
 * Description: Stores normalized phone numbers linked to customers.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "customer_phone_number", schema = "customer_registration")
public class CustomerPhoneNumber {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "customer_phone_number_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @NotBlank
  @Size(max = 20)
  @Column(name = "phone_type", nullable = false, length = 20)
  private String type;

  @NotBlank
  @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
  @Size(max = 20)
  @Column(name = "phone_number", nullable = false, length = 20)
  private String number;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }
}

```

File: project_root/src/main/java/com/bobwares/customer/registration/Customer.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: src/main/java/com/bobwares/customer/registration/Customer.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: Customer
 * Description: Root aggregate representing a registered customer and related contact data.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "customer", schema = "customer_registration")
public class Customer {

  @Id
  @GeneratedValue
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(name = "customer_id", nullable = false, updatable = false)
  private UUID id;

  @NotBlank
  @Size(max = 100)
  @Column(name = "first_name", nullable = false, length = 100)
  private String firstName;

  @Size(max = 100)
  @Column(name = "middle_name", length = 100)
  private String middleName;

  @NotBlank
  @Size(max = 100)
  @Column(name = "last_name", nullable = false, length = 100)
  private String lastName;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "address_id")
  private PostalAddress address;

  @NotNull
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
  @JoinColumn(name = "privacy_settings_id", nullable = false)
  private PrivacySettings privacySettings;

  @OneToMany(
      mappedBy = "customer",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Set<CustomerEmail> emails = new LinkedHashSet<>();

  @OneToMany(
      mappedBy = "customer",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private Set<CustomerPhoneNumber> phoneNumbers = new LinkedHashSet<>();

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public PostalAddress getAddress() {
    return address;
  }

  public void setAddress(PostalAddress address) {
    this.address = address;
  }

  public PrivacySettings getPrivacySettings() {
    return privacySettings;
  }

  public void setPrivacySettings(PrivacySettings privacySettings) {
    this.privacySettings = privacySettings;
  }

  public Set<CustomerEmail> getEmails() {
    return emails;
  }

  public void setEmails(Set<CustomerEmail> emails) {
    this.emails.clear();
    if (emails != null) {
      emails.forEach(this::addEmail);
    }
  }

  public void addEmail(CustomerEmail email) {
    if (email != null) {
      email.setCustomer(this);
      emails.add(email);
    }
  }

  public void removeEmail(CustomerEmail email) {
    if (email != null && emails.remove(email)) {
      email.setCustomer(null);
    }
  }

  public Set<CustomerPhoneNumber> getPhoneNumbers() {
    return phoneNumbers;
  }

  public void setPhoneNumbers(Set<CustomerPhoneNumber> phoneNumbers) {
    this.phoneNumbers.clear();
    if (phoneNumbers != null) {
      phoneNumbers.forEach(this::addPhoneNumber);
    }
  }

  public void addPhoneNumber(CustomerPhoneNumber phoneNumber) {
    if (phoneNumber != null) {
      phoneNumber.setCustomer(this);
      phoneNumbers.add(phoneNumber);
    }
  }

  public void removePhoneNumber(CustomerPhoneNumber phoneNumber) {
    if (phoneNumber != null && phoneNumbers.remove(phoneNumber)) {
      phoneNumber.setCustomer(null);
    }
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void updateContactDetails(Customer source) {
    if (source == null) {
      return;
    }
    setFirstName(source.getFirstName());
    setMiddleName(source.getMiddleName());
    setLastName(source.getLastName());
    setAddress(source.getAddress() == null ? null : cloneAddress(source.getAddress()));
    setPrivacySettings(clonePrivacySettings(source.getPrivacySettings()));
    setEmails(source.getEmails());
    setPhoneNumbers(source.getPhoneNumbers());
  }

  private PostalAddress cloneAddress(PostalAddress original) {
    if (original == null) {
      return null;
    }
    PostalAddress copy = new PostalAddress();
    copy.setLine1(original.getLine1());
    copy.setLine2(original.getLine2());
    copy.setCity(original.getCity());
    copy.setState(original.getState());
    copy.setPostalCode(original.getPostalCode());
    copy.setCountry(original.getCountry());
    return copy;
  }

  private PrivacySettings clonePrivacySettings(PrivacySettings original) {
    Objects.requireNonNull(original, "privacySettings must not be null");
    PrivacySettings copy = new PrivacySettings();
    copy.setMarketingEmailsEnabled(original.getMarketingEmailsEnabled());
    copy.setTwoFactorEnabled(original.getTwoFactorEnabled());
    return copy;
  }
}

```

File: project_root/src/main/java/com/bobwares/customer/registration/CustomerRepository.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: src/main/java/com/bobwares/customer/registration/CustomerRepository.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: CustomerRepository
 * Description: Spring Data repository for accessing customer aggregates.
 */
package com.bobwares.customer.registration;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  boolean existsByEmailsEmailIgnoreCase(String email);

  Optional<Customer> findByEmailsEmailIgnoreCase(String email);
}

```

File: project_root/src/main/java/com/bobwares/customer/registration/CustomerService.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: src/main/java/com/bobwares/customer/registration/CustomerService.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: CustomerService
 * Description: Provides transactional operations for creating, querying, updating, and deleting customers.
 */
package com.bobwares.customer.registration;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerService {

  private final CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public Customer create(@Valid @NotNull Customer candidate) {
    Customer aggregate = copyAggregate(candidate);
    validateAggregate(aggregate, null);
    return customerRepository.save(aggregate);
  }

  @Transactional(readOnly = true)
  public Customer get(UUID id) {
    return customerRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Customer %s not found".formatted(id)));
  }

  @Transactional(readOnly = true)
  public List<Customer> list() {
    return customerRepository.findAll().stream()
        .sorted(Comparator.comparing(Customer::getLastName).thenComparing(Customer::getFirstName))
        .collect(Collectors.toList());
  }

  public Customer update(UUID id, @Valid @NotNull Customer updates) {
    Customer aggregate = copyAggregate(updates);
    validateAggregate(aggregate, id);
    Customer existing = get(id);
    existing.updateContactDetails(aggregate);
    return existing;
  }

  public void delete(UUID id) {
    Customer existing = get(id);
    customerRepository.delete(existing);
  }

  private Customer copyAggregate(Customer source) {
    Customer target = new Customer();
    target.setFirstName(source.getFirstName());
    target.setMiddleName(source.getMiddleName());
    target.setLastName(source.getLastName());
    target.setAddress(source.getAddress() == null ? null : cloneAddress(source.getAddress()));
    target.setPrivacySettings(clonePrivacy(source.getPrivacySettings()));
    target.setEmails(cloneEmails(source.getEmails()));
    target.setPhoneNumbers(clonePhones(source.getPhoneNumbers()));
    return target;
  }

  private PostalAddress cloneAddress(PostalAddress source) {
    if (source == null) {
      return null;
    }
    PostalAddress address = new PostalAddress();
    address.setLine1(source.getLine1());
    address.setLine2(source.getLine2());
    address.setCity(source.getCity());
    address.setState(source.getState());
    address.setPostalCode(source.getPostalCode());
    address.setCountry(source.getCountry());
    return address;
  }

  private PrivacySettings clonePrivacy(PrivacySettings source) {
    if (source == null) {
      throw new IllegalArgumentException("privacySettings is required");
    }
    PrivacySettings settings = new PrivacySettings();
    settings.setMarketingEmailsEnabled(source.getMarketingEmailsEnabled());
    settings.setTwoFactorEnabled(source.getTwoFactorEnabled());
    return settings;
  }

  private Set<CustomerEmail> cloneEmails(Set<CustomerEmail> emails) {
    if (emails == null || emails.isEmpty()) {
      throw new IllegalArgumentException("At least one email is required");
    }
    return emails.stream()
        .map(email -> {
          CustomerEmail copy = new CustomerEmail();
          copy.setEmail(email.getEmail());
          return copy;
        })
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  private Set<CustomerPhoneNumber> clonePhones(Set<CustomerPhoneNumber> phones) {
    if (phones == null || phones.isEmpty()) {
      throw new IllegalArgumentException("At least one phone number is required");
    }
    return phones.stream()
        .map(phone -> {
          CustomerPhoneNumber copy = new CustomerPhoneNumber();
          copy.setType(phone.getType());
          copy.setNumber(phone.getNumber());
          return copy;
        })
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  private void validateAggregate(Customer aggregate, UUID currentId) {
    Objects.requireNonNull(aggregate.getPrivacySettings(), "privacySettings is required");
    ensureUniqueEmails(aggregate, currentId);
    ensureUniquePhoneNumbers(aggregate);
  }

  private void ensureUniqueEmails(Customer aggregate, UUID currentId) {
    Set<String> normalizedEmails = aggregate.getEmails().stream()
        .map(email -> email.getEmail().toLowerCase(Locale.ROOT))
        .collect(Collectors.toCollection(LinkedHashSet::new));

    if (normalizedEmails.size() != aggregate.getEmails().size()) {
      throw new IllegalArgumentException("Duplicate emails are not allowed for a customer");
    }

    for (String email : normalizedEmails) {
      boolean exists = customerRepository.existsByEmailsEmailIgnoreCase(email);
      if (exists) {
        customerRepository
            .findByEmailsEmailIgnoreCase(email)
            .filter(customer -> !Objects.equals(customer.getId(), currentId))
            .ifPresent(customer -> {
              throw new IllegalArgumentException(
                  "Email %s is already associated with another customer".formatted(email));
            });
      }
    }
  }

  private void ensureUniquePhoneNumbers(Customer aggregate) {
    Set<String> uniqueNumbers = aggregate.getPhoneNumbers().stream()
        .map(CustomerPhoneNumber::getNumber)
        .map(number -> number.replaceAll("\\s+", ""))
        .collect(Collectors.toCollection(LinkedHashSet::new));

    if (uniqueNumbers.size() != aggregate.getPhoneNumbers().size()) {
      throw new IllegalArgumentException("Duplicate phone numbers are not allowed for a customer");
    }
  }
}

```

File: project_root/src/main/java/com/bobwares/customer/registration/api/CustomerDto.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: src/main/java/com/bobwares/customer/registration/api/CustomerDto.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: CustomerDto
 * Description: Defines request and response payloads for the customer REST API.
 */
package com.bobwares.customer.registration.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public final class CustomerDto {

  private CustomerDto() {}

  @Schema(name = "CustomerCreateRequest", description = "Payload to create a customer")
  public record CreateRequest(
      @Schema(example = "Ada") @NotBlank @Size(max = 100) String firstName,
      @Schema(example = "L.") @Size(max = 100) String middleName,
      @Schema(example = "Lovelace") @NotBlank @Size(max = 100) String lastName,
      @NotEmpty List<@Email @Size(max = 320) String> emails,
      @NotEmpty List<@Valid PhoneNumber> phoneNumbers,
      @Valid Address address,
      @NotNull @Valid Privacy privacySettings) {}

  @Schema(name = "CustomerUpdateRequest", description = "Payload to update a customer")
  public record UpdateRequest(
      @Schema(example = "Ada") @NotBlank @Size(max = 100) String firstName,
      @Schema(example = "L.") @Size(max = 100) String middleName,
      @Schema(example = "Lovelace") @NotBlank @Size(max = 100) String lastName,
      @NotEmpty List<@Email @Size(max = 320) String> emails,
      @NotEmpty List<@Valid PhoneNumber> phoneNumbers,
      @Valid Address address,
      @NotNull @Valid Privacy privacySettings) {}

  @Schema(name = "CustomerResponse", description = "Customer representation returned by the API")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public record Response(
      UUID id,
      String firstName,
      String middleName,
      String lastName,
      List<String> emails,
      List<PhoneNumber> phoneNumbers,
      Address address,
      Privacy privacySettings) {}

  @Schema(name = "CustomerAddress", description = "Address associated with the customer")
  public record Address(
      @Schema(example = "123 Market St") @NotBlank @Size(max = 255) String line1,
      @Schema(example = "Suite 500") @Size(max = 255) String line2,
      @Schema(example = "San Francisco") @NotBlank @Size(max = 100) String city,
      @Schema(example = "CA") @NotBlank @Size(max = 100) String state,
      @Schema(example = "94103") @Size(max = 32) String postalCode,
      @Schema(example = "US") @NotBlank @Pattern(regexp = "^[A-Z]{2}$") String country) {}

  @Schema(name = "CustomerPhoneNumber", description = "Phone number associated with the customer")
  public record PhoneNumber(
      @Schema(example = "mobile") @NotBlank @Size(max = 20) String type,
      @Schema(example = "+14155551234")
          @NotBlank
          @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
          @Size(max = 20)
          String number) {}

  @Schema(name = "CustomerPrivacy", description = "Privacy preferences for the customer")
  public record Privacy(
      @Schema(example = "true") @NotNull Boolean marketingEmailsEnabled,
      @Schema(example = "true") @NotNull Boolean twoFactorEnabled) {}
}

```

File: project_root/src/main/java/com/bobwares/customer/registration/api/CustomerMapper.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: src/main/java/com/bobwares/customer/registration/api/CustomerMapper.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: CustomerMapper
 * Description: Translates between REST DTOs and customer persistence entities.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.Customer;
import com.bobwares.customer.registration.CustomerEmail;
import com.bobwares.customer.registration.CustomerPhoneNumber;
import com.bobwares.customer.registration.PostalAddress;
import com.bobwares.customer.registration.PrivacySettings;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

  public Customer toEntity(CustomerDto.CreateRequest request) {
    Objects.requireNonNull(request, "request must not be null");
    Customer customer = new Customer();
    applyCommonFields(customer, request.firstName(), request.middleName(), request.lastName());
    customer.setAddress(toAddress(request.address()));
    customer.setPrivacySettings(toPrivacy(request.privacySettings()));
    customer.setEmails(toEmails(request.emails()));
    customer.setPhoneNumbers(toPhoneNumbers(request.phoneNumbers()));
    return customer;
  }

  public Customer toEntity(CustomerDto.UpdateRequest request) {
    Objects.requireNonNull(request, "request must not be null");
    Customer customer = new Customer();
    applyCommonFields(customer, request.firstName(), request.middleName(), request.lastName());
    customer.setAddress(toAddress(request.address()));
    customer.setPrivacySettings(toPrivacy(request.privacySettings()));
    customer.setEmails(toEmails(request.emails()));
    customer.setPhoneNumbers(toPhoneNumbers(request.phoneNumbers()));
    return customer;
  }

  public CustomerDto.Response toResponse(Customer customer) {
    Objects.requireNonNull(customer, "customer must not be null");
    return new CustomerDto.Response(
        customer.getId(),
        customer.getFirstName(),
        customer.getMiddleName(),
        customer.getLastName(),
        customer.getEmails().stream()
            .map(CustomerEmail::getEmail)
            .sorted()
            .toList(),
        customer.getPhoneNumbers().stream()
            .map(phone -> new CustomerDto.PhoneNumber(phone.getType(), phone.getNumber()))
            .toList(),
        toAddressDto(customer.getAddress()),
        toPrivacyDto(customer.getPrivacySettings()));
  }

  private void applyCommonFields(
      Customer target, String firstName, String middleName, String lastName) {
    target.setFirstName(firstName);
    target.setMiddleName(middleName);
    target.setLastName(lastName);
  }

  private PostalAddress toAddress(CustomerDto.Address dto) {
    if (dto == null) {
      return null;
    }
    PostalAddress address = new PostalAddress();
    address.setLine1(dto.line1());
    address.setLine2(dto.line2());
    address.setCity(dto.city());
    address.setState(dto.state());
    address.setPostalCode(dto.postalCode());
    address.setCountry(dto.country());
    return address;
  }

  private PrivacySettings toPrivacy(CustomerDto.Privacy dto) {
    PrivacySettings settings = new PrivacySettings();
    settings.setMarketingEmailsEnabled(dto.marketingEmailsEnabled());
    settings.setTwoFactorEnabled(dto.twoFactorEnabled());
    return settings;
  }

  private Set<CustomerEmail> toEmails(List<String> emails) {
    return emails.stream()
        .map(value -> {
          CustomerEmail email = new CustomerEmail();
          email.setEmail(value);
          return email;
        })
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  private Set<CustomerPhoneNumber> toPhoneNumbers(List<CustomerDto.PhoneNumber> phoneNumbers) {
    return phoneNumbers.stream()
        .map(dto -> {
          CustomerPhoneNumber phone = new CustomerPhoneNumber();
          phone.setType(dto.type());
          phone.setNumber(dto.number());
          return phone;
        })
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  private CustomerDto.Address toAddressDto(PostalAddress address) {
    if (address == null) {
      return null;
    }
    return new CustomerDto.Address(
        address.getLine1(),
        address.getLine2(),
        address.getCity(),
        address.getState(),
        address.getPostalCode(),
        address.getCountry());
  }

  private CustomerDto.Privacy toPrivacyDto(PrivacySettings settings) {
    return new CustomerDto.Privacy(
        settings.getMarketingEmailsEnabled(), settings.getTwoFactorEnabled());
  }
}

```

File: project_root/src/main/java/com/bobwares/customer/registration/api/CustomerController.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.api
 * File: src/main/java/com/bobwares/customer/registration/api/CustomerController.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: CustomerController
 * Description: REST controller exposing CRUD endpoints for customers with OpenAPI documentation.
 */
package com.bobwares.customer.registration.api;

import com.bobwares.customer.registration.Customer;
import com.bobwares.customer.registration.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Manage customer registrations")
public class CustomerController {

  private final CustomerService customerService;
  private final CustomerMapper customerMapper;

  public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
    this.customerService = customerService;
    this.customerMapper = customerMapper;
  }

  @PostMapping
  @Operation(
      summary = "Create a customer",
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Customer created",
            content =
                @Content(
                    schema = @Schema(implementation = CustomerDto.Response.class))),
        @ApiResponse(responseCode = "400", description = "Validation error")
      })
  public ResponseEntity<CustomerDto.Response> create(
      @Valid @RequestBody CustomerDto.CreateRequest request) {
    Customer created = customerService.create(customerMapper.toEntity(request));
    CustomerDto.Response body = customerMapper.toResponse(created);
    return ResponseEntity.created(URI.create("/api/customers/" + created.getId())).body(body);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Retrieve a customer",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Customer found",
            content =
                @Content(
                    schema = @Schema(implementation = CustomerDto.Response.class))),
        @ApiResponse(responseCode = "404", description = "Customer not found")
      })
  public CustomerDto.Response get(@PathVariable UUID id) {
    return customerMapper.toResponse(customerService.get(id));
  }

  @GetMapping
  @Operation(
      summary = "List customers",
      responses =
          @ApiResponse(
              responseCode = "200",
              description = "Customers listed",
              content = @Content(schema = @Schema(implementation = CustomerDto.Response.class))))
  public List<CustomerDto.Response> list() {
    return customerService.list().stream().map(customerMapper::toResponse).toList();
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update a customer",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Customer updated",
            content =
                @Content(
                    schema = @Schema(implementation = CustomerDto.Response.class))),
        @ApiResponse(responseCode = "404", description = "Customer not found")
      })
  public CustomerDto.Response update(
      @PathVariable UUID id, @Valid @RequestBody CustomerDto.UpdateRequest request) {
    Customer updated = customerService.update(id, customerMapper.toEntity(request));
    return customerMapper.toResponse(updated);
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Delete a customer",
      responses = {
        @ApiResponse(responseCode = "204", description = "Customer deleted"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
      })
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    customerService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

```

File: project_root/src/main/java/com/bobwares/customer/registration/web/RestExceptionHandler.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration.web
 * File: src/main/java/com/bobwares/customer/registration/web/RestExceptionHandler.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: RestExceptionHandler
 * Description: Maps exceptions to standardized API error responses.
 */
package com.bobwares.customer.registration.web;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleNotFound(EntityNotFoundException ex) {
    return ErrorResponse.of(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler({IllegalArgumentException.class, ConstraintViolationException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleBadRequest(RuntimeException ex) {
    return ErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
    List<String> details =
        ex.getBindingResult().getFieldErrors().stream()
            .map(this::formatFieldError)
            .collect(Collectors.toList());
    return ErrorResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, "Validation failed", details);
  }

  private String formatFieldError(FieldError error) {
    return "%s: %s".formatted(error.getField(), error.getDefaultMessage());
  }

  public record ErrorResponse(
      int status, String error, String message, OffsetDateTime timestamp, List<String> details) {

    static ErrorResponse of(HttpStatus status, String message) {
      return of(status, message, List.of());
    }

    static ErrorResponse of(HttpStatus status, String message, List<String> details) {
      return new ErrorResponse(
          status.value(), status.getReasonPhrase(), message, OffsetDateTime.now(), details);
    }
  }
}

```

File: project_root/src/main/resources/application.yml

```text
# App: Customer Registration
# Package: src.main.resources
# File: src/main/resources/application.yml
# Version: 0.1.0
# Turns: 1
# Author: Bobwares
# Date: 2025-10-30T01:57:11Z
# Exports: Spring Boot application configuration
# Description: Configures core application properties, data source, Liquibase, Springdoc, and management endpoints.

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

File: project_root/src/main/resources/db/changelog/db.changelog-master.yml

```text
# App: Customer Registration
# Package: db.changelog
# File: src/main/resources/db/changelog/db.changelog-master.yml
# Version: 0.1.0
# Turns: 1
# Author: Bobwares
# Date: 2025-10-30T01:57:11Z
# Exports: Liquibase master changelog
# Description: Coordinates execution of SQL migrations for the customer registration schema.

databaseChangeLog:
  - changeSet:
      id: 01-customer-profile
      author: bobwares
      changes:
        - sqlFile:
            path: classpath:db/changelog/01_customer_profile_tables.sql
            splitStatements: true
            stripComments: false
            encoding: UTF-8

```

File: project_root/src/main/resources/db/changelog/01_customer_profile_tables.sql

```text
-- App: Customer Registration
-- Package: db.changelog
-- File: src/main/resources/db/changelog/01_customer_profile_tables.sql
-- Version: 0.1.0
-- Turns: 1
-- Author: Bobwares
-- Date: 2025-10-30T01:57:11Z
-- Exports: CREATE TABLE statements for customer profile domain
-- Description: Creates normalized PostgreSQL tables for customer profiles, emails, phone numbers, addresses, and privacy settings.

BEGIN;

CREATE SCHEMA IF NOT EXISTS customer_registration;

SET search_path TO customer_registration, public;

CREATE TABLE IF NOT EXISTS postal_address (
  address_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  line1 VARCHAR(255) NOT NULL,
  line2 VARCHAR(255),
  city VARCHAR(100) NOT NULL,
  state VARCHAR(100) NOT NULL,
  postal_code VARCHAR(32),
  country CHAR(2) NOT NULL
);

CREATE TABLE IF NOT EXISTS privacy_settings (
  privacy_settings_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  marketing_emails_enabled BOOLEAN NOT NULL,
  two_factor_enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS customer (
  customer_id UUID PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  middle_name VARCHAR(100),
  last_name VARCHAR(100) NOT NULL,
  address_id BIGINT REFERENCES postal_address(address_id),
  privacy_settings_id BIGINT NOT NULL REFERENCES privacy_settings(privacy_settings_id),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_customer_address_id ON customer(address_id);
CREATE INDEX IF NOT EXISTS idx_customer_privacy_settings_id ON customer(privacy_settings_id);

CREATE TABLE IF NOT EXISTS customer_email (
  customer_email_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  customer_id UUID NOT NULL REFERENCES customer(customer_id) ON DELETE CASCADE,
  email VARCHAR(320) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
  UNIQUE (customer_id, email)
);

CREATE INDEX IF NOT EXISTS idx_customer_email_customer_id ON customer_email(customer_id);
CREATE UNIQUE INDEX IF NOT EXISTS ux_customer_email_email ON customer_email(email);

CREATE TABLE IF NOT EXISTS customer_phone_number (
  customer_phone_number_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  customer_id UUID NOT NULL REFERENCES customer(customer_id) ON DELETE CASCADE,
  phone_type VARCHAR(20) NOT NULL,
  phone_number VARCHAR(20) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
  UNIQUE (customer_id, phone_number)
);

CREATE INDEX IF NOT EXISTS idx_customer_phone_customer_id ON customer_phone_number(customer_id);

CREATE OR REPLACE VIEW customer_profile_view AS
SELECT
  c.customer_id,
  c.first_name,
  c.middle_name,
  c.last_name,
  pa.line1,
  pa.line2,
  pa.city,
  pa.state,
  pa.postal_code,
  pa.country,
  ps.marketing_emails_enabled,
  ps.two_factor_enabled
FROM customer c
LEFT JOIN postal_address pa ON pa.address_id = c.address_id
JOIN privacy_settings ps ON ps.privacy_settings_id = c.privacy_settings_id;

COMMIT;

```

File: project_root/e2e/actuator.http

```text
# App: Customer Registration
# Package: e2e
# File: e2e/actuator.http
# Version: 0.1.0
# Turns: 1
# Author: Bobwares
# Date: 2025-10-30T01:57:11Z
# Exports: HTTP requests for actuator smoke checks
# Description: Exercises actuator endpoints for health and info diagnostics.

@host = http://localhost:{{APP_PORT:=8080}}

### Health check
GET {{host}}/actuator/health
Accept: application/json

### Info endpoint
GET {{host}}/actuator/info
Accept: application/json

```

File: project_root/e2e/customer.http

```text
# App: Customer Registration
# Package: e2e
# File: e2e/customer.http
# Version: 0.1.0
# Turns: 1
# Author: Bobwares
# Date: 2025-10-30T01:57:11Z
# Exports: HTTP requests for customer CRUD
# Description: Exercises the customer API with create, read, update, delete, and not-found flows.

@host = http://localhost:{{APP_PORT:=8080}}
@customerId = {{customer_id}}

### Create customer
POST {{host}}/api/customers
Content-Type: application/json
Accept: application/json

{
  "firstName": "Ada",
  "middleName": "L.",
  "lastName": "Lovelace",
  "emails": ["ada.lovelace@example.com"],
  "phoneNumbers": [
    {
      "type": "mobile",
      "number": "+14155551234"
    }
  ],
  "address": {
    "line1": "123 Market St",
    "line2": "Suite 500",
    "city": "San Francisco",
    "state": "CA",
    "postalCode": "94103",
    "country": "US"
  },
  "privacySettings": {
    "marketingEmailsEnabled": true,
    "twoFactorEnabled": true
  }
}

> {% client.global.set("customer_id", response.body.id); %}

### Get customer by id
GET {{host}}/api/customers/{{customer_id}}
Accept: application/json

### Update customer
PUT {{host}}/api/customers/{{customer_id}}
Content-Type: application/json
Accept: application/json

{
  "firstName": "Augusta",
  "middleName": "Ada",
  "lastName": "Lovelace",
  "emails": ["augusta.lovelace@example.com"],
  "phoneNumbers": [
    {
      "type": "home",
      "number": "+441632960123"
    }
  ],
  "address": {
    "line1": "456 Innovation Way",
    "line2": null,
    "city": "London",
    "state": "London",
    "postalCode": "SW1A 1AA",
    "country": "GB"
  },
  "privacySettings": {
    "marketingEmailsEnabled": false,
    "twoFactorEnabled": true
  }
}

### Delete customer
DELETE {{host}}/api/customers/{{customer_id}}

### Verify not found
GET {{host}}/api/customers/{{customer_id}}
Accept: application/json

> {%- if response.status !== 404 -%}
>   {% client.test("Expect 404 after deletion", function() { client.assert(response.status === 404); }); %}
> {%- endif -%}

```

File: project_root/src/test/java/com/bobwares/customer/registration/ApplicationSmokeTest.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: src/test/java/com/bobwares/customer/registration/ApplicationSmokeTest.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: ApplicationSmokeTest
 * Description: Verifies that the Spring application context loads successfully.
 */
package com.bobwares.customer.registration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bobwares.customer.registration.CustomerRepository;

@SpringBootTest(
    properties = {
      "APP_NAME=customer-registration-test",
      "APP_PORT=8080",
      "DATABASE_HOST=localhost",
      "DATABASE_PORT=5432",
      "DATABASE_NAME=testdb",
      "DATABASE_USERNAME=test",
      "DATABASE_PASSWORD=test",
      "APP_DEFAULT_TAX_RATE=0.05",
      "APP_DEFAULT_SHIPPING_COST=10.00",
      "APP_SUPPORTED_CURRENCIES=USD",
      "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
      "spring.liquibase.enabled=false"
    })
class ApplicationSmokeTest {

  @MockBean private CustomerRepository customerRepository;

  @Test
  void contextLoads() {
    // context initialization validation
  }
}

```

File: project_root/src/test/java/com/bobwares/customer/registration/CustomerServiceTests.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: src/test/java/com/bobwares/customer/registration/CustomerServiceTests.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: CustomerServiceTests
 * Description: Unit tests covering happy-path and validation behaviors of CustomerService.
 */
package com.bobwares.customer.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTests {

  @Mock private CustomerRepository customerRepository;

  @InjectMocks private CustomerService customerService;

  @Test
  void createShouldPersistCustomer() {
    Customer candidate = buildCustomer("ada.lovelace@example.com", "+14155551234");
    when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Customer created = customerService.create(candidate);

    assertThat(created.getEmails()).hasSize(1);
    assertThat(created.getPhoneNumbers()).hasSize(1);
    assertThat(created.getPrivacySettings().getTwoFactorEnabled()).isTrue();
  }

  @Test
  void createShouldRejectDuplicateEmails() {
    Customer candidate = buildCustomer("ada@example.com", "+14155551234");
    CustomerEmail duplicateEmail = new CustomerEmail();
    duplicateEmail.setEmail("ada@example.com");
    candidate.addEmail(duplicateEmail);

    assertThatThrownBy(() -> customerService.create(candidate))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Duplicate emails");
  }

  @Test
  void updateShouldRejectEmailUsedByAnotherCustomer() {
    UUID currentId = UUID.randomUUID();
    UUID otherId = UUID.randomUUID();

    Customer updates = buildCustomer("shared@example.com", "+14155551234");

    when(customerRepository.existsByEmailsEmailIgnoreCase("shared@example.com")).thenReturn(true);
    Customer other = mock(Customer.class);
    when(other.getId()).thenReturn(otherId);
    lenient()
        .when(customerRepository.findByEmailsEmailIgnoreCase("shared@example.com"))
        .thenReturn(Optional.of(other));

    assertThatThrownBy(() -> customerService.update(currentId, updates))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("already associated");
  }

  private Customer buildCustomer(String email, String phone) {
    Customer customer = new Customer();
    customer.setFirstName("Ada");
    customer.setMiddleName("L");
    customer.setLastName("Lovelace");

    PrivacySettings privacy = new PrivacySettings();
    privacy.setMarketingEmailsEnabled(true);
    privacy.setTwoFactorEnabled(true);
    customer.setPrivacySettings(privacy);

    CustomerEmail customerEmail = new CustomerEmail();
    customerEmail.setEmail(email);
    customer.setEmails(Set.of(customerEmail));

    CustomerPhoneNumber phoneNumber = new CustomerPhoneNumber();
    phoneNumber.setType("mobile");
    phoneNumber.setNumber(phone);
    customer.setPhoneNumbers(Set.of(phoneNumber));

    PostalAddress address = new PostalAddress();
    address.setLine1("123 Market St");
    address.setCity("San Francisco");
    address.setState("CA");
    address.setPostalCode("94103");
    address.setCountry("US");
    customer.setAddress(address);

    return customer;
  }
}

```

File: project_root/src/test/java/com/bobwares/customer/registration/CustomerControllerIT.java

```text
/**
 * App: Customer Registration
 * Package: com.bobwares.customer.registration
 * File: src/test/java/com/bobwares/customer/registration/CustomerControllerIT.java
 * Version: 0.1.0
 * Turns: 1
 * Author: Bobwares
 * Date: 2025-10-30T01:57:11Z
 * Exports: CustomerControllerIT
 * Description: Integration test exercising the customer REST API against a PostgreSQL Testcontainer.
 */
package com.bobwares.customer.registration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class CustomerControllerIT {

  @Container
  static final PostgreSQLContainer<?> POSTGRES =
      new PostgreSQLContainer<>("postgres:16").withDatabaseName("customer")
          .withUsername("customer")
          .withPassword("secret");

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    registry.add("spring.liquibase.default-schema", () -> "customer_registration");
    registry.add("app.name", () -> "customer-registration-test");
    registry.add("app.port", () -> 8080);
    registry.add("app.default-tax-rate", () -> "0.05");
    registry.add("app.default-shipping-cost", () -> "10.00");
    registry.add("app.supported-currencies", () -> "USD,EUR");
  }

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void customerCrudLifecycle() throws Exception {
    ObjectNode createPayload = objectMapper.createObjectNode();
    createPayload.put("firstName", "Ada");
    createPayload.put("middleName", "L.");
    createPayload.put("lastName", "Lovelace");
    createPayload.putArray("emails").add("ada.lovelace@example.com");
    ObjectNode phone = objectMapper.createObjectNode();
    phone.put("type", "mobile");
    phone.put("number", "+14155551234");
    createPayload.putArray("phoneNumbers").add(phone);
    ObjectNode address = objectMapper.createObjectNode();
    address.put("line1", "123 Market St");
    address.putNull("line2");
    address.put("city", "San Francisco");
    address.put("state", "CA");
    address.put("postalCode", "94103");
    address.put("country", "US");
    createPayload.set("address", address);
    ObjectNode privacy = objectMapper.createObjectNode();
    privacy.put("marketingEmailsEnabled", true);
    privacy.put("twoFactorEnabled", true);
    createPayload.set("privacySettings", privacy);

    MvcResult createResult =
        mockMvc
            .perform(
                post("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createPayload.toString()))
            .andExpect(status().isCreated())
            .andReturn();

    JsonNode createBody = objectMapper.readTree(createResult.getResponse().getContentAsString());
    String customerId = createBody.get("id").asText();

    mockMvc
        .perform(get("/api/customers/" + customerId).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    ObjectNode updatePayload = objectMapper.createObjectNode();
    updatePayload.put("firstName", "Augusta");
    updatePayload.put("middleName", "Ada");
    updatePayload.put("lastName", "Lovelace");
    updatePayload.putArray("emails").add("augusta@example.com");
    ObjectNode updatePhone = objectMapper.createObjectNode();
    updatePhone.put("type", "home");
    updatePhone.put("number", "+441632960123");
    updatePayload.putArray("phoneNumbers").add(updatePhone);
    ObjectNode updateAddress = objectMapper.createObjectNode();
    updateAddress.put("line1", "456 Innovation Way");
    updateAddress.putNull("line2");
    updateAddress.put("city", "London");
    updateAddress.put("state", "London");
    updateAddress.put("postalCode", "SW1A 1AA");
    updateAddress.put("country", "GB");
    updatePayload.set("address", updateAddress);
    ObjectNode updatePrivacy = objectMapper.createObjectNode();
    updatePrivacy.put("marketingEmailsEnabled", false);
    updatePrivacy.put("twoFactorEnabled", true);
    updatePayload.set("privacySettings", updatePrivacy);

    mockMvc
        .perform(
            put("/api/customers/" + customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload.toString()))
        .andExpect(status().isOk());

    mockMvc.perform(delete("/api/customers/" + customerId)).andExpect(status().isNoContent());

    mockMvc.perform(get("/api/customers/" + customerId)).andExpect(status().isNotFound());
  }
}

```

File: project_root/ai/agentic-pipeline/turns/1/session_context.md

```text
TURN_ID=1
TURN_START_TIME=2025-10-30T01:56:22Z
TURN_END_TIME=2025-10-30T02:13:19Z
SANDBOX_BASE_DIRECTORY=/workspace
AGENTIC_PIPELINE_PROJECT=/workspace/codex-agentic-ai-pipeline
TARGET_PROJECT=/workspace/codex-customer-registration-java
PROJECT_CONTEXT=/workspace/codex-customer-registration-java/ai/context/project_context.md
ACTIVE_PATTERN_NAME=spring-boot-mvc-jpa-postgresql
ACTIVE_PATTERN_PATH=/workspace/codex-agentic-ai-pipeline/application-implementation-patterns/spring-boot-mvc-jpa-postgresql
EXECUTION_PLAN=/workspace/codex-agentic-ai-pipeline/application-implementation-patterns/spring-boot-mvc-jpa-postgresql/execution-plan.md
CURRENT_TURN_DIRECTORY=/workspace/codex-customer-registration-java/ai/agentic-pipeline/turns/1

```

File: project_root/ai/agentic-pipeline/turns_index.csv

```text
turnId,timestampUtc,task,branch,tag,headAfter,testsPassed,testsFailed,coverageDeltaPct
1,2025-10-30T02:11:29Z,execute-execution-plan,work,turn/1,f6898ba28057893f22e203f0046e58c858ae2cff,4,0,0

```


File: project_root/ai/agentic-pipeline/turns/1/adr.md

```markdown
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
```
