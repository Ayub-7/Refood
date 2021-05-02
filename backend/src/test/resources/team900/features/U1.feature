Feature: U1 Registering and logging into an individual account
  Scenario: Successful login
    Given User attempts to login
    When They enter with email "johnsmith@yahoo.com" and password "Potato1!"
    Then They are redirected to their profile

  Scenario: Invalid email format inputted
    Given User attempts to login
    When They enter an email with an invalid format, such that "bababooey@@somemail..com"
    Then They are given a warning that their email format is invalid

  Scenario: Non-existent email
    Given User attempts to login
    When They enter an email with the valid format "ghostman@gmail.com" and password "HelpMe69!"
    Then They are given a warning that either their password or email is incorrect

  Scenario: Incorrect password
    Given User attempts to login
    When They enter an email with the valid format "johnsmith@yahoo.com" and password "Ligma420!"
    Then They are given a warning that either their password or email is incorrect

  Scenario: Email is blank
    Given User attempts to login
    When They enter only the password "UserIsDead420!"
    Then They are given a warning that the email field is empty

  Scenario: Password is blank
    Given User attempts to login
    When They enter only the email "johnsmith@yahoo.com"
    Then They are given a warning that the password field is empty

  Scenario: Both email and password are blank
    Given User attempts to login
    When They enter nothing, but attempt to logging anyway
    Then They are given a warning that both email and password fields are empty

  Scenario: Successful Registration
    Given User attempts to register
    When They input the firstname "Jeff", lastname "Goldstein", email "jeffgold@hotmail.com", DoB "19/04/1998", address "20 Waimairi Road, Christchurch, New Zealand"
    Then They successfully register and are redirected to their homepage

  Scenario: Registration with a taken email
    Given User attempts to register with an email that is already registered
    When They input the firstname "Jeff", lastname "Goldstein", email "jeffgold@hotmail.com", DoB "19/04/1998", and address "20 Waimairi Road, Christchurch, New Zealand"
    Then They are notified that the email they tried to register with is already registered

  Scenario: Registration with no names
    Given User attempts to register
    When They input the firstname "", lastname "", email "jeffgold@hotmail.com", DoB "19/04/1998", and address "20 Waimairi Road, Christchurch, New Zealand"
    Then They are notified that they need to enter firstname and lastname as they are mandatory fields

  Scenario: Registration with an invalid email
    Given User attempts to register
    When They input the firstname "Kyle", lastname "Jonathan", email "kyle@@b0..com", DoB "19/04/1998", and address "20 Waimairi Road, Christchurch, New Zealand"
    Then They are notified that they need to enter a valid email

  Scenario: Registration with a blank email
    Given User attempts to register
    When They input the firstname "Kyle", lastname "Jonathan", email "", DoB "19/04/1998", and address "20 Waimairi Road, Christchurch, New Zealand"
    Then They are notified that they need to enter a valid email

  Scenario: Underage registration
    Given User attempts to register
    When They input the firstname "Jeff", lastname "Goldstein", email "jeffgold@hotmail.com", DoB "19/04/2018", and address "20 Waimairi Road, Christchurch, New Zealand"
    Then They are notified that the email they need to be at least 13 years old to register

  Scenario: Password storage
    Given User attempts to register
    When They successfully register
    Then Password is hashed and not stored in plain text

  Scenario: Logging out
    Given User is logged in
    When They press log out
    Then They successfully logout and their token session disappears
