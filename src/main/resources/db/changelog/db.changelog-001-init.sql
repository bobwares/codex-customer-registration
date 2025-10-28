-- App: Customer Registration
-- Package: resources.db.changelog
-- File: db.changelog-001-init.sql
-- Version: 0.1.0
-- Turns: 1
-- Author: AI
-- Date: 2025-10-28T15:32:47Z
-- Exports: Liquibase formatted SQL change sets
-- Description: Establishes normalized customer profile tables, including emails, phone numbers, and privacy preferences.
--liquibase formatted sql

--changeset ai:customer-table
CREATE TABLE IF NOT EXISTS customers (
    id UUID PRIMARY KEY,
    first_name VARCHAR(64) NOT NULL,
    middle_name VARCHAR(64),
    last_name VARCHAR(64) NOT NULL,
    address_line1 VARCHAR(128) NOT NULL,
    address_line2 VARCHAR(128),
    address_city VARCHAR(64) NOT NULL,
    address_state VARCHAR(64) NOT NULL,
    address_postal_code VARCHAR(32) NOT NULL,
    address_country CHAR(2) NOT NULL,
    privacy_marketing_emails_enabled BOOLEAN NOT NULL,
    privacy_two_factor_enabled BOOLEAN NOT NULL
);

--changeset ai:customer-emails-table
CREATE TABLE IF NOT EXISTS customer_emails (
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    email VARCHAR(254) NOT NULL,
    PRIMARY KEY (customer_id, email)
);

--changeset ai:customer-phone-table
CREATE TABLE IF NOT EXISTS customer_phone_numbers (
    customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    phone_type VARCHAR(16) NOT NULL,
    phone_number VARCHAR(32) NOT NULL,
    PRIMARY KEY (customer_id, phone_type, phone_number)
);

--changeset ai:customer-email-unique
CREATE UNIQUE INDEX IF NOT EXISTS ux_customer_emails_email ON customer_emails (email);

--changeset ai:customer-phone-number-unique
CREATE UNIQUE INDEX IF NOT EXISTS ux_customer_phone_numbers_number ON customer_phone_numbers (phone_number);
