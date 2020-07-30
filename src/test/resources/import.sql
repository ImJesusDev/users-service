INSERT INTO users (first_name, last_name, email, username, password, enabled, created_at, last_connection) VALUES ('Jesús', 'Díaz', 'test@mail.com', 'test', '$2a$10$yz8jfEWItmAfNaivhnv.ReY/E6tGyDFVbrIT3XDUjQ3wvrJxH0Hya', true, NOW(), NOW());
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_OPERATOR');
INSERT INTO users_x_roles (user_id, role_id) VALUES (1,1);
INSERT INTO users_x_roles (user_id, role_id) VALUES (1,2);
