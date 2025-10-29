-- App: Customer Registration
-- Package: db.changelog.sql
-- File: 01_customer_profile_tables.sql
-- Version: 0.1.0
-- Turns: 1
-- Author: AI Coding Agent
-- Date: 2025-10-29T16:56:41Z
-- Exports: customer domain tables
-- Description: Liquibase SQL changeset mirroring normalized customer table definitions.

--liquibase formatted sql
--changeset ai-agent:01 runOnChange:true context:default
CREATE SCHEMA IF NOT EXISTS customer_registration;
SET search_path TO customer_registration, public;

CREATE TABLE IF NOT EXISTS postal_address (
  address_id SERIAL PRIMARY KEY,
  line1 VARCHAR(255) NOT NULL,
  line2 VARCHAR(255),
  city VARCHAR(100) NOT NULL,
  state VARCHAR(50) NOT NULL,
  postal_code VARCHAR(20),
  country CHAR(2) NOT NULL
);

CREATE TABLE IF NOT EXISTS privacy_settings (
  privacy_settings_id SERIAL PRIMARY KEY,
  marketing_emails_enabled BOOLEAN NOT NULL,
  two_factor_enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS customer (
  customer_id UUID PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  middle_name VARCHAR(255),
  last_name VARCHAR(255) NOT NULL,
  address_id INTEGER REFERENCES postal_address(address_id),
  privacy_settings_id INTEGER REFERENCES privacy_settings(privacy_settings_id)
);

CREATE INDEX IF NOT EXISTS idx_customer_address_id ON customer(address_id);
CREATE INDEX IF NOT EXISTS idx_customer_privacy_settings_id ON customer(privacy_settings_id);

CREATE TABLE IF NOT EXISTS customer_email (
  email_id SERIAL PRIMARY KEY,
  customer_id UUID NOT NULL REFERENCES customer(customer_id) ON DELETE CASCADE,
  email VARCHAR(320) NOT NULL,
  UNIQUE (customer_id, email)
);

CREATE INDEX IF NOT EXISTS idx_customer_email_customer_id ON customer_email(customer_id);
CREATE UNIQUE INDEX IF NOT EXISTS idx_customer_email_unique_email ON customer_email(email);

CREATE TABLE IF NOT EXISTS customer_phone_number (
  phone_id SERIAL PRIMARY KEY,
  customer_id UUID NOT NULL REFERENCES customer(customer_id) ON DELETE CASCADE,
  type VARCHAR(20) NOT NULL,
  number VARCHAR(15) NOT NULL,
  UNIQUE (customer_id, number)
);

CREATE INDEX IF NOT EXISTS idx_customer_phone_number_customer_id ON customer_phone_number(customer_id);
CREATE INDEX IF NOT EXISTS idx_customer_phone_number_type ON customer_phone_number(type);

CREATE OR REPLACE VIEW customer_overview AS
SELECT
  c.customer_id,
  c.first_name,
  c.middle_name,
  c.last_name,
  pa.line1,
  pa.line2,
  pa.city,
  pa.state,
  pa.postal_code,
  pa.country,
  ps.marketing_emails_enabled,
  ps.two_factor_enabled
FROM customer c
LEFT JOIN postal_address pa ON c.address_id = pa.address_id
LEFT JOIN privacy_settings ps ON c.privacy_settings_id = ps.privacy_settings_id;
