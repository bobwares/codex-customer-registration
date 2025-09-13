-- /**
-- * App: Customer Registration
-- * Package: db.migration
-- * File: V1__init.sql
-- * Version: 0.1.0
-- * Turns: 1
-- * Author: codex
-- * Date: 2025-09-13T02:16:18Z
-- * Exports: initial schema
-- * Description: Creates tables for customer registration domain.
-- */
CREATE TABLE customers (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255),
    last_name VARCHAR(255) NOT NULL,
    line1 VARCHAR(255),
    line2 VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    postal_code VARCHAR(20),
    country VARCHAR(2),
    marketing_emails_enabled BOOLEAN NOT NULL,
    two_factor_enabled BOOLEAN NOT NULL
);

CREATE TABLE customer_emails (
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    email VARCHAR(255) NOT NULL,
    PRIMARY KEY (customer_id, email)
);

CREATE TABLE customer_phone_numbers (
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL,
    number VARCHAR(20) NOT NULL,
    PRIMARY KEY (customer_id, number)
);
