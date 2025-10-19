Feature: Trainer Profile Management
  As a user
  I want to view, update, and manage trainer profiles.

  @PositiveCase
  Scenario: Successfully retrieve a trainer's profile
    Given a trainer with username "john.doe" exists
    When a user  sends a GET request to "/api/trainers/john.doe"
    Then the trainer profile response status should be 200
    And the response body should contain the trainer's first name "John"

  @PositiveCase
  Scenario: Successfully update a trainer's profile
    Given the trainer service will successfully update the profile for "john.doe"
    When a user sends a PUT request to "/api/trainers/john.doe" with valid data:
      | username       | john.doe |
      | firstName      | Updated  |
      | lastName       | Name     |
      | trainingType   | Yoga     |
      | active         | true     |
    Then the trainer profile response status should be 200
    And the response body should contain the trainer's first name "Updated"

  @NegativeCase
  Scenario: Attempt to update a trainer's profile with invalid data
    When a user sends a PUT request to "/api/trainers/john.doe" with:
      | firstName | " " |

    Then the trainee profile response status should be 400

  @PositiveCase
  Scenario: Successfully activate a trainer
    Given the trainer service is ready to update activation status for "john.doe"
    When a user sends a PATCH request to "/api/trainers/john.doe/activation" with status "true"
    Then the trainee profile response status should be 200
    And the trainer service activation method should be called with "true"
