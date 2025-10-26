# ADR: Customer Registration Service Foundation

## Status
Accepted

## Context
The project requires a foundational Spring Boot service that manages customer registrations end-to-end, including persistence, validation, and REST APIs. We need an opinionated baseline that integrates with PostgreSQL and supports future enhancements.

## Decision
Implemented a Spring Boot application using MVC + JPA with PostgreSQL, leveraging Liquibase for schema management. Defined domain models for customers, email, phone numbers, postal addresses, and privacy settings. Exposed CRUD APIs through REST controllers and integrated validation and exception handling.

## Consequences
- Provides a structured and test-covered baseline for customer onboarding.
- Enables consistent schema management and data seeding for local development.
- Establishes clear DTO mapping and service boundaries for future features.
