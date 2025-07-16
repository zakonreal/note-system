-- Пароль: admin
INSERT INTO public.users (username, password, role)
VALUES ('admin', '$2a$10$Zcw9T7uOqREW6xXG1tGmqung5IAkqXSqmhRQZ2hfRBjBVuoCgjrQ.', 'ADMIN');

-- Пароль: password
INSERT INTO public.users (username, password, role)
VALUES ('user1', '$2a$10$z.eTt7t.g7L7lvCDCEuCNuuy6dRuKdcv01vVC5YhVj6nGet1H.VNa', 'USER');

INSERT INTO public.notes (user_id, title, content, created_date, completed) VALUES
(1, 'Первая заметка админа', 'Это тестовая заметка администратора', CURRENT_DATE, true),
(2, 'Первая заметка пользователя', 'Это тестовая заметка обычного пользователя', CURRENT_DATE, false);