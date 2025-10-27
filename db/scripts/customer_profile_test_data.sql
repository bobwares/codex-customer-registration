/*
 * App: Customer Registration
 * Package: db.scripts
 * File: customer_profile_test_data.sql
 * Version: 0.1.0
 * Turns: 1
 * Author: ChatGPT Codex
 * Date: 2025-02-14T00:00:00Z
 * Exports: customer_profile_test_data
 * Description: Inserts representative Customer Profile domain records for local testing.
 */
BEGIN;

-- Insert postal addresses
INSERT INTO postal_address (address_id, line1, line2, city, state, postal_code, country)
VALUES
    (1, '100 Market St', NULL, 'Springfield', 'IL', '62701', 'US'),
    (2, '42 Pine Ave', 'Apt 5', 'Madison', 'WI', '53703', 'US'),
    (3, '88 Cedar Blvd', NULL, 'Austin', 'TX', '73301', 'US'),
    (4, '17 Oak Street', NULL, 'Denver', 'CO', '80203', 'US'),
    (5, '501 Elm Road', NULL, 'Seattle', 'WA', '98101', 'US'),
    (6, '932 Birch Way', 'Suite 120', 'Portland', 'OR', '97205', 'US'),
    (7, '24 Willow Ln', NULL, 'San Diego', 'CA', '92101', 'US'),
    (8, '355 Ash Ct', NULL, 'Boston', 'MA', '02108', 'US'),
    (9, '712 Maple Cir', NULL, 'Phoenix', 'AZ', '85004', 'US'),
    (10, '64 Walnut Dr', 'Unit 3B', 'Miami', 'FL', '33101', 'US'),
    (11, '890 Pearl St', NULL, 'Chicago', 'IL', '60601', 'US'),
    (12, '740 Cypress Ave', NULL, 'Atlanta', 'GA', '30303', 'US'),
    (13, '512 Magnolia Blvd', NULL, 'Raleigh', 'NC', '27601', 'US'),
    (14, '143 Poplar Way', NULL, 'Nashville', 'TN', '37201', 'US'),
    (15, '276 Hickory Rd', NULL, 'Cleveland', 'OH', '44114', 'US'),
    (16, '930 Aspen Pl', NULL, 'Boulder', 'CO', '80302', 'US'),
    (17, '61 Fir Ave', 'Floor 4', 'Newark', 'NJ', '07102', 'US'),
    (18, '447 Palm St', NULL, 'Tampa', 'FL', '33602', 'US'),
    (19, '105 Sequoia Trl', NULL, 'Boise', 'ID', '83702', 'US'),
    (20, '389 Cherry St', NULL, 'Salt Lake City', 'UT', '84101', 'US')
ON CONFLICT DO NOTHING;

-- Insert privacy settings
INSERT INTO privacy_settings (privacy_settings_id, marketing_emails_enabled, two_factor_enabled)
VALUES
    (1, TRUE, FALSE),
    (2, FALSE, TRUE),
    (3, TRUE, TRUE),
    (4, FALSE, FALSE),
    (5, TRUE, FALSE),
    (6, TRUE, TRUE),
    (7, FALSE, TRUE),
    (8, TRUE, FALSE),
    (9, TRUE, TRUE),
    (10, FALSE, FALSE),
    (11, TRUE, TRUE),
    (12, FALSE, TRUE),
    (13, TRUE, FALSE),
    (14, FALSE, FALSE),
    (15, TRUE, TRUE),
    (16, FALSE, TRUE),
    (17, TRUE, FALSE),
    (18, TRUE, TRUE),
    (19, FALSE, FALSE),
    (20, TRUE, TRUE)
ON CONFLICT DO NOTHING;

