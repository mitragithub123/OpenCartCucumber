Feature: Login data driven with excel

  Scenario Outline: Login Data Driven Excel
    Given the user navigates to login page
    Then the user should be redirected to MyAccount Page by passing email and password with excel row "<rowIndex>"

    Examples: 
      | rowIndex |
      |        1 |
      |        2 |
      |        3 |
      