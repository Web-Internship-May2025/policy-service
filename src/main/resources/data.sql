-- Insert sample InsuranceItems
INSERT INTO insurance_item (id, name, is_optional, franchise_percentage, amount, is_deleted) VALUES (1, 'Motor Insurance', false, 10, 500.0, false);
INSERT INTO insurance_item (id, name, is_optional, franchise_percentage, amount, is_deleted) VALUES (2, 'Theft Insurance', true, 5, 200.0, false);
INSERT INTO insurance_item (id, name, is_optional, franchise_percentage, amount, is_deleted) VALUES (3, 'Fire Insurance', false, 15, 300.0, false);
INSERT INTO insurance_item (id, name, is_optional, franchise_percentage, amount, is_deleted) VALUES (4, 'Liability Coverage', false, 20, 400.0, false);

-- Insert sample InsurancePlans
INSERT INTO insurance_plan (id, name, is_premium, is_deleted) VALUES (1, 'Basic Plan', false, false);
INSERT INTO insurance_plan (id, name, is_premium, is_deleted) VALUES (2, 'Premium Plan', true, false);

-- Associate InsuranceItems with InsurancePlans (many-to-many join table)
INSERT INTO insurance_plan_item (insurance_plan_id, insurance_item_id) VALUES (1, 1);
INSERT INTO insurance_plan_item (insurance_plan_id, insurance_item_id) VALUES (1, 4); -- Basic Plan includes Liability Coverage
INSERT INTO insurance_plan_item (insurance_plan_id, insurance_item_id) VALUES (2, 1); -- Premium Plan includes Motor Insurance
INSERT INTO insurance_plan_item (insurance_plan_id, insurance_item_id) VALUES (2, 2); -- Premium Plan includes Theft Insurance
INSERT INTO insurance_plan_item (insurance_plan_id, insurance_item_id) VALUES (2, 3); -- Premium Plan includes Fire Insurance
INSERT INTO insurance_plan_item (insurance_plan_id, insurance_item_id) VALUES (2, 4); -- Premium Plan includes Liability Coverage

INSERT INTO country (id, name, country_code, plates_regex) VALUES (1, 'Serbia', 'SRB', '^[A-Z]{2}[- ]?\\d{3,4}[- ]?[A-Z]{2}$');

INSERT INTO car_plates (id, country_id, plate_number) VALUES (1, 1, 'NS 645 PO');
INSERT INTO car_plates (id, country_id, plate_number) VALUES (2, 1, 'BG-5545-LA');


INSERT INTO proposal (id, is_valid, update_date, creation_date, amount, car_id, is_deleted, proposal_status, insurance_id, sales_agent_id, subscriber_id)
VALUES
  (1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2500.0, 3, false, 'INITIALIZED', 1, 1, 3),
  (2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5500.0, 3, false, 'SUBSCRIBER_ADDED', 2, 2, 3),
  (3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 75500.0, 1,  false, 'PAID', 2, 6, 3),
  (4, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 5500.0, 1, false, 'CONFIRMED', 2, 1, 3);

-- Insert Policies za te Proposal-e
INSERT INTO policy (
  id, date_signed, expiring_date, money_received_date, amount, is_deleted, proposal_id
) VALUES
  (1, CURRENT_TIMESTAMP, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 1 YEAR), CURRENT_TIMESTAMP, 2500.0, false, 1),
  (2, CURRENT_TIMESTAMP, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 1 YEAR), CURRENT_TIMESTAMP, 5500.0, false, 2),
  (3, CURRENT_TIMESTAMP, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 1 YEAR), CURRENT_TIMESTAMP, 7500.0, false, 3),
  (4, CURRENT_TIMESTAMP, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 1 YEAR), CURRENT_TIMESTAMP, 5500.0, false, 4);
-- 3) Sada franchise INSERT-ovi mogu sigurno da se izvrše:
INSERT INTO franchise (id, percentage, is_deleted, proposal_id, insurance_item_id)
VALUES
 (1, 10, false, 1, 1),
 (2, 20, false, 2, 4);
