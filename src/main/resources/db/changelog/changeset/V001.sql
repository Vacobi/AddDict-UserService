create table if not exists token
(
    id            bigint primary key generated always as identity unique,
    refresh_token text unique,
    created_at    timestamptz not null default now(),
    expire_at     timestamptz not null,
    status        varchar(32) not null default 'ACTIVE',
    user_id       bigint      not null references "user" (id)
);