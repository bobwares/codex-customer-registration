-- App: Customer Registration
-- Package: db
-- File: customer_profile_test_data.sql
-- Version: 0.1.0
-- Turns: 1
-- Author: AI Agent
-- Date: 2025-10-25T09:49:02Z
-- Exports: Test data seed statements
-- Description: Seeds sample customer registration records for local testing and smoke verification.

BEGIN;

SET search_path TO customer_registration, public;

INSERT INTO postal_address (address_id, line1, line2, city, state, postal_code, country)
VALUES
    (1, '100 Market Street', NULL, 'Springfield', 'IL', '62701', 'US'),
    (2, '200 Oak Avenue', 'Apt 2B', 'Madison', 'WI', '53703', 'US'),
    (3, '300 Pine Road', NULL, 'Austin', 'TX', '73301', 'US'),
    (4, '400 Maple Lane', NULL, 'Denver', 'CO', '80205', 'US'),
    (5, '500 Cedar Boulevard', 'Suite 5', 'Phoenix', 'AZ', '85001', 'US'),
    (6, '600 Birch Way', NULL, 'Portland', 'OR', '97205', 'US'),
    (7, '700 Walnut Street', NULL, 'Boston', 'MA', '02108', 'US'),
    (8, '800 Chestnut Drive', NULL, 'Seattle', 'WA', '98101', 'US'),
    (9, '900 Elm Circle', NULL, 'Atlanta', 'GA', '30303', 'US'),
    (10, '1000 Ash Place', NULL, 'Miami', 'FL', '33101', 'US'),
    (11, '1100 Hickory Court', NULL, 'Columbus', 'OH', '43004', 'US'),
    (12, '1200 Sycamore Street', NULL, 'Raleigh', 'NC', '27601', 'US'),
    (13, '1300 Poplar Drive', NULL, 'Nashville', 'TN', '37201', 'US'),
    (14, '1400 Spruce Terrace', NULL, 'Boise', 'ID', '83702', 'US'),
    (15, '1500 Fir Trail', NULL, 'Salt Lake City', 'UT', '84101', 'US'),
    (16, '1600 Magnolia Way', 'Unit 4', 'New Orleans', 'LA', '70112', 'US'),
    (17, '1700 Palm Avenue', NULL, 'San Diego', 'CA', '92101', 'US'),
    (18, '1800 Sequoia Loop', NULL, 'San Jose', 'CA', '95112', 'US'),
    (19, '1900 Aspen Ridge', NULL, 'Boulder', 'CO', '80302', 'US'),
    (20, '2000 Juniper Lane', NULL, 'Minneapolis', 'MN', '55401', 'US')
ON CONFLICT (address_id) DO NOTHING;

INSERT INTO privacy_settings (privacy_settings_id, marketing_emails_enabled, two_factor_enabled)
VALUES
    (1, TRUE, FALSE),
    (2, FALSE, TRUE),
    (3, TRUE, TRUE),
    (4, FALSE, FALSE),
    (5, TRUE, FALSE),
    (6, FALSE, TRUE),
    (7, TRUE, TRUE),
    (8, FALSE, FALSE),
    (9, TRUE, FALSE),
    (10, TRUE, TRUE),
    (11, FALSE, TRUE),
    (12, TRUE, FALSE),
    (13, FALSE, FALSE),
    (14, TRUE, TRUE),
    (15, FALSE, TRUE),
    (16, TRUE, FALSE),
    (17, TRUE, TRUE),
    (18, FALSE, FALSE),
    (19, TRUE, TRUE),
    (20, FALSE, TRUE)
ON CONFLICT (privacy_settings_id) DO NOTHING;

