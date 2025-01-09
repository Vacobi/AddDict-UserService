create table if not exists "user"
(
    id bigint primary key generated always as identity unique,
    login varchar(32) not null unique,
    password varchar(64) not null
)