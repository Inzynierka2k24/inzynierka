INSERT INTO users VALUES (default, 'user1', 'user1@mail.com');
INSERT INTO users VALUES (default, 'user2', 'user2@mail.com');
INSERT INTO users VALUES (default, 'user3', 'user3@mail.com');

INSERT INTO apartments VALUES (1, 1, 123.10, 'Apartment 1', 'Abc', 'Def', 'Ghi', '12B', '2C');
INSERT INTO apartments VALUES (2, 1, 431.64, 'Apartment 2', 'Cba', 'Fed', 'Ihg', '35A', '9');

INSERT INTO reservations VALUES (default, 1, '2023-11-11', '2023-11-30');
INSERT INTO reservations VALUES (default, 2, '2023-11-12', '2023-11-15');
INSERT INTO reservations VALUES (default, 2, '2023-11-01', '2023-11-07');

INSERT INTO finances VALUES (default, 1, 1, 1, 1, 1, 333, '2023-11-30', 'Abc');
INSERT INTO finances VALUES (default, 1, 1, 1, 2, 2, 1000, '2023-10-31', 'Abc');
INSERT INTO finances VALUES (default, 1, 2, 1, 2, 3, 120, '2023-11-10', 'Abc');

INSERT INTO external_offers VALUES (default, 1, 2, 'https://www.trivago.pl/pl');

