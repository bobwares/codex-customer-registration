-- App: Customer Registration
-- Package: db.scripts
-- File: customer_profile_test_data.sql
-- Version: 0.1.0
-- Turns: 1
-- Author: Bobwares (bobwares@outlook.com)
-- Date: 2025-10-26T00:40:26Z
-- Exports: Seed data for postal_address, privacy_settings, customer, customer_email, customer_phone_number
-- Description: Inserts 20 customer records with related emails and phone numbers for testing.

BEGIN;

INSERT INTO postal_address (id, line1, line2, city, state, postal_code, country) VALUES
    (1, '101 Market St', NULL, 'Springfield', 'IL', '62701', 'US'),
    (2, '202 Oak Ave', 'Apt 2', 'Madison', 'WI', '53703', 'US'),
    (3, '303 Pine Rd', NULL, 'Austin', 'TX', '73301', 'US'),
    (4, '404 Maple Ln', NULL, 'Denver', 'CO', '80014', 'US'),
    (5, '505 Cedar Blvd', 'Suite 5', 'Phoenix', 'AZ', '85001', 'US'),
    (6, '606 Birch Way', NULL, 'Portland', 'OR', '97035', 'US'),
    (7, '707 Walnut St', NULL, 'Boston', 'MA', '02108', 'US'),
    (8, '808 Chestnut Dr', NULL, 'Seattle', 'WA', '98101', 'US'),
    (9, '909 Elm Cir', NULL, 'Atlanta', 'GA', '30303', 'US'),
    (10, '111 Ash Pl', NULL, 'Miami', 'FL', '33101', 'US'),
    (11, '121 River Rd', NULL, 'Raleigh', 'NC', '27601', 'US'),
    (12, '131 Lake View', 'Unit 4', 'Boise', 'ID', '83702', 'US'),
    (13, '141 Hill St', NULL, 'Nashville', 'TN', '37201', 'US'),
    (14, '151 Grove Ave', NULL, 'Chicago', 'IL', '60601', 'US'),
    (15, '161 Valley Dr', NULL, 'Salt Lake City', 'UT', '84101', 'US'),
    (16, '171 Harbor Way', NULL, 'San Diego', 'CA', '92101', 'US'),
    (17, '181 Sunset Blvd', NULL, 'Los Angeles', 'CA', '90012', 'US'),
    (18, '191 Sunrise Ct', NULL, 'Tampa', 'FL', '33602', 'US'),
    (19, '201 Boulder St', NULL, 'Boulder', 'CO', '80302', 'US'),
    (20, '211 Coral Ave', NULL, 'Honolulu', 'HI', '96813', 'US')
ON CONFLICT (id) DO NOTHING;

INSERT INTO privacy_settings (id, marketing_emails_enabled, two_factor_enabled) VALUES
    (1, TRUE, FALSE),
    (2, FALSE, TRUE),
    (3, TRUE, TRUE),
    (4, FALSE, FALSE),
    (5, TRUE, FALSE),
    (6, FALSE, TRUE),
    (7, TRUE, TRUE),
    (8, FALSE, FALSE),
    (9, TRUE, FALSE),
    (10, FALSE, TRUE),
    (11, TRUE, TRUE),
    (12, FALSE, FALSE),
    (13, TRUE, FALSE),
    (14, FALSE, TRUE),
    (15, TRUE, TRUE),
    (16, FALSE, FALSE),
    (17, TRUE, FALSE),
    (18, FALSE, TRUE),
    (19, TRUE, TRUE),
    (20, FALSE, FALSE)
ON CONFLICT (id) DO NOTHING;

