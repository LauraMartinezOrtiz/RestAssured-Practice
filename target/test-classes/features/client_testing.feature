@active
Feature: Client testing CRUD

  @smoke
  Scenario: Create a new client
    Given I have a client with the following details:
      | Name  | LastName | Country  | City | Email           | Phone     |
      | Laura | Doe      | Colombia | Cali | laura@email.com | 312345789 |
    When I send a POST request to create a client
    Then the response should have a status code of 201
    And the response should include the details of the created client
    And validates the response with client JSON schema


  @smoke
  Scenario: Change the phone number of the first Client named Laura
    Given there are 3 registered clients in the system
    And find the first client with Name "Laura"
    When I save the current phone number of the client
    And Update the phone number of the client to "3145678"
    Then validates that the new phone number is different