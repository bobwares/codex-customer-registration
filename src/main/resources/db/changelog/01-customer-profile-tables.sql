/**
 * App: Customer Registration
 * Package: db.changelog
 * File: 01-customer-profile-tables.sql
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Agent
 * Date: 2025-10-29T22:24:49Z
 * Exports: customer-profile-liquibase
 * Description: Liquibase formatted SQL mirroring the normalized customer schema.
 */
--liquibase formatted sql
--changeset ai-agent:customer-profile
CREATE SCHEMA IF NOT EXISTS customer_registration;

CREATE TABLE IF NOT EXISTS customer_registration.privacy_settings (
    privacy_settings_id BIGSERIAL PRIMARY KEY,
    marketing_emails_enabled BOOLEAN NOT NULL,
    two_factor_enabled BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS customer_registration.customer_address (
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

CREATE TABLE IF NOT EXISTS customer_registration.customer (
    customer_id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255),
    last_name VARCHAR(255) NOT NULL,
    privacy_settings_id BIGINT NOT NULL REFERENCES customer_registration.privacy_settings(privacy_settings_id),
    address_id BIGINT REFERENCES customer_registration.customer_address(address_id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_customer_address UNIQUE (address_id)
);

CREATE TABLE IF NOT EXISTS customer_registration.customer_email (
    email_id BIGSERIAL PRIMARY KEY,
    customer_id UUID NOT NULL REFERENCES customer_registration.customer(customer_id) ON DELETE CASCADE,
    email VARCHAR(320) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_customer_email UNIQUE (customer_id, email)
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_customer_email_address
    ON customer_registration.customer_email (email);

CREATE TABLE IF NOT EXISTS customer_registration.customer_phone_number (
    phone_id BIGSERIAL PRIMARY KEY,
    customer_id UUID NOT NULL REFERENCES customer_registration.customer(customer_id) ON DELETE CASCADE,
    type VARCHAR(20) NOT NULL,
    number VARCHAR(32) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_customer_phone UNIQUE (customer_id, number)
);

CREATE INDEX IF NOT EXISTS idx_customer_email_customer_id
    ON customer_registration.customer_email (customer_id);

CREATE INDEX IF NOT EXISTS idx_customer_phone_customer_id
    ON customer_registration.customer_phone_number (customer_id);

--rollback DROP SCHEMA IF EXISTS customer_registration CASCADE;
