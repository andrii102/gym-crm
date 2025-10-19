Feature: Trainee Profile Management
  As a user
  I want to view, update, and manage trainee profiles
  So I can interact with gym system

  @PositiveCase
  Scenario: Successfully retrieve a trainee's profile
    Given a trainee with username "john.doe" exists
    When a user sends a GET request to "/api/trainees/john.doe"
    Then the trainee profile response status should be 200

  @NegativeCase
  Scenario: Attempt to retrieve a non-existent trainee's profile
    Given a trainee with username "not.found" does not exist
    When a user sends a GET request to "/api/trainees/not.found"
    Then the trainee profile response status should be 404

  @PositiveCase
  Scenario: Successfully update trainee's profile
    Given the trainee service will successfully update the profile for "john.doe"
    When a user sends a PUT request to "/api/trainees/john.doe" with:
      | username  | john.doe     |
      | firstName | Updated      |
      | lastName  | Name         |
      | address   | 456 New St   |
      | active  | true           |
    Then the trainee profile response status should be 200
    And the response body should contain the trainee's first name "Updated"

  @NegativeCase
  Scenario: Attempt to update a trainee's profile with invalid data
    When a user sends a PUT request to "/api/trainees/john.doe" with:
      | firstName | "" |
    Then the trainee profile response status should be 400

  @PositiveCase
  Scenario: Successfully delete a trainee
    Given the trainee service is ready to delete "john.doe"
    When a user sends a DELETE request to "/api/trainees/john.doe"
    Then the trainee profile response status should be 200

  @PositiveCase
  Scenario: Successfully activate a trainee
    Given the trainee service is ready to update activation status for "john.doe"
    When a user sends a PATCH request to "/api/trainees/john.doe/activation" with status "true"
    Then the trainee profile response status should be 200
    And the trainee service activation method should be called with "true"

  @NegativeCase
  Scenario: Attempt to activate a non-existent trainee
    Given the trainee service will not find "not.found"
    When a user sends a PATCH request to "/api/trainees/not.found/activation" with status "true"
    Then the trainee profile response status should be 404