-- App: Customer Registration
-- Package: db.migrations
-- File: 01_customer_profile_tables.sql
-- Version: 0.1.0
-- Turns: 1
-- Author: Bobwares (bobwares@outlook.com)
-- Date: 2025-10-26T00:40:26Z
-- Exports: Tables customer, postal_address, privacy_settings, customer_email, customer_phone_number
-- Description: Creates normalized PostgreSQL tables for the customer registration domain.

BEGIN;

CREATE TABLE IF NOT EXISTS postal_address (
    id BIGSERIAL PRIMARY KEY,
    line1 VARCHAR(255) NOT NULL,
    line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country CHAR(2) NOT NULL
);

CREATE TABLE IF NOT EXISTS privacy_settings (
    id BIGSERIAL PRIMARY KEY,
    marketing_emails_enabled BOOLEAN NOT NULL,
    two_factor_enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS customer (
    id UUID PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    address_id BIGINT REFERENCES postal_address (id),
    privacy_settings_id BIGINT NOT NULL REFERENCES privacy_settings (id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_customer_address_id ON customer (address_id);
CREATE INDEX IF NOT EXISTS idx_customer_privacy_settings_id ON customer (privacy_settings_id);

CREATE TABLE IF NOT EXISTS customer_email (
    id BIGSERIAL PRIMARY KEY,
    customer_id UUID NOT NULL REFERENCES customer (id) ON DELETE CASCADE,
    email VARCHAR(320) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UNIQUE (customer_id, email)
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_customer_email_lower ON customer_email (LOWER(email));
CREATE INDEX IF NOT EXISTS idx_customer_email_customer_id ON customer_email (customer_id);

CREATE TABLE IF NOT EXISTS customer_phone_number (
    id BIGSERIAL PRIMARY KEY,
    customer_id UUID NOT NULL REFERENCES customer (id) ON DELETE CASCADE,
    phone_type VARCHAR(20) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UNIQUE (customer_id, phone_number),
    CHECK (phone_type IN ('mobile', 'home', 'work', 'other'))
);

CREATE INDEX IF NOT EXISTS idx_customer_phone_customer_id ON customer_phone_number (customer_id);

COMMIT;
