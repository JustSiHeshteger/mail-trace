INSERT INTO address (country, town, street, postal_code) VALUES
    ('Russia', 'Moscow', 'Tverskaya', '101000'),
    ('Russia', 'Saint Petersburg', 'Nevsky Prospect', '190000'),
    ('Russia', 'Novosibirsk', 'Lenina Street', '630000'),
    ('Russia', 'Vladivoctok', 'Truda Street', '123456');

INSERT INTO person (first_name, second_name, third_name, phone_number, address_id) VALUES
    ('Ivan', 'Ivanov', 'Ivanovich', '79991112233', 3),
    ('Petr', 'Petrov', NULL, '79992223344', 4);

INSERT INTO post_office (post_office_name, address_id) VALUES
    ('Moscow', 1),
    ('Saint Petersburg', 2);

INSERT INTO postage (postage_id, postage_type, status, person_id) VALUES
    ('ea901f00-ecfe-4bfc-9b35-b9e0356d3e21', 'PACKAGE', 'IN_TRANSIT', 1),
    ('ea901f00-ecfe-4bfc-9b35-b9e0356d3e22', 'LETTER', 'DELIVERED', 2);

INSERT INTO tracking (timestamp, postage_id, post_office_id) VALUES
    (NOW(), 'ea901f00-ecfe-4bfc-9b35-b9e0356d3e21', 1),
    (NOW(), 'ea901f00-ecfe-4bfc-9b35-b9e0356d3e22', 2);