Feature:
  Validate the Get Post Operation using Rest Assured

  Scenario: Validate the GET Operation
    Given I navigate to the "/posts" location
    Then  the author name "rvatta" is present in the list of authors

  Scenario: Validate the POST Operation
    Given I perform the post request "/posts/{profileNo}/profile" with following data
      | name      | Sams |
      | profileNo | 2    |
    Then the response body should have name as "Sams"

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
