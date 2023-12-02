CREATE TABLE IF NOT EXISTS users (
  user_id bigserial NOT NULL PRIMARY KEY,
  login varchar(50) UNIQUE NOT NULL,
  mail varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS apartments (
  apartment_id bigserial NOT NULL PRIMARY KEY,
  user_id bigint NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
  daily_price float NOT NULL,
  title varchar(50),
  country varchar(50),
  city varchar(50),
  street varchar(50),
  building_nr varchar(50),
  apartment_nr varchar(50)
);

CREATE TABLE IF NOT EXISTS external_offers (
  offer_id bigserial NOT NULL PRIMARY KEY,
  apartment_id bigint NOT NULL REFERENCES apartments(apartment_id) ON DELETE CASCADE,
  service_type int NOT NULL,
  external_link varchar(250) NOT NULL
);

CREATE TABLE IF NOT EXISTS reservations (
  reservation_id bigserial NOT NULL PRIMARY KEY,
  apartment_id bigint NOT NULL REFERENCES apartments(apartment_id) ON DELETE CASCADE,
  start_date date NOT NULL,
  end_date date NOT NULL
);

CREATE TABLE IF NOT EXISTS external_accounts (
  account_id bigserial NOT NULL PRIMARY KEY,
  user_id bigint NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
  login varchar(50) NOT NULL,
  password varchar(50) NOT NULL,
  service_type int NOT NULL
);

CREATE TABLE IF NOT EXISTS contacts (
  contact_id bigserial NOT NULL PRIMARY KEY,
  user_id bigint NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
  type int NOT NULL,
  receiver varchar(50) NOT NULL,
  message varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS finances (
  finance_id bigserial NOT NULL PRIMARY KEY,
  user_id bigint NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
  apartment_id bigint NOT NULL REFERENCES apartments(apartment_id) ON DELETE CASCADE,
  event_id bigint,
  event_type int,
  source int NOT NULL,
  price float4 NOT NULL,
  date date NOT NULL,
  details varchar(50)
);

ALTER TABLE reservations
DROP CONSTRAINT IF EXISTS reservations_apartment_id_fkey;

ALTER TABLE reservations
ADD CONSTRAINT reservations_apartment_id_fkey
FOREIGN KEY (apartment_id) REFERENCES apartments(apartment_id)
ON DELETE CASCADE;

ALTER TABLE finances
DROP CONSTRAINT IF EXISTS finances_apartment_id_fkey;

ALTER TABLE reservations
ADD CONSTRAINT finances_apartment_id_fkey
FOREIGN KEY (apartment_id) REFERENCES apartments(apartment_id)
ON DELETE CASCADE;