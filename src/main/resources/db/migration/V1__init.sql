-- App: Customer Registration
-- Package: com.bobwares.customer.registration
-- File: V1__init.sql
-- Version: 0.1.0
-- Turns: 1
-- Author: Bobwares
-- Date: 2025-09-12T19:54:27Z
-- Exports: database schema
-- Description: Initializes customer table.

CREATE TABLE customers (
  id UUID PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  middle_name VARCHAR(100),
  last_name VARCHAR(100) NOT NULL,
  email VARCHAR(255) NOT NULL
);
