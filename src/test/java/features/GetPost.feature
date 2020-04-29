Feature:  Validate the Get Post Operation using Rest Assured

  Scenario: Validate the GET Operation
    Given I logged into the application "/login" with following credentials
      | email    | rohitkashyapvatta@mail.com |
      | password | Rehan#2019                 |
    And I read the json file
    And I navigate to the "/posts" location
    Then  the author name "rvatta" is present in the list of authors

  Scenario: Validate the DELETE operation
    Given I perform the post request "/posts/" with following body data
      | id     | 1           |
      | title  | json-server |
      | author | typicode    |
    When I perform the DELETE request "/posts/{id}/" with following data
      | id | 1 |
    And I perform the GET request "/posts/{id}/" with following data
      | id | 1 |
    Then the author name "typicode" is not present in the list of authors

  Scenario: Validate the PUT operation
    Given I perform the post request "/posts/" with following body data
      | id     | 1           |
      | title  | json-server |
      | author | typicode    |
    When I perform the PUT request "/posts/{id}" with following data
      | id     | 1                  |
      | title  | Rohit Test Assured |
      | author | kvatta             |
    And I perform the GET request "/posts/{id}/" with following data
      | id | 1 |
    Then the title "Rohit Test Assured" is present in the list of posts

  Scenario: Validate the GET Operation With Bearer Token
    Given I logged into the application "/login" with following credentials
      | email    | rohitkashyapvatta@mail.com |
      | password | Rehan#2019                 |
    Given I navigate to the "/posts" location
    Then  the author name "rvatta" is present in the list of authors

  Scenario: Validate the GET Operation With Complex Data Object
    Given I logged into the application "/login" with following credentials
      | email    | rohitkashyapvatta@mail.com |
      | password | Rehan#2019                 |
    And I perform the GET request "/location/" with following query data
      | id | 1 |
    Then  the "primary" location with address "1st street" is present

  Scenario: Validate the GET Operation With Json Schema validation
    Given I logged into the application "/login" with following credentials
      | email    | rohitkashyapvatta@mail.com |
      | password | Rehan#2019                 |
    And I perform the GET request "/posts/{id}/" with following data
      | id | 1 | performGetOperation
    Then the json schema should match with the response