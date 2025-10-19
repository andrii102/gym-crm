Feature: Trainings Management
  As a user
  I want to create trainings and view training types.

  @PositiveCase
  Scenario: Successfully retrieve all training types
    Given the training service has a list of training types
    When a user sends a GET request to "/api/trainings/training-types" for trainings
    Then the training response status should be 200
    And the response body should contain a list of training types

  @PositiveCase
  Scenario: Successfully create a new training
    Given the training service is ready to create a new training
    When a user sends a POST request to "/api/trainings" with training data:
      | traineeUsername | test.trainee |
      | trainerUsername | test.trainer |
      | trainingName    | Cardio       |
      | trainingDate    | 2025-12-01   |
      | trainingDuration| 60             |
    Then the training response status should be 200
    And the training service create method should be called

  @NegativeCase
  Scenario Outline: Attempt to create a training with invalid data
    When a user sends a POST request to "/api/trainings" with training data:
      | traineeUsername | <traineeUsername> |
      | trainerUsername | <trainerUsername> |
      | trainingName    | <trainingName>    |
    Then the training response status should be 400

    Examples:
      | traineeUsername | trainerUsername | trainingName |
      |                 | test.trainer    | Cardio       |
      | test.trainee    |                 | Cardio       |