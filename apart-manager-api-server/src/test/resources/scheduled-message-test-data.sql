INSERT INTO users VALUES (default, 'testUser', 'testUser@gmail.com');

INSERT INTO contacts VALUES (default, 1, 'Kubol Grubol', 1, 'grubol@example.com', '761844591', true, false, 300);
INSERT INTO contacts VALUES (default, 1, 'Pan Marcin', 2, 'marcin.mechanic@example.com', '525701839', false, true, 250);

INSERT INTO apartments VALUES (default, 1, 100.0, 'Apartment 1', 'Country 1', 'City 1', 'Street 1', '1', '1A');
INSERT INTO apartments VALUES (default, 1, 200.0, 'Apartment 2', 'Country 2', 'City 2', 'Street 2', '2', '2B');


INSERT INTO scheduled_messages VALUES (default, 1, 1, 'Test Message', 1, 1, 1);

INSERT INTO scheduled_messages_apartments VALUES (1, (SELECT apartment_id FROM apartments WHERE title = 'Apartment 1'));
INSERT INTO scheduled_messages_apartments VALUES (1, (SELECT apartment_id FROM apartments WHERE title = 'Apartment 2'));