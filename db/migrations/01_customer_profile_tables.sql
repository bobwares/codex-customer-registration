/**
 * App: Customer Registration
 * Package: db.migrations
 * File: 01_customer_profile_tables.sql
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Agent
 * Date: 2025-10-29T22:24:49Z
 * Exports: customer_profile_tables
 * Description: Creates normalized PostgreSQL tables, constraints, and indexes for customer profiles.
 */
BEGIN;

CREATE SCHEMA IF NOT EXISTS customer_registration;
SET search_path TO customer_registration, public;

CREATE TABLE IF NOT EXISTS privacy_settings (
    privacy_settings_id BIGSERIAL PRIMARY KEY,
    marketing_emails_enabled BOOLEAN NOT NULL,
    two_factor_enabled BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS customer_address (
    address_id BIGSERIAL PRIMARY KEY,
    line1 VARCHAR(255) NOT NULL,
    line2 VARCHAR(255),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20),
    country CHAR(2) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS customer (
    customer_id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255),
    last_name VARCHAR(255) NOT NULL,
    privacy_settings_id BIGINT NOT NULL REFERENCES privacy_settings(privacy_settings_id),
    address_id BIGINT REFERENCES customer_address(address_id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_customer_address UNIQUE (address_id)
);

CREATE TABLE IF NOT EXISTS customer_email (
    email_id BIGSERIAL PRIMARY KEY,
    customer_id UUID NOT NULL REFERENCES customer(customer_id) ON DELETE CASCADE,
    email VARCHAR(320) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_customer_email UNIQUE (customer_id, email)
);

CREATE INDEX IF NOT EXISTS idx_customer_email_customer_id
    ON customer_email (customer_id);
CREATE UNIQUE INDEX IF NOT EXISTS uq_customer_email_address
    ON customer_email (email);

CREATE TABLE IF NOT EXISTS customer_phone_number (
    phone_id BIGSERIAL PRIMARY KEY,
    customer_id UUID NOT NULL REFERENCES customer(customer_id) ON DELETE CASCADE,
    type VARCHAR(20) NOT NULL,
    number VARCHAR(32) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_customer_phone UNIQUE (customer_id, number)
);

CREATE INDEX IF NOT EXISTS idx_customer_phone_customer_id
    ON customer_phone_number (customer_id);

CREATE OR REPLACE VIEW customer_profile_overview AS
SELECT
    c.customer_id,
    c.first_name,
    c.middle_name,
    c.last_name,
    ps.marketing_emails_enabled,
    ps.two_factor_enabled,
    ca.line1,
    ca.line2,
    ca.city,
    ca.state,
    ca.postal_code,
    ca.country
FROM customer c
         JOIN privacy_settings ps ON ps.privacy_settings_id = c.privacy_settings_id
         LEFT JOIN customer_address ca ON ca.address_id = c.address_id;

COMMIT;
