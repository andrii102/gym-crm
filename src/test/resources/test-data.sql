-- Insert into users
INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (1, 'John', 'Doe', 'john.doe', 'pass123', TRUE);
INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (2, 'Jane', 'Smith', 'jane.smith', 'pass456', TRUE);
INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (3, 'Mike', 'Brown', 'mike.brown', 'pass789', TRUE);
INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (4, 'Emma', 'White', 'emma.white', 'pass321', TRUE);
INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (5, 'Jessica', 'Black', 'jessica.black', 'pass654', TRUE);
INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (6, 'Jacob', 'Green', 'jacob.green', 'pass987', TRUE);

-- Insert into training_type
INSERT INTO training_type (id, training_type_name) VALUES (1, 'Yoga');
INSERT INTO training_type (id, training_type_name) VALUES (2, 'Cardio');
INSERT INTO training_type (id, training_type_name) VALUES (3, 'Strength');

-- Insert into trainer
INSERT INTO trainer (id, training_type, user_id) VALUES (1, 1, 1);
INSERT INTO trainer (id, training_type, user_id) VALUES (2, 2, 2);
INSERT INTO trainer (id, training_type, user_id) VALUES (3, 3, 3);
INSERT INTO trainer (id, training_type, user_id) VALUES (4, 3, 4);

-- Insert into trainee
INSERT INTO trainee (id, date_of_birth, address, user_id)
VALUES (1, DATE '2000-05-15', '123 Main St', 5);
INSERT INTO trainee (id, date_of_birth, address, user_id)
VALUES (2, DATE '1998-11-22', '456 Oak Ave', 6);

-- Insert into training
INSERT INTO training (id, trainee_id, trainer_id, training_name, training_type, training_date, training_duration)
VALUES (1, 1, 1, 'Morning Yoga', 1, DATE '2025-06-01', 60);
INSERT INTO training (id, trainee_id, trainer_id, training_name, training_type, training_date, training_duration)
VALUES (2, 2, 2, 'Evening Cardio', 2, DATE '2025-06-02', 45);
INSERT INTO training (id, trainee_id, trainer_id, training_name, training_type, training_date, training_duration)
VALUES (3, 2, 3, 'Strength Training', 3, DATE '2025-06-03', 90);

-- Insert into join table trainee_trainer
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (1, 1);
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (2, 2);
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (2, 3);

-- Sync sequence values
ALTER SEQUENCE users_seq RESTART WITH 100;
ALTER SEQUENCE training_type_seq RESTART WITH 100;
ALTER SEQUENCE trainer_seq RESTART WITH 100;
ALTER SEQUENCE trainee_seq RESTART WITH 100;
ALTER SEQUENCE training_seq RESTART WITH 100;