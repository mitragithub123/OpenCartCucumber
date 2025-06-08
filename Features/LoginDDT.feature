Feature: Login Data Driven

  Scenario Outline: Login with multiple credentials
    Given the user navigates to login page
    When the user enters "<username>" and "<password>"
    And clicks login button
    Then the user should be redirected to MyAccount Page

    Examples: 
      | username                     | password   | result  |
      | andolasoft.user133@gmail.com | mitra@1234 | valid   |
      | pavanoltraining@gmail.com1   | test@123   | invalid |
      | pavanoltraining@gmail.com2   | test@124   | invalid |
      | pavanoltraining@gmail.com3   | test@125   | invalid |
      | abc123@gmail.com             | test@123   | valid   |
