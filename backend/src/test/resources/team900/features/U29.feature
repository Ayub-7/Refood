Feature: U29 - Browse/search sale listings

  Scenario: User successfully searches for sale listings
    Given there are sale listings available
    When the user makes a request to look at current listings
    Then the user successfully retrieves them
