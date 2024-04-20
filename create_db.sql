create database post_test;
INSERT INTO post_office (index, name, address)
VALUES (10001, 'Central Post Office', 'Main Street, City'),
       (10002, 'North Branch Post Office', 'North Avenue, City'),
       (10003, 'South Branch Post Office', 'South Street, City');

INSERT INTO shipment (recipient_name, type, recipient_zip_code, recipient_address, status)
VALUES ('John Doe', 'PACKAGE', 12345, '100 Main St, Anytown', 'REGISTERED'),
       ('Jane Smith', 'LETTER', 54321, '200 Elm St, Anytown', 'IN_POST_OFFICE');
