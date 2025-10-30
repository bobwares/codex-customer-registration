-- App: Customer Registration
-- Package: com.bobwares.customer.registration
-- File: create-customer-tables.sql
-- Version: 0.1.0
-- Turns: Turn 1
-- Author: Bobwares
-- Date: 2025-10-30T06:53:03Z
-- Exports: CustomerTablesDDL
-- Description: Creates normalized tables for customers, emails, phone numbers, and embedded settings.
CREATE TABLE IF NOT EXISTS customers (
    id UUID PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    address_city VARCHAR(100) NOT NULL,
    address_state VARCHAR(100) NOT NULL,
    address_postal_code VARCHAR(20) NOT NULL,
    address_country CHAR(2) NOT NULL,
    marketing_emails_enabled BOOLEAN NOT NULL,
    two_factor_enabled BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS customer_emails (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS customer_phone_numbers (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    phone_type VARCHAR(16) NOT NULL,
    phone_number VARCHAR(32) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_customer_emails_customer ON customer_emails(customer_id);
CREATE INDEX IF NOT EXISTS idx_customer_phone_numbers_customer ON customer_phone_numbers(customer_id);
