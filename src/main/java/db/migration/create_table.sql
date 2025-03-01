create table if not exists dish (
    id_dish bigserial primary key,
    "name" varchar (50) not null,
    unit_price double precision
);

DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'unit') THEN
            CREATE TYPE unit AS ENUM ('G', 'L', 'U');
        END IF;
    END
$$;


create table if not exists ingredient (
    id_ingredient bigserial primary key not null,
    "name" varchar (50),
    unit unit
);


create table if not exists dish_ingredient (
    id_dish bigint references dish(id_dish),
    id_ingredient bigint references ingredient(id_ingredient),
    required_quantity double precision,
    unit unit,
    primary key (id_dish, id_ingredient)
);

create table if not exists price (
    id_ingredient bigint references ingredient(id_ingredient),
    unit_price double precision,
    date date,
    primary key (id_ingredient, unit_price)
);

DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'movement_type') THEN
            CREATE TYPE movement_type AS ENUM ('entree', 'sortie');
        END IF;
    END
$$;

create table if not exists stock_movement (
    id_stock bigserial primary key,
    id_ingredient bigint references ingredient(id_ingredient),
    movement_type movement_type,
    quantity double precision,
    movement_datetime timestamp
);

