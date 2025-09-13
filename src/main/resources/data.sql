-- Создание тестовых пользователей
INSERT INTO users (id, username, password, first_name, last_name, email) VALUES 
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Admin', 'User', 'admin@example.com'),
(2, 'moderator', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Moderator', 'User', 'moderator@example.com'),
(3, 'user', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'Regular', 'User', 'user@example.com');

-- Добавление ролей пользователям
INSERT INTO user_roles (user_id, role) VALUES 
(1, 'ROLE_ADMIN'),
(2, 'ROLE_MODERATOR'),
(3, 'ROLE_USER');

-- Создание тестовых категорий
INSERT INTO categories (id, name) VALUES 
(1, 'Technology'),
(2, 'Sports'),
(3, 'Politics'),
(4, 'Entertainment');

-- Создание тестовых новостей
INSERT INTO news (id, title, description, content, category_id, user_id, created_at) VALUES 
(1, 'New Technology Breakthrough', 'A major breakthrough in technology', 'This is the content of the technology news...', 1, 1, NOW()),
(2, 'Sports Update', 'Latest sports news', 'This is the content of the sports news...', 2, 2, NOW()),
(3, 'Political News', 'Important political development', 'This is the content of the political news...', 3, 3, NOW());

-- Создание тестовых комментариев
INSERT INTO comments (id, content, user_id, news_id, created_at) VALUES 
(1, 'Great article!', 2, 1, NOW()),
(2, 'Very informative', 3, 1, NOW()),
(3, 'Interesting perspective', 1, 2, NOW());
