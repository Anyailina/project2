--liquibase formatted sql
--changeset annill:create_school_scheme

create table if not exists schools
(
    id   bigserial not null primary key,
    name varchar(255),
    city varchar(255)
);

alter table persons
    add column if not exists school_fk bigint;
alter table persons
    add constraint fk_school foreign key (school_fk) references schools (id);