INSERT INTO customer (
    customer_id,
    first_name,
    middle_name,
    last_name,
    address_id,
    privacy_settings_id
)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Alice', NULL, 'Smith', 1, 1),
    ('22222222-2222-2222-2222-222222222222', 'Benjamin', 'Arthur', 'Reed', 2, 2),
    ('33333333-3333-3333-3333-333333333333', 'Charlotte', NULL, 'Brown', 3, 3),
    ('44444444-4444-4444-4444-444444444444', 'Dominic', 'Lee', 'Miller', 4, 4),
    ('55555555-5555-5555-5555-555555555555', 'Evelyn', NULL, 'Davis', 5, 5),
    ('66666666-6666-6666-6666-666666666666', 'Felix', NULL, 'Wilson', 6, 6),
    ('77777777-7777-7777-7777-777777777777', 'Grace', 'Lynn', 'Taylor', 7, 7),
    ('88888888-8888-8888-8888-888888888888', 'Henry', NULL, 'Anderson', 8, 8),
    ('99999999-9999-9999-9999-999999999999', 'Isabella', NULL, 'Thomas', 9, 9),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Jackson', 'Michael', 'Clark', 10, 10),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Katherine', NULL, 'Lopez', 11, 11),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Liam', NULL, 'Hughes', 12, 12),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Maya', 'Rose', 'Patel', 13, 13),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Noah', NULL, 'Bennett', 14, 14),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'Olivia', NULL, 'Morgan', 15, 15),
    ('11112222-3333-4444-5555-666677778888', 'Parker', NULL, 'Sanders', 16, 16),
    ('9999aaaa-bbbb-cccc-dddd-eeeeffff0000', 'Quinn', NULL, 'Foster', 17, 17),
    ('abcdabcd-abcd-abcd-abcd-abcdabcdabcd', 'Riley', NULL, 'Nguyen', 18, 18),
    ('12341234-1234-1234-1234-123412341234', 'Sophia', NULL, 'Ramirez', 19, 19),
    ('56785678-5678-5678-5678-567856785678', 'Theodore', 'James', 'Ellis', 20, 20)
ON CONFLICT (customer_id) DO NOTHING;

INSERT INTO customer_email (customer_id, email)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'alice.smith@example.com'),
    ('22222222-2222-2222-2222-222222222222', 'ben.reed@example.com'),
    ('33333333-3333-3333-3333-333333333333', 'charlotte.brown@example.com'),
    ('44444444-4444-4444-4444-444444444444', 'dominic.miller@example.com'),
    ('55555555-5555-5555-5555-555555555555', 'evelyn.davis@example.com'),
    ('66666666-6666-6666-6666-666666666666', 'felix.wilson@example.com'),
    ('77777777-7777-7777-7777-777777777777', 'grace.taylor@example.com'),
    ('88888888-8888-8888-8888-888888888888', 'henry.anderson@example.com'),
    ('99999999-9999-9999-9999-999999999999', 'isabella.thomas@example.com'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'jackson.clark@example.com'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'katherine.lopez@example.com'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'liam.hughes@example.com'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'maya.patel@example.com'),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'noah.bennett@example.com'),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'olivia.morgan@example.com'),
    ('11112222-3333-4444-5555-666677778888', 'parker.sanders@example.com'),
    ('9999aaaa-bbbb-cccc-dddd-eeeeffff0000', 'quinn.foster@example.com'),
    ('abcdabcd-abcd-abcd-abcd-abcdabcdabcd', 'riley.nguyen@example.com'),
    ('12341234-1234-1234-1234-123412341234', 'sophia.ramirez@example.com'),
    ('56785678-5678-5678-5678-567856785678', 'theodore.ellis@example.com')
ON CONFLICT (customer_id, email) DO NOTHING;

INSERT INTO customer_phone_number (customer_id, phone_type, phone_number)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'mobile', '+12175550100'),
    ('22222222-2222-2222-2222-222222222222', 'work', '+16085550101'),
    ('33333333-3333-3333-3333-333333333333', 'mobile', '+15125550102'),
    ('44444444-4444-4444-4444-444444444444', 'home', '+13035550103'),
    ('55555555-5555-5555-5555-555555555555', 'mobile', '+16025550104'),
    ('66666666-6666-6666-6666-666666666666', 'mobile', '+15035550105'),
    ('77777777-7777-7777-7777-777777777777', 'mobile', '+16175550106'),
    ('88888888-8888-8888-8888-888888888888', 'work', '+12065550107'),
    ('99999999-9999-9999-9999-999999999999', 'mobile', '+14045550108'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'home', '+13055550109'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'mobile', '+16145550110'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'work', '+19195550111'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'mobile', '+16155550112'),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'mobile', '+12085550113'),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'home', '+18015550114'),
    ('11112222-3333-4444-5555-666677778888', 'mobile', '+15045550115'),
    ('9999aaaa-bbbb-cccc-dddd-eeeeffff0000', 'mobile', '+16105550116'),
    ('abcdabcd-abcd-abcd-abcd-abcdabcdabcd', 'work', '+14085550117'),
    ('12341234-1234-1234-1234-123412341234', 'mobile', '+17205550118'),
    ('56785678-5678-5678-5678-567856785678', 'mobile', '+19525550119')
ON CONFLICT (customer_id, phone_number) DO NOTHING;

COMMIT;

-- Smoke test
SELECT COUNT(*) AS customer_count FROM customer;

