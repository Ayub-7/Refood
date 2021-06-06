INSERT INTO business (name, description, created, user_id, business_type, address_id) VALUES
    /* 5 businesses */
    ('Dabshots', 'Nullam varius. Nulla facilisi. Cras non velit nec nisi vulputate nonummy.', '2020-05-18 21:06:11', 1, 'CHARITABLE_ORGANISATION', 21),
    ('Layo', 'Nam ultrices, libero non mattis pulvinar, nulla pede ullamcorper augue, a suscipit nulla elit ac nulla. Sed vel enim sit amet nunc viverra dapibus. Nulla suscipit ligula in lacus.', '2020-08-25 22:22:19', 2, 'ACCOMMODATION_AND_FOOD_SERVICES', 22),
    ('Skinder', 'Morbi sem mauris, laoreet ut, rhoncus aliquet, pulvinar sed, nisl. Nunc rhoncus dui vel sem.', '2020-09-11 20:50:50', 3, 'RETAIL_TRADE', 23),
    ('Skiptube', 'Phasellus in felis.', '2020-10-18 10:30:14', 4, 'RETAIL_TRADE', 24),
    ('Photojam', 'Praesent blandit. Nam nulla.', '2020-05-23 08:21:22', 5, 'RETAIL_TRADE', 25);


INSERT INTO business_admins (business_id, user_id) VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5),
    (3, 7),
    (5, 9),
    (4, 6),
    (5, 8),
    (2, 9),
    (2, 1),
    (3, 1);



INSERT INTO product (id, name, description, manufacturer, recommended_retail_price, created, business_id) VALUES
    /* 20 products */
    ('WAUEH98E37A680027', 'Wine - White, Pinot Grigio', 'integer ac leo pellentesque ultrices mattis odio donec vitae nisi nam ultrices libero non mattis pulvinar', 'Bubbletube', 51.63, '2021-01-27 16:07:14', 2),
    ('WAUVT64B54N722288', 'Pastry - Cheese Baked Scones', 'amet erat nulla tempus vivamus', 'Watties', 19.88, '2021-03-05 14:36:54', 1),
    ('KNDJT2A29D7324794', 'Beef - Ground Lean Fresh', 'vestibulum ante ipsum primis in faucibus', 'Watties', 59.43, '2021-02-06 20:17:16', 3),
    ('W04GP5EC0B1798680', 'Compound - Mocha', 'vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc', 'Nestle', 88.93, '2021-01-11 20:54:46', 1),
    ('SCFEBBCF6CG218162', 'Yogurt - Blueberry, 175 Gr', 'nec nisi volutpat eleifend donec ut dolor morbi vel lectus in quam fringilla rhoncus mauris enim leo', 'Gabcube', 72.83, '2021-01-15 16:56:26', 5),
    ('WBAPM7C51AE969438', 'Longos - Lasagna Veg', 'a pede posuere nonummy integer non velit donec diam neque vestibulum eget vulputate ut ultrices vel augue vestibulum ante ipsum', 'Skimia', 36.81, '2021-03-04 03:38:04', 2),
    ('5UXWX7C53EL958298', 'Ice Cream Bar - Drumstick', 'eu mi nulla ac enim', 'Formula Foods', 50.2, '2021-01-01 07:57:56', 5),
    ('WBAVD33586K975326', 'Peas Snow', 'porttitor pede justo eu massa donec dapibus duis at velit eu est congue elementum', 'Peapea Foods', 73.9, '2021-04-06 19:28:28', 2),
    ('5NPDH4AE0EH887279', 'Cheese - Pied De Vents', 'eget congue eget semper rutrum nulla nunc purus phasellus in felis donec semper sapien a libero nam dui', 'Food Food Food', 36.65, '2021-01-24 21:22:47', 5),
    ('2C3CDZFJ2FH936197', 'Sherbet - Raspberry', 'justo sollicitudin ut suscipit a feugiat et eros vestibulum ac est', 'Sherbet Co', 86.18, '2021-01-28 19:33:16', 2),
    ('WBAUP9C53DV435366', 'Energy Drink', 'ipsum dolor sit amet consectetuer adipiscing elit proin', 'Coca-Cola', 91.48, '2021-03-04 02:58:24', 4),
    ('YV1612FS9E2422348', 'Bread Base - Gold Formel', 'condimentum neque sapien placerat ante nulla justo', 'Coopolandes', 29.81, '2021-01-08 21:31:51', 3),
    ('WAUMF98KX9A331487', 'Chicken - White Meat With Tender', 'in eleifend quam a odio in hac habitasse platea dictumst maecenas ut massa quis augue luctus', 'Chicken Little Ltd', 30.91, '2021-01-03 04:39:36', 3),
    ('5UXFG8C5XAL953759', 'Yogurt - Strawberry, 175 Gr', 'justo lacinia eget tincidunt eget tempus vel pede morbi porttitor lorem id ligula suspendisse ornare consequat', 'Formula Foods', 95.84, '2021-02-10 6:35:49', 3),
    ('WAUJC58E75A227743', 'Fish - Scallops, Cold Smoked', 'rutrum at lorem integer tincidunt ante vel ipsum praesent blandit lacinia erat vestibulum sed', 'United Fisheries', 65.12, '2021-02-03 19:00:32', 2),
    ('1N6AA0CA2AN447971', 'Wine - Bouchard La Vignee Pinot', 'bibendum morbi non quam nec dui luctus rutrum nulla tellus in sagittis dui', 'Lorem Ipsum', 41.35, '2021-03-16 16:26:06', 5),
    ('SCBDR3ZA2BC827880', 'Nestea - Iced Tea', 'a suscipit nulla elit ac nulla sed vel enim sit amet nunc viverra dapibus nulla suscipit ligula in lacus curabitur', 'Pepsico', 24.27, '2021-01-27 18:14:57', 5),
    ('WAUUL98E96A125348', 'Calvados - Boulard', 'quis lectus suspendisse potenti in eleifend quam a', 'Vegesdirect', 9.03, '2021-01-25 22:18:45', 3),
    ('JN8AF5MR1ET433412', 'Juice - Pineapple, 341 Ml', 'ridiculus mus etiam vel augue vestibulum', 'Juicelord', 2.66, '2021-03-09 22:56:37', 3),
    ('WAUEH78E77A454447', 'Bread - Focaccia Quarter', 'orci vehicula condimentum curabitur in libero', 'Bread Company', 22.34, '2021-04-05 18:35:12', 5);


