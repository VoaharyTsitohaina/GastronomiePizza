insert into dish (name, unit_price)
    values ('Hot Dog', 15000.00);

insert into ingredient (name, unit)
    values ('saucisse' ,'G'), ('huile', 'L'), ('oeuf', 'U'), ('pain', 'U');

insert into dish_ingredient (id_dish, id_ingredient, required_quantity, unit)
    values (1, 1, 100.00, 'U'), (1, 2, 0.15, 'L'), (1, 3, 1.00,'U'), (1, 4, 1.00, 'U');

insert into price (id_ingredient, unit_price, date)
    values (1, 20.00, '2025-01-01'), (2, 10000.00, '2025-01-01'), (3, 1000.00, '2025-01-01'), (4, 1000.00, '2025-01-01'),
           (1, 15.00, '2024-11-01'), (2, 9000.00, '2024-11-01'), (3, 900.00, '2024-11-01'), (4, 1000.00, '2024-11-01'),
           (1, 12.00, '2024-10-01'), (2, 8500.00, '2024-10-01'), (3, 800.00, '2024-10-01'), (4, 900.00, '2024-10-01');



-- Insertion des mouvements d'entrée initiaux (au 1er février 2025 à 8h)
INSERT INTO stock_movement (id_ingredient, movement_type, quantity, movement_datetime)
VALUES
    (3, 'entree', 100, '2025-02-01 08:00:00'),   -- Oeuf
    (4, 'entree', 50, '2025-02-01 08:00:00'),    -- Pain
    (1, 'entree', 10000, '2025-02-01 08:00:00'),  -- Saucisse
    (2, 'entree', 20, '2025-02-01 08:00:00');     -- Huile

-- Insertion des mouvements de sortie
INSERT INTO stock_movement (id_ingredient, movement_type, quantity, movement_datetime)
VALUES
    (3, 'sortie', 10, '2025-02-02 10:00:00'),  -- Oeuf, sortie
    (3, 'sortie', 10, '2025-02-03 15:00:00'),  -- Oeuf, sortie
    (4, 'sortie', 20, '2025-02-05 16:00:00');  -- Pain, sortie

-- Pour que le stock de "Pain" devienne 80 unités le 24 février 2025,
-- nous ajoutons une entrée supplémentaire pour le pain
INSERT INTO stock_movement (id_ingredient, movement_type, quantity, movement_datetime)
VALUES
    (4, 'entree', 50, '2025-02-10 08:00:00');
