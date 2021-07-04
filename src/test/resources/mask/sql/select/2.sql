SELECT
*
FROM
users
JOIN user_divisions ON users.division_id = user_divisions.id AND user_divisions.name = 'Sensitive Data';
