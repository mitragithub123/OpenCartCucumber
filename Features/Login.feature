Feature: Login with valid credentials

  @sanity @regression
  Scenario: Successful login with valid credentials
    Given the user navigates to login page
    When user enters email as "andolasoft.user133@gmail.com" and password as "mitra@1234"
    And the user clicks on login button
    Then the user should redirect to my account page
