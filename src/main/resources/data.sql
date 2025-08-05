-- Insert into users
INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (1, 'John', 'Doe', 'john.doe', '$2a$12$K6ba191yQ/dIxUkk2jkxuOkE/p8GXcwHQmngLM1hahT6ag8dvf.Ae', TRUE) ON CONFLICT (id) DO NOTHING;    -- pass123
INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (2, 'Jane', 'Smith', 'jane.smith', '$2a$12$VwAJCap5EXK1GWYTc82Ctuhv2SUqtNRxd.wRt4jDnaU36VifNIkTy', TRUE) ON CONFLICT (id) DO NOTHING;    -- pass456
INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (3, 'Mike', 'Brown', 'mike.brown', '$2a$12$h8Qi3AxlSl3fZ.CmRvpvfux.Qv21doYN8HvXvD3a4mTHFgNVg59t.', TRUE) ON CONFLICT (id) DO NOTHING;    --pass789
INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (4, 'Emma', 'White', 'emma.white', '$2a$12$SFPGKmJ/YXobesc4hDjb/.cHt9cWNg8/InRMScuDWzxEVxyfAkybO', TRUE) ON CONFLICT (id) DO NOTHING;    --pass321
INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (5, 'Jessica', 'Black', 'jessica.black', '$2a$12$7N.9t0j3XgH/wWbpqWmBPeLvHne7vAr.jrcO9a5hLLvOgBXuYml0u', TRUE) ON CONFLICT (id) DO NOTHING;  --pass654
INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (6, 'Jacob', 'Green', 'jacob.green', '$2a$12$TLay2hlDPV/F2n4bStJml.bGZrN8Qws1V3jXNpc.G/UkXeYkoe6Iy', TRUE) ON CONFLICT (id) DO NOTHING;  --pass987

-- Insert into training_type
INSERT INTO training_type (id, training_type_name) VALUES (1, 'Yoga') ON CONFLICT (id) DO NOTHING;
INSERT INTO training_type (id, training_type_name) VALUES (2, 'Cardio') ON CONFLICT (id) DO NOTHING;
INSERT INTO training_type (id, training_type_name) VALUES (3, 'Strength') ON CONFLICT (id) DO NOTHING;

-- Insert into trainer
INSERT INTO trainer (id, training_type, user_id) VALUES (1, 1, 1) ON CONFLICT (id) DO NOTHING;
INSERT INTO trainer (id, training_type, user_id) VALUES (2, 2, 2) ON CONFLICT (id) DO NOTHING;
INSERT INTO trainer (id, training_type, user_id) VALUES (3, 3, 3) ON CONFLICT (id) DO NOTHING;
INSERT INTO trainer (id, training_type, user_id) VALUES (4, 3, 4) ON CONFLICT (id) DO NOTHING;

-- Insert into trainee
INSERT INTO trainee (id, date_of_birth, address, user_id)
VALUES (1, DATE '2000-05-15', '123 Main St', 5) ON CONFLICT (id) DO NOTHING;
INSERT INTO trainee (id, date_of_birth, address, user_id)
VALUES (2, DATE '1998-11-22', '456 Oak Ave', 6) ON CONFLICT (id) DO NOTHING;

-- Insert into training
INSERT INTO training (id, trainee_id, trainer_id, training_name, training_type, training_date, training_duration)
VALUES (1, 1, 1, 'Morning Yoga', 1, DATE '2025-06-01', 60) ON CONFLICT (id) DO NOTHING;
INSERT INTO training (id, trainee_id, trainer_id, training_name, training_type, training_date, training_duration)
VALUES (2, 2, 2, 'Evening Cardio', 2, DATE '2025-06-02', 45) ON CONFLICT (id) DO NOTHING;
INSERT INTO training (id, trainee_id, trainer_id, training_name, training_type, training_date, training_duration)
VALUES (3, 2, 3, 'Strength Training', 3, DATE '2025-06-03', 90) ON CONFLICT (id) DO NOTHING;

-- Insert into join table trainee_trainer
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (1, 1) ON CONFLICT DO NOTHING;
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (2, 2) ON CONFLICT DO NOTHING;
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (2, 3) ON CONFLICT DO NOTHING;

-- Sync sequence values with max existing IDs
SELECT setval('users_seq', (SELECT COALESCE(MAX(id), 1) FROM users));
SELECT setval('training_type_seq', (SELECT COALESCE(MAX(id), 1) FROM training_type));
SELECT setval('trainer_seq', (SELECT COALESCE(MAX(id), 1) FROM trainer));
SELECT setval('trainee_seq', (SELECT COALESCE(MAX(id), 1) FROM trainee));
SELECT setval('training_seq', (SELECT COALESCE(MAX(id), 1) FROM training));