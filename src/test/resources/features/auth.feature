Feature: Authentication flow
  As a system user
  I want to log in with my credentials
  So i can access secure endpoints

  @PositiveCase
  Scenario: A user logs in with valid credentials
    Given a registered user "john.doe" exists with password "pass123"
    When the user sends a POST request to "/api/auth/login" with username "john.doe" and password "pass123"
    Then the server response status should be 200
    And the response body should contain a JWT token
    And the refresh token should be in the cookie

  @NegativeCase
  Scenario: A user logs in with an invalid password
    Given a registered user "john.doe" exists with password "pass123"
    When the user sends a POST request to "/api/auth/login" with username "john.doe" and password "wrong_password"
    Then the server response status should be 401

  @NegativeCase
  Scenario: A non-existent user tries to log in
    Given no user with username "john.doe" exists
    When the user sends a POST request to "/api/auth/login" with username "ghost.user" and password "any_password"
    Then the server response status should be 401