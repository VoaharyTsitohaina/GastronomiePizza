create database gastropizza;

\c gastropizza;

create table if not exists dish (
    id_dish serial primary key,
    "name" varchar (50) not null,
    unit_price double precision
);

create table if not exists ingredient (
    id_ingredient serial primary key not null,
    "name" varchar (50),
    unit char(1) check(unit in ('G', 'L', 'U')) not null
);

create table if not exists dish_ingredient (
    id_dish int references dish(id_dish),
    id_ingredient int references ingredient(id_ingredient),
    required_quantity double precision,
    unit char(1) check(unit in ('G', 'U', 'L')),
    primary key (id_dish, id_ingredient)
);

create table if not exists price (
    id_ingredient int references ingredient(id_ingredient),
    unit_price double precision,
    date date,
    primary key (id_ingredient, unit_price)
);

