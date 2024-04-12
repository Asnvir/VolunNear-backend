CREATE TABLE IF NOT EXISTS volun_near_app_db.roles (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id));

INSERT INTO roles (name)
SELECT 'ROLE_VOLUNTEER' WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'ROLE_VOLUNTEER'
);

INSERT INTO roles (name)
SELECT 'ROLE_ORGANISATION' WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'ROLE_ORGANISATION'
);