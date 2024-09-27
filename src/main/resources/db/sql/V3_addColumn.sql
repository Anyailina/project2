--liquibase formatted sql
--changeset annill:add_column_school_scheme

alter table schools
    add column if not exists deleted boolean;
