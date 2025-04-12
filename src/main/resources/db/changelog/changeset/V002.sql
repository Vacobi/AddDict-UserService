create table if not exists "subscribe"
(
    id bigint primary key generated always as identity unique,
    subscriber bigint not null references "user" (id),
    author     bigint not null references "user" (id)
)