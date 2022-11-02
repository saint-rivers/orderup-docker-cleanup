--
-- drop table account_users   cascade ;
-- drop table user_groups cascade ;
-- drop table groups cascade ;
-- drop table ou_activation_attempts cascade;
-- drop table ou_feedbacks cascade;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS account_users
(
    id       uuid         not null primary key default uuid_generate_v4(),
    username      varchar(50) not null unique,
    name          varchar(50) not null,
    email         varchar(50) not null unique,
    profile_image text,
    gender        varchar(7),
    phone_number  varchar(50),
    role          varchar(15),
    is_activated  bool
);

CREATE TABLE IF NOT EXISTS groups
(
    id           uuid primary key default uuid_generate_v4(),
    name   varchar(255) not null,
    group_image  text         not null,
    description varchar(255),
    created_date timestamp    not null
);

CREATE TABLE IF NOT EXISTS user_groups
(
    id uuid primary key default uuid_generate_v4(),
    user_id    uuid,
    group_id   uuid,
    is_owner   bool,

    foreign key (user_id) references account_users (id) on delete cascade,
    foreign key (group_id) references groups (id) on delete cascade

);

CREATE TABLE IF NOT EXISTS ou_activation_attempts
(
    id            uuid primary key not null default uuid_generate_v4() ,
    user_id       uuid not null ,
    otp_code  int4 not null,
    request_date timestamp,
    expiration_date timestamp,
    foreign key (user_id) references account_users (id) on delete cascade
);

CREATE TABLE IF NOT EXISTS ou_feedbacks
(
    id            uuid primary key not null default uuid_generate_v4() ,
    user_id       uuid not null unique ,
    feedback      text not null ,
    created_date timestamp,
    foreign key (user_id) references account_users (id) on delete cascade
);

-- insert into ou_activation_attempts (user_id, otp_code, request_date, expiration_date) values ('cdd56a5a-aac0-4d3b-9679-d575ef375400', 666999,  '2022-10-26T09:03:35.765Z', '2022-10-26T09:03:35.765Z');