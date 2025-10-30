-- App: Customer Registration
-- Package: com.bobwares.customer.registration
-- File: 001-create-customer-schema.sql
-- Version: 0.1.0
-- Turns: Turn 1
-- Author: Bobwares
-- Date: 2025-10-30T06:53:03Z
-- Exports: CustomerSchemaInitialization
-- Description: Bootstraps the PostgreSQL schema to mirror the Liquibase-managed customer registration tables.
\i ../src/main/resources/db/changelog/sql/create-customer-tables.sql
