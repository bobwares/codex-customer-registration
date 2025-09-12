# AI Workspace Files

_Generated on: 2025-09-12T19:55:09Z_

## File: ./ai/project-parser/../agentic-pipeline/turns/1/adr.md

```markdown
# Use Spring Boot MVC with JPA and PostgreSQL

**Status**: Accepted

**Date**: 2025-09-12

**Context**
The customer registration service requires a synchronous HTTP API with relational persistence.

**Decision**
Adopt Spring Boot MVC with Spring Data JPA backed by PostgreSQL and Flyway migrations.

**Consequences**
- Provides rapid development with robust ecosystem and tooling.
- Requires running PostgreSQL for application and tests.

```

## File: ./ai/project-parser/../agentic-pipeline/turns/1/changelog.md

```markdown
# Turn 1 – 2025-09-12T19:54:27Z

## Changes
- Scaffolded Spring Boot MVC JPA PostgreSQL project.
- Implemented Customer entity, repository, service, and REST controller.
- Added Flyway migration, application configuration, and repository tests.

```

## File: ./ai/project-parser/../context/codex_project_context.md

```markdown
# Codex Project Context

# Project 

- Name: Customer Registration
- Detailed Description:

  The Customer Registration project provides a standardized service for securely onboarding new customers into a system. It manages the complete registration lifecycle—from capturing customer details and validating inputs, to persisting records in a relational database, and exposing CRUD operations through a REST API. The system is designed to integrate with enterprise authentication and authorization frameworks, ensuring compliance with data security policies. It also establishes a foundation for downstream processes such as customer profile enrichment, service eligibility checks, and integration with external systems.

- Short Description

    Customer Registration is a service that securely captures, validates, and manages new customer data, exposing standardized APIs for onboarding and downstream integrations.


- Author: Bobwares ([bobwares@outlook.com](mailto:bobwares@outlook.com)) 

## Pattern

build project using pattern: spring-boot-mvc-jpa-postgresql

 
## Maven 

- groupId: com.bobwares.customer
- artifactId: registration
- name: Customer Registration
- description: Spring Boot service for managing customer registrations

## Domain
- Domain Object
  Customer
- REST API Request Schema
  load ./schemas/customer.schema.json
- REST API Response Schema
  load ./schemas/customer.schema.json
- Persisted Data schema
    load ./schemas/customer.schema.json
```

## File: ./ai/project-parser/../context/schemas/customer.schema.json

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "title": "CustomerProfile",
  "type": "object",
  "properties": {
    "id": {
      "type": "string",
      "format": "uuid",
      "description": "Unique identifier for the customer profile"
    },
    "firstName": {
      "type": "string",
      "minLength": 1,
      "description": "Customer’s given name"
    },
    "middleName": {
      "type": "string",
      "description": "Customer’s middle name or initial",
      "minLength": 1
    },
    "lastName": {
      "type": "string",
      "minLength": 1,
      "description": "Customer’s family name"
    },
    "emails": {
      "type": "array",
      "description": "List of the customer’s email addresses",
      "items": {
        "type": "string",
        "format": "email"
      },
      "minItems": 1,
      "uniqueItems": true
    },
    "phoneNumbers": {
      "type": "array",
      "description": "List of the customer’s phone numbers",
      "items": {
        "$ref": "#/definitions/PhoneNumber"
      },
      "minItems": 1
    },
    "address": {
      "$ref": "#/definitions/PostalAddress"
    },
    "privacySettings": {
      "$ref": "#/definitions/PrivacySettings"
    }
  },
  "required": [
    "id",
    "firstName",
    "lastName",
    "emails",
    "privacySettings"
  ],
  "additionalProperties": false,
  "definitions": {

    "PhoneNumber": {
      "type": "object",
      "properties": {
        "type": {
          "type": "string",
          "description": "Type of phone number",
          "enum": ["mobile", "home", "work", "other"]
        },
        "number": {
          "type": "string",
          "pattern": "^\\+?[1-9]\\d{1,14}$",
          "description": "Phone number in E.164 format"
        }
      },
      "required": ["type", "number"],
      "additionalProperties": false
    },
    "PostalAddress": {
      "type": "object",
      "properties": {
        "line1": {
          "type": "string",
          "minLength": 1,
          "description": "Street address, P.O. box, company name, c/o"
        },
        "line2": {
          "type": "string",
          "description": "Apartment, suite, unit, building, floor, etc."
        },
        "city": {
          "type": "string",
          "minLength": 1,
          "description": "City or locality"
        },
        "state": {
          "type": "string",
          "minLength": 1,
          "description": "State, province, or region"
        },
        "postalCode": {
          "type": "string",
          "description": "ZIP or postal code"
        },
        "country": {
          "type": "string",
          "minLength": 2,
          "maxLength": 2,
          "description": "ISO 3166-1 alpha-2 country code"
        }
      },
      "required": ["line1", "city", "state", "postalCode", "country"],
      "additionalProperties": false
    },
    "PrivacySettings": {
      "type": "object",
      "properties": {
        "marketingEmailsEnabled": {
          "type": "boolean",
          "description": "Whether the user opts in to marketing emails"
        },
        "twoFactorEnabled": {
          "type": "boolean",
          "description": "Whether two-factor authentication is enabled"
        }
      },
      "required": [
        "marketingEmailsEnabled",
        "twoFactorEnabled"
      ],
      "additionalProperties": false
    }
  }
}

