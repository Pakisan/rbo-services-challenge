--liquibase formatted sql
--changeset Pavel Bodiachevskii:init-database

create schema events;
alter schema events owner to postgres;

create table if not exists events.events
(
    id         uuid not null primary key,
    created_at timestamp not null,
    payload    jsonb not null,
    type       varchar(255) not null
);
alter table events.events owner to postgres;