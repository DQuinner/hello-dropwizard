-- A script to create employee table
create table person(
    -- auto-generated primary key
    id bigserial NOT NULL,
    name varchar(255) not null,
    PRIMARY KEY (id)
);