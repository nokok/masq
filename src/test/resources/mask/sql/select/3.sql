SELECT *
FROM users u
         JOIN user_divisions d ON u.division_id = d.id AND d.name = 'Sensitive Data';
