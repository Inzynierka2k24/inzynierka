CREATE TABLE IF NOT EXISTS users
(
    user_id bigserial          NOT NULL PRIMARY KEY,
    login   varchar(50) UNIQUE NOT NULL,
    mail    varchar(50)        NOT NULL
);

CREATE TABLE IF NOT EXISTS apartments
(
    apartment_id bigserial NOT NULL PRIMARY KEY,
    user_id      bigint    NOT NULL REFERENCES users (user_id),
    daily_price  float     NOT NULL,
    title        varchar(50),
    country      varchar(50),
    city         varchar(50),
    street       varchar(50),
    building_nr  varchar(50),
    apartment_nr varchar(50)
);

CREATE TABLE IF NOT EXISTS external_offers
(
    offer_id      bigserial    NOT NULL PRIMARY KEY,
    apartment_id  bigint       NOT NULL REFERENCES apartments (apartment_id),
    service_type  int          NOT NULL,
    external_link varchar(250) NOT NULL
);

CREATE TABLE IF NOT EXISTS reservations
(
    reservation_id bigserial NOT NULL PRIMARY KEY,
    apartment_id   bigint    NOT NULL REFERENCES apartments (apartment_id),
    start_date     date      NOT NULL,
    end_date       date      NOT NULL
);

CREATE TABLE IF NOT EXISTS external_accounts
(
    account_id   bigserial   NOT NULL PRIMARY KEY,
    user_id      bigint      NOT NULL REFERENCES users (user_id),
    login        varchar(50) NOT NULL,
    password     varchar(50) NOT NULL,
    mail         varchar(50) NOT NULL,
    service_type int         NOT NULL
);

CREATE TABLE IF NOT EXISTS contacts
(
    contact_id          bigserial   NOT NULL PRIMARY KEY,
    user_id             bigint      NOT NULL REFERENCES users (user_id),
    name                varchar(50) NOT NULL,
    type                int         NOT NULL,
    mail                varchar(50),
    phone               varchar(50),
    email_notifications boolean     NOT NULL DEFAULT false,
    sms_notifications   boolean     NOT NULL DEFAULT false,
    price               float4      NOT NULL
);

CREATE TABLE IF NOT EXISTS finances
(
    finance_id   bigserial NOT NULL PRIMARY KEY,
    user_id      bigint    NOT NULL REFERENCES users (user_id),
    apartment_id bigint    NOT NULL REFERENCES apartments (apartment_id),
    event_id     bigint,
    event_type   int,
    source       int       NOT NULL,
    price        float4    NOT NULL,
    date         date      NOT NULL,
    details      varchar(50)
);

CREATE TABLE IF NOT EXISTS scheduled_messages
(
    message_id     bigserial   NOT NULL PRIMARY KEY,
    user_id        bigint      NOT NULL REFERENCES users (user_id),
    contact_id     bigint      NOT NULL REFERENCES contacts (contact_id),
    apartment_id   bigint      NOT NULL REFERENCES apartments (apartment_id),
    message        varchar(50) NOT NULL,
    interval_type  int         NOT NULL,
    interval_value int         NOT NULL,
    trigger_type   int         NOT NULL
);