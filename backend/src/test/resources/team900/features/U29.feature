Feature: U29 - Browse/search sale listings

  Scenario: User successfully searches for sale listings
    Given there are sale listings available
    When the user makes a request to look at current listings
    Then the user successfully retrieves them


  Scenario: U6 User can type all of a product name
    Given there are products with name "Food" and "Ford"
    When the user searches for a product with name "Food"
    Then only the product with name "Food" is in the search result


  Scenario: U6 User can type part of a product name
    Given there are products with name "Food" and "Ford"
    When the user searches for a product with name "Fo"
    Then products with name "Food" and "Ford" is in the search result

