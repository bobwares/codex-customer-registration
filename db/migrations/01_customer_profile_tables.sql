/**
 * App: Customer Registration
 * Package: db.migrations
 * File: 01_customer_profile_tables.sql
 * Version: 0.1.0
 * Turns: 1
 * Author: AI Coding Agent
 * Date: 2025-10-23T22:12:31Z
 * Exports: customer_profile schema, postal_address, privacy_settings, customer, customer_email, customer_phone_number, customer_overview view
 * Description: Creates normalized PostgreSQL schema objects for customer profile data derived from the JSON schema.
 */

BEGIN;

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE SCHEMA IF NOT EXISTS customer_profile;

SET search_path TO customer_profile, public;

CREATE TABLE IF NOT EXISTS postal_address (
  address_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  line1 VARCHAR(255) NOT NULL,
  line2 VARCHAR(255),
  city VARCHAR(100) NOT NULL,
  state VARCHAR(100) NOT NULL,
  postal_code VARCHAR(20),
  country CHAR(2) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE OR REPLACE FUNCTION customer_profile.touch_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_postal_address_updated_at ON postal_address;
CREATE TRIGGER trg_postal_address_updated_at
BEFORE UPDATE ON postal_address
FOR EACH ROW
EXECUTE FUNCTION customer_profile.touch_updated_at();

CREATE TABLE IF NOT EXISTS privacy_settings (
  privacy_settings_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  marketing_emails_enabled BOOLEAN NOT NULL,
  two_factor_enabled BOOLEAN NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

DROP TRIGGER IF EXISTS trg_privacy_settings_updated_at ON privacy_settings;
CREATE TRIGGER trg_privacy_settings_updated_at
BEFORE UPDATE ON privacy_settings
FOR EACH ROW
EXECUTE FUNCTION customer_profile.touch_updated_at();

CREATE TABLE IF NOT EXISTS customer (
  customer_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  first_name VARCHAR(255) NOT NULL,
  middle_name VARCHAR(255),
  last_name VARCHAR(255) NOT NULL,
  address_id UUID REFERENCES postal_address(address_id) ON DELETE SET NULL,
  privacy_settings_id UUID REFERENCES privacy_settings(privacy_settings_id) ON DELETE SET NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_customer_name
  ON customer (LOWER(first_name), LOWER(last_name));

DROP TRIGGER IF EXISTS trg_customer_updated_at ON customer;
CREATE TRIGGER trg_customer_updated_at
BEFORE UPDATE ON customer
FOR EACH ROW
EXECUTE FUNCTION customer_profile.touch_updated_at();

CREATE TABLE IF NOT EXISTS customer_email (
  customer_email_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  customer_id UUID NOT NULL REFERENCES customer(customer_id) ON DELETE CASCADE,
  email VARCHAR(320) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT chk_customer_email_format CHECK (email ~* '^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$'),
  CONSTRAINT ux_customer_email UNIQUE (customer_id, email)
);

CREATE INDEX IF NOT EXISTS idx_customer_email_customer_id
  ON customer_email (customer_id);

DROP TRIGGER IF EXISTS trg_customer_email_updated_at ON customer_email;
CREATE TRIGGER trg_customer_email_updated_at
BEFORE UPDATE ON customer_email
FOR EACH ROW
EXECUTE FUNCTION customer_profile.touch_updated_at();

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type t JOIN pg_namespace n ON n.oid = t.typnamespace WHERE t.typname = 'phone_type' AND n.nspname = 'customer_profile') THEN
    CREATE TYPE customer_profile.phone_type AS ENUM ('mobile', 'home', 'work', 'other');
  END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TABLE IF NOT EXISTS customer_phone_number (
  customer_phone_number_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  customer_id UUID NOT NULL REFERENCES customer(customer_id) ON DELETE CASCADE,
  type customer_profile.phone_type NOT NULL,
  number VARCHAR(15) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT chk_phone_number_format CHECK (number ~ '^\+?[1-9][0-9]{1,14}$'),
  CONSTRAINT ux_customer_phone UNIQUE (customer_id, number)
);

CREATE INDEX IF NOT EXISTS idx_customer_phone_customer_id
  ON customer_phone_number (customer_id);

DROP TRIGGER IF EXISTS trg_customer_phone_updated_at ON customer_phone_number;
CREATE TRIGGER trg_customer_phone_updated_at
BEFORE UPDATE ON customer_phone_number
FOR EACH ROW
EXECUTE FUNCTION customer_profile.touch_updated_at();

CREATE OR REPLACE VIEW customer_overview AS
SELECT
  c.customer_id,
  c.first_name,
  c.middle_name,
  c.last_name,
  c.created_at,
  c.updated_at,
  pa.line1,
  pa.line2,
  pa.city,
  pa.state,
  pa.postal_code,
  pa.country,
  ps.marketing_emails_enabled,
  ps.two_factor_enabled,
  ARRAY(SELECT ce.email FROM customer_profile.customer_email ce WHERE ce.customer_id = c.customer_id ORDER BY ce.email) AS emails,
  ARRAY(SELECT jsonb_build_object('type', cpn.type, 'number', cpn.number) FROM customer_profile.customer_phone_number cpn WHERE cpn.customer_id = c.customer_id ORDER BY cpn.number) AS phone_numbers
FROM customer c
LEFT JOIN postal_address pa ON pa.address_id = c.address_id
LEFT JOIN privacy_settings ps ON ps.privacy_settings_id = c.privacy_settings_id;

COMMIT;
