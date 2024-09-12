--liquibase formatted sql
--changeset annill:create_person_table

create table if not exists persons
(
    id      bigserial not null primary key,
    age     integer,
    height  integer,
    name    varchar(255),
    surname varchar(255)
);

