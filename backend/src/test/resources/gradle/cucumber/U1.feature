Feature: U1 Registering and logging into an individual account
  Scenario: AC1 If I am not already logged in, I can log in or register.
    Given I have an account and I want to log in.
    When I log in as "omar@gmail.com" with the correct password.
    Then I log in successfully.

  Scenario: AC1 If I am not already logged in, I can log in or register.
    Given I do not have an account.
    When I register with my full name "Omar Sheta", aged 23, using the email "notomar@gmail.com", with the password "Potato1!", using the address "420 Main South Road, Christchurch, New Zealand"
    Then I register successfully and will obtain a token session as a user.