Feature: Registration
  As a new user
  I want to register for an account
  So I can access the gym services.

  @PositiveCase
  @registration
  Scenario: A new user successfully registers as a trainee
    Given the trainee service is ready to register a new user
    When a user sends a POST request to "/api/auth/trainee" with:
      | firstName   | "Test"     |
      | lastName    | "Trainee"  |
      | address     | "123 Main St" |
      | dateOfBirth | 2025-01-01 |
    Then the server response status of registration should be 201
    And the response body should contain the new username and password

  @PositiveCase
  @registration
  Scenario: A new user successfully registers as a trainer
    Given the trainer service is ready to register a new user
    When a user sends a POST request to "/api/auth/trainer" with:
      | firstName        | "Test"    |
      | lastName         | "Trainer" |
      | specializationId | 1         |
    Then the server response status of registration should be 201
    And the response body should contain the new username and password

  @NegativeCase
  @registration
  Scenario Outline: Attempts to register a trainee with missing required fields
    When a user sends a POST request to "/api/auth/trainee" with:
      | firstName   | <firstName>   |
      | lastName    | <lastName>    |
      | address     | <address>     |
      | dateOfBirth | <dateOfBirth> |
    Then the server response status of registration should be 400

    Examples:
      | firstName | lastName | address         | dateOfBirth  | Description     |
      |         | "User"   | "123 Main St"   | 2000-01-01 | Missing first name  |
      | "Test"    |        | "123 Main St"   | 2000-01-01 | Missing last name   |

  @NegativeCase
  @registration
  Scenario Outline: Attempts to register a trainee with missing required fields
    When a user sends a POST request to "/api/auth/trainer" with:
      | firstName        | <firstName>   |
      | lastName         | <lastName>    |
      | specializationId | <specializationId> |
    Then the server response status of registration should be 400

    Examples:
      | firstName | lastName | specializationId | Description         |
      |            | "User"  | 1                | Missing first name  |
      | "Test"    |          | 1                | Missing last name   |
      | "Test"    |  "User"  |                  | Missing specializationId |
