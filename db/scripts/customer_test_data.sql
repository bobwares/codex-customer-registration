-- App: Customer Registration
-- Package: db.scripts
-- File: customer_test_data.sql
-- Version: 0.1.0
-- Turns: 1
-- Author: Codex Agent
-- Date: 2025-09-14T03:38:27Z
-- Exports: none
-- Description: Inserts sample customer domain data for testing.

BEGIN;

-- Insert postal addresses
INSERT INTO postal_address (address_id, line1, line2, city, state, postal_code, country) VALUES
    (1, '100 Market St', NULL, 'Springfield', 'IL', '62701', 'US'),
    (2, '200 Oak Ave', 'Apt 2', 'Madison', 'WI', '53703', 'US'),
    (3, '300 Pine Rd', NULL, 'Austin', 'TX', '73301', 'US'),
    (4, '400 Maple Ln', NULL, 'Denver', 'CO', '80014', 'US'),
    (5, '500 Cedar Blvd', 'Suite 5', 'Phoenix', 'AZ', '85001', 'US'),
    (6, '600 Birch Way', NULL, 'Portland', 'OR', '97035', 'US'),
    (7, '700 Walnut St', NULL, 'Boston', 'MA', '02108', 'US'),
    (8, '800 Chestnut Dr', NULL, 'Seattle', 'WA', '98101', 'US'),
    (9, '900 Elm Cir', NULL, 'Atlanta', 'GA', '30303', 'US'),
    (10, '1000 Ash Pl', NULL, 'Miami', 'FL', '33101', 'US'),
    (11, '1100 Fir St', NULL, 'Dallas', 'TX', '75201', 'US'),
    (12, '1200 Palm Ave', NULL, 'Orlando', 'FL', '32801', 'US'),
    (13, '1300 Vine Rd', NULL, 'Nashville', 'TN', '37201', 'US'),
    (14, '1400 Ivy Ln', NULL, 'Columbus', 'OH', '43004', 'US'),
    (15, '1500 Poplar Dr', NULL, 'Charlotte', 'NC', '28202', 'US'),
    (16, '1600 Spruce Ct', NULL, 'Sacramento', 'CA', '94203', 'US'),
    (17, '1700 Willow Way', NULL, 'Boise', 'ID', '83702', 'US'),
    (18, '1800 Cypress Blvd', NULL, 'Tampa', 'FL', '33602', 'US'),
    (19, '1900 Dogwood Rd', NULL, 'Salt Lake City', 'UT', '84101', 'US'),
    (20, '2000 Redwood St', NULL, 'San Diego', 'CA', '92101', 'US')
ON CONFLICT DO NOTHING;

-- Insert privacy settings
INSERT INTO privacy_settings (privacy_settings_id, marketing_emails_enabled, two_factor_enabled) VALUES
    (1, TRUE, FALSE), (2, FALSE, TRUE), (3, TRUE, TRUE), (4, FALSE, FALSE), (5, TRUE, FALSE),
    (6, FALSE, TRUE), (7, TRUE, TRUE), (8, FALSE, FALSE), (9, TRUE, FALSE), (10, FALSE, TRUE),
    (11, TRUE, TRUE), (12, FALSE, FALSE), (13, TRUE, FALSE), (14, FALSE, TRUE), (15, TRUE, TRUE),
    (16, FALSE, FALSE), (17, TRUE, FALSE), (18, FALSE, TRUE), (19, TRUE, TRUE), (20, FALSE, FALSE)
ON CONFLICT DO NOTHING;

