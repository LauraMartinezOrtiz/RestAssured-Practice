@active
Feature: Client testing CRUD

  @smoke
  Scenario: Change the phone number of the first Client named Laura
    Given there are 10 registered clients in the system
    And find the first client with Name "Laura"
    When I save the current phone number of the client
    And I send a PUT request to update the phone number of the client to "11111111"
    And validates that the new phone number is different
    Then delete all the registered clients
    And the response should have a status code of 200

  @smoke
  Scenario: Update and delete a New Client
    Given I have a client with the following details:
      | Name | LastName | Country  | City | Email          | Phone     |
      | Juan | Test     | Colombia | Cali | juan@email.com | 232435656 |
    And validates the response with client JSON schema
    When I find the new client
    And I send a PUT request to update the name parameter to "Luis"
    And the response should have a status code of 200
    And I send a DELETE request to delete the new client
    Then the response should have a status code of 200

