-- Insert sample InsuranceItems
INSERT INTO insurance_item (id, name, is_optional, franchise_percentage, amount, is_deleted)
VALUES (1, 'Motor Insurance', false, 10, 500.0, false);

INSERT INTO insurance_item (id, name, is_optional, franchise_percentage, amount, is_deleted)
VALUES (2, 'Theft Insurance', true, 5, 200.0, false);

INSERT INTO insurance_item (id, name, is_optional, franchise_percentage, amount, is_deleted)
VALUES (3, 'Fire Insurance', false, 15, 300.0, false);

INSERT INTO insurance_item (id, name, is_optional, franchise_percentage, amount, is_deleted)
VALUES (4, 'Liability Coverage', false, 20, 400.0, false);

-- Insert sample InsurancePlans
INSERT INTO insurance_plan (id, name, is_premium, is_deleted)
VALUES (1, 'Basic Plan', false, false);

INSERT INTO insurance_plan (id, name, is_premium, is_deleted)
VALUES (2, 'Premium Plan', true, false);

-- Associate InsuranceItems with InsurancePlans (many-to-many join table)
INSERT INTO insurance_plan_item (insurance_plan_id, insurance_item_id) VALUES (1, 1);
INSERT INTO insurance_plan_item (insurance_plan_id, insurance_item_id) VALUES (1, 4); -- Basic Plan includes Liability Coverage

INSERT INTO insurance_plan_item (insurance_plan_id, insurance_item_id) VALUES (2, 1); -- Premium Plan includes Motor Insurance
INSERT INTO insurance_plan_item (insurance_plan_id, insurance_item_id) VALUES (2, 2); -- Premium Plan includes Theft Insurance
INSERT INTO insurance_plan_item (insurance_plan_id, insurance_item_id) VALUES (2, 3); -- Premium Plan includes Fire Insurance
INSERT INTO insurance_plan_item (insurance_plan_id, insurance_item_id) VALUES (2, 4); -- Premium Plan includes Liability Coverage

-- Insert sample Country and Car Plates
INSERT INTO country (id, name, country_code, plates_regex)
VALUES (1, 'Serbia', 'SRB', '^[A-Z]{2}[- ]?\\d{3,4}[- ]?[A-Z]{2}$');

INSERT INTO car_plates (id, country_id, plate_number) VALUES (1, 1, 'NS 645 PO');
INSERT INTO car_plates (id, country_id, plate_number) VALUES (2, 1, 'BG-5545-LA');

-- Insert sample Proposals
INSERT INTO proposal (id, is_valid, update_date, creation_date, amount, car_plates_id, is_deleted, proposal_status, insurance_id)
VALUES (1, true, DATEADD('YEAR', 1, CURRENT_TIMESTAMP), DATEADD('YEAR', 1, CURRENT_TIMESTAMP), 2500.0, 1, false, 'INITIALIZED', 1);

INSERT INTO proposal (id, is_valid, update_date, creation_date, amount, car_plates_id, is_deleted, proposal_status, insurance_id)
VALUES (2, true, DATEADD('YEAR', 1, CURRENT_TIMESTAMP), DATEADD('YEAR', 1, CURRENT_TIMESTAMP), 5500.0, 2, false, 'SUBSCRIBER_ADDED', 2);