-- Insert customers
INSERT INTO customer (customer_id, first_name, middle_name, last_name, address_id, privacy_settings_id)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Alice', NULL, 'Miller', 1, 1),
    ('22222222-2222-2222-2222-222222222222', 'Brandon', 'K', 'Foster', 2, 2),
    ('33333333-3333-3333-3333-333333333333', 'Caroline', NULL, 'Nguyen', 3, 3),
    ('44444444-4444-4444-4444-444444444444', 'Derrick', NULL, 'Owens', 4, 4),
    ('55555555-5555-5555-5555-555555555555', 'Elena', 'J', 'Harris', 5, 5),
    ('66666666-6666-6666-6666-666666666666', 'Felix', NULL, 'Morales', 6, 6),
    ('77777777-7777-7777-7777-777777777777', 'Georgia', NULL, 'Reed', 7, 7),
    ('88888888-8888-8888-8888-888888888888', 'Hector', NULL, 'Santana', 8, 8),
    ('99999999-9999-9999-9999-999999999999', 'Isabella', NULL, 'Chen', 9, 9),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Jonah', NULL, 'Price', 10, 10),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Kara', NULL, 'Lopez', 11, 11),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Liam', NULL, 'Mitchell', 12, 12),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Maya', NULL, 'Simmons', 13, 13),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Noah', NULL, 'Diaz', 14, 14),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'Olivia', NULL, 'Stone', 15, 15),
    ('00000000-0000-0000-0000-000000000000', 'Parker', NULL, 'Jensen', 16, 16),
    ('12121212-1212-1212-1212-121212121212', 'Quinn', NULL, 'Patel', 17, 17),
    ('13131313-1313-1313-1313-131313131313', 'Riley', NULL, 'Hughes', 18, 18),
    ('14141414-1414-1414-1414-141414141414', 'Sofia', NULL, 'Gardner', 19, 19),
    ('15151515-1515-1515-1515-151515151515', 'Tyler', NULL, 'Bennett', 20, 20)
ON CONFLICT DO NOTHING;

-- Insert customer emails
INSERT INTO customer_email (customer_id, email)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'alice.miller@example.com'),
    ('22222222-2222-2222-2222-222222222222', 'brandon.foster@example.com'),
    ('33333333-3333-3333-3333-333333333333', 'caroline.nguyen@example.com'),
    ('44444444-4444-4444-4444-444444444444', 'derrick.owens@example.com'),
    ('55555555-5555-5555-5555-555555555555', 'elena.harris@example.com'),
    ('66666666-6666-6666-6666-666666666666', 'felix.morales@example.com'),
    ('77777777-7777-7777-7777-777777777777', 'georgia.reed@example.com'),
    ('88888888-8888-8888-8888-888888888888', 'hector.santana@example.com'),
    ('99999999-9999-9999-9999-999999999999', 'isabella.chen@example.com'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'jonah.price@example.com'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'kara.lopez@example.com'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'liam.mitchell@example.com'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'maya.simmons@example.com'),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'noah.diaz@example.com'),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'olivia.stone@example.com'),
    ('00000000-0000-0000-0000-000000000000', 'parker.jensen@example.com'),
    ('12121212-1212-1212-1212-121212121212', 'quinn.patel@example.com'),
    ('13131313-1313-1313-1313-131313131313', 'riley.hughes@example.com'),
    ('14141414-1414-1414-1414-141414141414', 'sofia.gardner@example.com'),
    ('15151515-1515-1515-1515-151515151515', 'tyler.bennett@example.com')
ON CONFLICT DO NOTHING;

-- Insert customer phone numbers
INSERT INTO customer_phone_number (customer_id, type, number)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'mobile', '+12175550001'),
    ('22222222-2222-2222-2222-222222222222', 'mobile', '+16085550002'),
    ('33333333-3333-3333-3333-333333333333', 'mobile', '+15125550003'),
    ('44444444-4444-4444-4444-444444444444', 'mobile', '+13035550004'),
    ('55555555-5555-5555-5555-555555555555', 'mobile', '+12065550005'),
    ('66666666-6666-6666-6666-666666666666', 'mobile', '+15035550006'),
    ('77777777-7777-7777-7777-777777777777', 'mobile', '+16195550007'),
    ('88888888-8888-8888-8888-888888888888', 'mobile', '+17815550008'),
    ('99999999-9999-9999-9999-999999999999', 'mobile', '+16025550009'),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'mobile', '+13055550010'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'mobile', '+13125550011'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'mobile', '+14045550012'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'mobile', '+19175550013'),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'mobile', '+16155550014'),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'mobile', '+12165550015'),
    ('00000000-0000-0000-0000-000000000000', 'mobile', '+13035550016'),
    ('12121212-1212-1212-1212-121212121212', 'mobile', '+19725550017'),
    ('13131313-1313-1313-1313-131313131313', 'mobile', '+18135550018'),
    ('14141414-1414-1414-1414-141414141414', 'mobile', '+12025550019'),
    ('15151515-1515-1515-1515-151515151515', 'mobile', '+18015550020')
ON CONFLICT DO NOTHING;

COMMIT;

-- Smoke test
SELECT COUNT(*) AS customer_count FROM customer;