INSERT INTO customer (id, first_name, middle_name, last_name, address_id, privacy_settings_id)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Alice', NULL, 'Smith', 1, 1),
    ('22222222-2222-2222-2222-222222222222', 'Bob', 'J', 'Jones', 2, 2),
    ('33333333-3333-3333-3333-333333333333', 'Charlie', NULL, 'Brown', 3, 3),
    ('44444444-4444-4444-4444-444444444444', 'Diana', 'K', 'Miller', 4, 4),
    ('55555555-5555-5555-5555-555555555555', 'Ethan', NULL, 'Davis', 5, 5),
    ('66666666-6666-6666-6666-666666666666', 'Fiona', NULL, 'Wilson', 6, 6),
    ('77777777-7777-7777-7777-777777777777', 'Grace', 'L', 'Taylor', 7, 7),
    ('88888888-8888-8888-8888-888888888888', 'Henry', NULL, 'Anderson', 8, 8),
    ('99999999-9999-9999-9999-999999999999', 'Isabel', NULL, 'Thomas', 9, 9),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Jack', 'M', 'Jackson', 10, 10),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Karen', NULL, 'White', 11, 11),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Liam', NULL, 'Harris', 12, 12),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Mia', 'N', 'Martin', 13, 13),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Noah', NULL, 'Lee', 14, 14),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'Olivia', NULL, 'Perez', 15, 15),
    ('11111111-2222-3333-4444-555555555555', 'Paul', NULL, 'Garcia', 16, 16),
    ('22222222-3333-4444-5555-666666666666', 'Quinn', NULL, 'Rodriguez', 17, 17),
    ('33333333-4444-5555-6666-777777777777', 'Riley', NULL, 'Lopez', 18, 18),
    ('44444444-5555-6666-7777-888888888888', 'Sophie', NULL, 'Hill', 19, 19),
    ('55555555-6666-7777-8888-999999999999', 'Trevor', NULL, 'Young', 20, 20)
ON CONFLICT (id) DO NOTHING;

INSERT INTO customer_email (customer_id, email)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'alice.smith@example.com'),
    ('22222222-2222-2222-2222-222222222222', 'bob.jones@example.com'),
    ('33333333-3333-3333-3333-333333333333', 'charlie.brown@example.com'),
    ('44444444-4444-4444-4444-444444444444', 'diana.miller@example.com'),
    ('55555555-5555-5555-5555-555555555555', 'ethan.davis@example.com'),
    ('66666666-6666-6666-6666-666666666666', 'fiona.wilson@example.com'),
    ('77777777-7777-7777-7777-777777777777', 'grace.taylor@example.com'),
    ('88888888-8888-8888-8888-888888888888', 'henry.anderson@example.com'),
    ('99999999-9999-9999-9999-999999999999', 'isabel.thomas@example.com'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'jack.jackson@example.com'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'karen.white@example.com'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'liam.harris@example.com'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'mia.martin@example.com'),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'noah.lee@example.com'),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'olivia.perez@example.com'),
    ('11111111-2222-3333-4444-555555555555', 'paul.garcia@example.com'),
    ('22222222-3333-4444-5555-666666666666', 'quinn.rodriguez@example.com'),
    ('33333333-4444-5555-6666-777777777777', 'riley.lopez@example.com'),
    ('44444444-5555-6666-7777-888888888888', 'sophie.hill@example.com'),
    ('55555555-6666-7777-8888-999999999999', 'trevor.young@example.com')
ON CONFLICT DO NOTHING;

INSERT INTO customer_phone_number (customer_id, phone_type, phone_number)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'mobile', '+12175550101'),
    ('22222222-2222-2222-2222-222222222222', 'mobile', '+16085550202'),
    ('33333333-3333-3333-3333-333333333333', 'home', '+15125550303'),
    ('44444444-4444-4444-4444-444444444444', 'work', '+13035550404'),
    ('55555555-5555-5555-5555-555555555555', 'mobile', '+16025550505'),
    ('66666666-6666-6666-6666-666666666666', 'mobile', '+15035550606'),
    ('77777777-7777-7777-7777-777777777777', 'home', '+16175550707'),
    ('88888888-8888-8888-8888-888888888888', 'mobile', '+12065550808'),
    ('99999999-9999-9999-9999-999999999999', 'mobile', '+14045550909'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'work', '+13055551010'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'mobile', '+19195551111'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'mobile', '+12085551212'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'mobile', '+16155551313'),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'mobile', '+13125551414'),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'mobile', '+18015551515'),
    ('11111111-2222-3333-4444-555555555555', 'mobile', '+16195551616'),
    ('22222222-3333-4444-5555-666666666666', 'mobile', '+13195551717'),
    ('33333333-4444-5555-6666-777777777777', 'mobile', '+18135551818'),
    ('44444444-5555-6666-7777-888888888888', 'mobile', '+17205551919'),
    ('55555555-6666-7777-8888-999999999999', 'mobile', '+18085552020')
ON CONFLICT DO NOTHING;

COMMIT;

-- Smoke test
SELECT COUNT(*) AS customer_count FROM customer;
