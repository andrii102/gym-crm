-- Insert into users
INSERT INTO users ( id, firstName, lastName, username, password, isActive)
VALUES ( 1, 'John', 'Doe', 'john.doe', 'pass123', TRUE);
INSERT INTO users ( id, firstName, lastName, username, password, isActive)
VALUES ( 2, 'Jane', 'Smith', 'jane.smith', 'pass456', TRUE);
INSERT INTO users ( id, firstName, lastName, username, password, isActive)
VALUES ( 3, 'Mike', 'Brown', 'mike.brown', 'pass789', TRUE);
INSERT INTO users ( id, firstName, lastName, username, password, isActive)
VALUES ( 4, 'Emma', 'White', 'emma.white', 'pass321', TRUE);
INSERT INTO users ( id, firstName, lastName, username, password, isActive)
VALUES ( 5, 'Jessica', 'Black', 'jessica.black', 'pass654', TRUE);
INSERT INTO users ( id, firstName, lastName, username, password, isActive)
VALUES ( 6, 'Jacob', 'Green', 'jacob.green', 'pass987', TRUE);

-- Insert into trainingType
INSERT INTO trainingType ( id, trainingTypeName) VALUES ( 1, 'Yoga');
INSERT INTO trainingType ( id, trainingTypeName) VALUES ( 2, 'Cardio');
INSERT INTO trainingType ( id, trainingTypeName) VALUES (3, 'Strength');

-- Insert into trainer
INSERT INTO trainer ( id, training_type, user_id) VALUES ( 1, 1, 1);
INSERT INTO trainer ( id, training_type, user_id) VALUES ( 2, 2, 2);
INSERT INTO trainer ( id, training_type, user_id) VALUES ( 3, 3, 3);
INSERT INTO trainer ( id, training_type, user_id) VALUES ( 4, 3, 4);

-- Insert into trainee
INSERT INTO trainee ( id, dateOfBirth, address, user_id)
VALUES (1 ,DATE '2000-05-15', '123 Main St', 5);
INSERT INTO trainee ( id, dateOfBirth, address, user_id)
VALUES ( 2, DATE '1998-11-22', '456 Oak Ave', 6);

-- Insert into training
INSERT INTO training ( trainee_id, trainer_id, trainingName, training_type, trainingDate, trainingDuration)
VALUES ( 1, 1, 'Morning Yoga', 1, DATE '2025-06-01', 60);
INSERT INTO training ( trainee_id, trainer_id, trainingName, training_type, trainingDate, trainingDuration)
VALUES ( 2, 2, 'Evening Cardio', 2, DATE '2025-06-02', 45);

INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (1, 1);
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (2, 2);