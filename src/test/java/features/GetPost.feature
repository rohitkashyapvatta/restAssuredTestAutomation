Feature:
  Validate the Get Post Operation using Rest Assured

  Scenario: Validate the GET Operation
    Given I navigate to the "/posts" location
    Then  the author name "typicode" is present in the list of authors