-- Insert customers
INSERT INTO customer (customer_id, first_name, middle_name, last_name, address_id, privacy_settings_id) VALUES
    ('11111111-1111-1111-1111-111111111111', 'Alice', NULL, 'Smith', 1, 1),
    ('22222222-2222-2222-2222-222222222222', 'Bob', 'J', 'Jones', 2, 2),
    ('33333333-3333-3333-3333-333333333333', 'Charlie', NULL, 'Brown', 3, 3),
    ('44444444-4444-4444-4444-444444444444', 'David', 'K', 'Miller', 4, 4),
    ('55555555-5555-5555-5555-555555555555', 'Emma', NULL, 'Davis', 5, 5),
    ('66666666-6666-6666-6666-666666666666', 'Frank', NULL, 'Wilson', 6, 6),
    ('77777777-7777-7777-7777-777777777777', 'Grace', 'L', 'Taylor', 7, 7),
    ('88888888-8888-8888-8888-888888888888', 'Hugo', NULL, 'Anderson', 8, 8),
    ('99999999-9999-9999-9999-999999999999', 'Isabel', NULL, 'Thomas', 9, 9),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Jack', 'M', 'Jackson', 10, 10),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Karen', NULL, 'White', 11, 11),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Liam', NULL, 'Harris', 12, 12),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Mia', NULL, 'Martin', 13, 13),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Noah', 'N', 'Lee', 14, 14),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'Olivia', NULL, 'Walker', 15, 15),
    ('11111111-2222-3333-4444-555555555555', 'Paul', NULL, 'Young', 16, 16),
    ('22222222-3333-4444-5555-666666666666', 'Quinn', NULL, 'King', 17, 17),
    ('33333333-4444-5555-6666-777777777777', 'Ruby', NULL, 'Scott', 18, 18),
    ('44444444-5555-6666-7777-888888888888', 'Sam', NULL, 'Green', 19, 19),
    ('55555555-6666-7777-8888-999999999999', 'Tina', NULL, 'Hall', 20, 20)
ON CONFLICT DO NOTHING;

-- Insert customer emails
INSERT INTO customer_email (customer_id, email) VALUES
    ('11111111-1111-1111-1111-111111111111', 'alice@example.com'),
    ('22222222-2222-2222-2222-222222222222', 'bob@example.com'),
    ('33333333-3333-3333-3333-333333333333', 'charlie@example.com'),
    ('44444444-4444-4444-4444-444444444444', 'david@example.com'),
    ('55555555-5555-5555-5555-555555555555', 'emma@example.com'),
    ('66666666-6666-6666-6666-666666666666', 'frank@example.com'),
    ('77777777-7777-7777-7777-777777777777', 'grace@example.com'),
    ('88888888-8888-8888-8888-888888888888', 'hugo@example.com'),
    ('99999999-9999-9999-9999-999999999999', 'isabel@example.com'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'jack@example.com'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'karen@example.com'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'liam@example.com'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'mia@example.com'),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'noah@example.com'),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'olivia@example.com'),
    ('11111111-2222-3333-4444-555555555555', 'paul@example.com'),
    ('22222222-3333-4444-5555-666666666666', 'quinn@example.com'),
    ('33333333-4444-5555-6666-777777777777', 'ruby@example.com'),
    ('44444444-5555-6666-7777-888888888888', 'sam@example.com'),
    ('55555555-6666-7777-8888-999999999999', 'tina@example.com')
ON CONFLICT DO NOTHING;

-- Insert phone numbers
INSERT INTO customer_phone_number (customer_id, type, number) VALUES
    ('11111111-1111-1111-1111-111111111111', 'mobile', '+15555550101'),
    ('22222222-2222-2222-2222-222222222222', 'mobile', '+15555550102'),
    ('33333333-3333-3333-3333-333333333333', 'mobile', '+15555550103'),
    ('44444444-4444-4444-4444-444444444444', 'mobile', '+15555550104'),
    ('55555555-5555-5555-5555-555555555555', 'mobile', '+15555550105'),
    ('66666666-6666-6666-6666-666666666666', 'mobile', '+15555550106'),
    ('77777777-7777-7777-7777-777777777777', 'mobile', '+15555550107'),
    ('88888888-8888-8888-8888-888888888888', 'mobile', '+15555550108'),
    ('99999999-9999-9999-9999-999999999999', 'mobile', '+15555550109'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'mobile', '+15555550110'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'mobile', '+15555550111'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'mobile', '+15555550112'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'mobile', '+15555550113'),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'mobile', '+15555550114'),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'mobile', '+15555550115'),
    ('11111111-2222-3333-4444-555555555555', 'mobile', '+15555550116'),
    ('22222222-3333-4444-5555-666666666666', 'mobile', '+15555550117'),
    ('33333333-4444-5555-6666-777777777777', 'mobile', '+15555550118'),
    ('44444444-5555-6666-7777-888888888888', 'mobile', '+15555550119'),
    ('55555555-6666-7777-8888-999999999999', 'mobile', '+15555550120')
ON CONFLICT DO NOTHING;

-- Smoke test query
SELECT COUNT(*) FROM customer;

COMMIT;