INSERT INTO inventory (best_before, business_id, expires, manufactured, price_per_item, product_id, quantity, sell_by, total_price) VALUES
    ('2021-05-27 12:00:00', 1, '2021-05-27 12:00:00', '2021-01-27 12:00:00', 5.00, 'WAUVT64B54N722288', 10, '2021-05-25 12:00:00', 50.00),
    ('2021-08-27 12:00:00', 1, '2021-08-27 12:00:00', '2020-01-27 12:00:00', 3.00, 'W04GP5EC0B1798680', 7, null, 80.00),
    ('2021-08-27 12:00:00', 1, '2021-10-27 12:00:00', '2021-01-27 12:00:00', 1.00, 'WAUVT64B54N722288', 9, null, 50.00),
    ('2021-09-27 12:00:00', 1, '2021-11-27 12:00:00', '2021-01-27 12:00:00', 6.00, 'WAUVT64B54N722288', 1, null, 50.00),
    ('2021-09-27 12:00:00', 1, '2021-11-27 12:00:00', '2021-01-27 12:00:00', 6.00, 'WAUVT64B54N722288', 1, null, 50.00),
    ('2021-09-27 12:00:00', 1, '2021-11-27 12:00:00', '2021-01-27 12:00:00', 6.00, 'WAUVT64B54N722288', 1, null, 50.00);

INSERT INTO listing (closes, created, more_info, price, quantity, inventory_item_id) VALUES
    ('2021-09-22 12:00:00', '2021-01-27 12:00:00', 'Not negotiable.', 10, 3, 1),
    ('2021-10-27 12:00:00', '2021-01-28 12:00:00', 'Could be negotiable.', 10, 5, 1),
    ('2021-11-12 12:00:00', '2021-02-01 12:00:00', 'Price is negotiable.', 10, 5, 1),
    ('2021-09-08 12:00:00', '2021-02-02 12:00:00', 'Contact us for more information.', 15.5, 1, 2),
    ('2021-10-19 12:00:00', '2021-02-03 12:00:00', 'Do not contact us for more information.', 5.99, 2, 2);




