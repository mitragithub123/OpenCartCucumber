Feature: Account registration

  @regression
  Scenario: Successful Account registration
    Given the user navigates to Register account page
    When user enters the details into below fields
      | firstName | John       |
      | lastName  | Walker     |
      | telephone | 1234567890 |
      | password  | test@123   |
    And the user selects privacy policy
    And the user clicks on continue button
    Then the user account should created successfully
