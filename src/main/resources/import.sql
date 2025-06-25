-- Insert into users
INSERT INTO users ( firstName, lastName, username, password, isActive)
VALUES ( 'John', 'Doe', 'john.doe', 'pass123', TRUE);
INSERT INTO users ( firstName, lastName, username, password, isActive)
VALUES ( 'Jane', 'Smith', 'jane.smith', 'pass456', TRUE);
INSERT INTO users ( firstName, lastName, username, password, isActive)
VALUES ( 'Mike', 'Brown', 'mike.brown', 'pass789', TRUE);
INSERT INTO users ( firstName, lastName, username, password, isActive)
VALUES ( 'Emma', 'White', 'emma.white', 'pass321', TRUE);

-- Insert into trainingType
INSERT INTO trainingType ( trainingTypeName) VALUES ( 'Yoga');
INSERT INTO trainingType ( trainingTypeName) VALUES ( 'Cardio');
INSERT INTO trainingType ( trainingTypeName) VALUES ('Strength');

-- Insert into trainer
INSERT INTO trainer ( training_type, user_id) VALUES ( 1, 1);
INSERT INTO trainer ( training_type, user_id) VALUES ( 2, 2);

-- Insert into trainee
INSERT INTO trainee ( dateOfBirth, address, user_id)
VALUES (DATE '2000-05-15', '123 Main St', 3);
INSERT INTO trainee ( dateOfBirth, address, user_id)
VALUES ( DATE '1998-11-22', '456 Oak Ave', 4);

-- Insert into training
INSERT INTO training ( trainee_id, trainer_id, trainingName, training_type, trainingDate, trainingDuration)
VALUES ( 3, 1, 'Morning Yoga', 1, DATE '2025-06-01', 60);
INSERT INTO training ( trainee_id, trainer_id, trainingName, training_type, trainingDate, trainingDuration)
VALUES ( 4, 2, 'Evening Cardio', 2, DATE '2025-06-02', 45);