```

## File: project_root/AGENTS.md

```markdown
# Codex Session Context

open and read file /workspace/codex-agentic-ai-pipline/agentic-pipeline/context/codex_session_context.md

# Turns

open and read file /workspace/codex-agentic-ai-pipeline/agentic-pipeline/context/Turns_Technical_Design.md

# Codex Project Context

open and read file ./ai/context/codex_project_context.md


# Patterns

open and read pattern specified in the codex_project_context in the directory /workspace/codex-agentic-ai-pipeline/agentic-pipeline/patterns

# Tasks

tasks are in directory /workspace/codex-agentic-ai-pipeline/agentic-pipeline/tasks

# Tools

tools are in directory /workspace/codex-agentic-ai-pipeline/agentic-pipeline/tools



# Coding Standards

## Metadata Header

— Every source, test, and IAC file must begin with Metadata Header comment section.
- exclude pom.xml
- Placement: Top of file, above any import or code statements.
- Version: Increment only when the file contents change.
- Date: UTC timestamp of the most recent change.


### Metadata Header Template
    ```markdown
      /**
      * App: {{Application Name}}
      * Package: {{package}}
      * File: {{file name}}
      * Version: semantic versioning starting at 0.1.0
      * Turns: append {{turn number}} list when created or updated.
      * Author: {{author}}
      * Date: {{YYYY-MM-DDThh:mm:ssZ}}
      * Exports: {{ exported functions, types, and variables.}}
      * Description: documentate the function of the class or function. Document each
      *              method or function in the file.
      */
    ````

### Source Versioning Rules

      * Use **semantic versioning** (`MAJOR.MINOR.PATCH`).
      * Start at **0.1.0**; update only when code or configuration changes.
      * Update the version in the source file if it is updated during a turn.

# Logging

## Change Log

- Track changes each “AI turn” in: project_root/ai/agentic-pipeline/turns/current turn directory/changelog.md
- append changes to project change log located project_root/changelog.md

### Change Log Entry Template

    # Turn: {{turn number}}  – {{Date Time of execution}}
    
    ## prompt

    {{ input prompt}}

    #### Task
    <Task>
    
    #### Changes
    - Initial project structure and configuration.
    
    ### 0.0.2 – 2025-06-08 07:23:08 UTC (work)
    
    #### Task
    <Task>
    
    #### Changes
    - Add tsconfig for ui and api.
    - Create src directories with unit-test folders.
    - Add e2e test directory for Playwright.
   

## ADR (Architecture Decision Record)

### Purpose

The adr.md` folder captures **concise, high-signal Architecture Decision Records** whenever the
AI coding agent (or a human) makes a non-obvious technical or architectural choice.
Storing ADRs keeps the project’s architectural rationale transparent and allows reviewers to
understand **why** a particular path was taken without trawling through commit history or code
comments.

### Location

    project_root/ai/agentic-pipeline/turns/current turn directory/adr.md


### When the Agent Must Create an ADR

| Scenario                                                     | Example                                                                                                                                                                                                                                                                | Required? |
|--------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------|
| Summarize Chain of Thought reasoning for the task            | Documenting the decision flow: ① capture requirements for a low-latency, pay-per-request CRUD API → ② compare DynamoDB single-table vs. Aurora Serverless → ③ choose DynamoDB single-table with GSI on email for predictable access patterns and minimal ops overhead. | **Yes**   |
| Selecting one library or pattern over plausible alternatives | Choosing Prisma instead of TypeORM                                                                                                                                                                                                                                     | **Yes**   |
| Introducing a new directory or module layout                 | Splitting `customer` domain into bounded contexts                                                                                                                                                                                                                      | **Yes**   |
| Changing a cross-cutting concern                             | Switching error-handling strategy to functional `Result` types                                                                                                                                                                                                         | **Yes**   |
| Cosmetic or trivial change                                   | Renaming a variable                                                                                                                                                                                                                                                    | **Yes**   |


### Minimal ADR Template

```markdown
# {{ADR Title}}

**Status**: Proposed | Accepted | Deprecated

**Date**: {{YYYY-MM-DD}}

**Context**  
Briefly explain the problem or decision context.

**Decision**  
State the choice that was made.

**Consequences**  
List the trade-offs and implications (positive and negative).  
```



```

