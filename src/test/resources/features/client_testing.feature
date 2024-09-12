@active
Feature: Client testing CRUD

  @smoke
  Scenario: Create a new client
    Given I have a client with the following details:
      | Name | LastName | Country | City | Email           | Phone          | Gender | PhoneNumber  |
      | John | Doe      | USA     | NY   | johnD@gmail.com | (901) 091-2345 | Male   | 123-456-7891 |
    When I send a POST request to create a client
    Then the response should have a status code of 201
    And the response should include the details of the created client
    And validates the response with client JSON schema

  @smoke
  Scenario: Read details of an existing client
    Given there are registered clients in the system
    When I retrieve the details of the client with ID "1"
    Then the response should have a status code of 200
    And the response should have the following details:
      | Name  | LastName | Country | City   | Email                     | Phone          | Gender | PhoneNumber  | Id |
      | Maria | Garcia   | Spain   | Madrid | Gregg_Volkman12@gmail.com | (922) 600-9149 | Female | 789-456-1230 | 1  |
    And validates the response with client JSON schema

  @smoke @regression
  Scenario: View all the clients
    Given there are registered clients in the system
    When I send a GET request to view all the clients
    Then the response should have a status code of 200
    And validates the response with client list JSON schema

  @smoke
  Scenario: Update client details
    Given there are registered clients in the system
    And I retrieve the details of the client with ID "1"
    When I send a PUT request to update the client with ID "1"
    """
    {
      "name": "Maria",
      "lastName": "Gomez",
      "gender": "Female",
      "country": "Spain",
      "city": "Barcelona"
    }
    """
    Then the response should have a status code of 200
    And the response should have the following details:
      | Name  | LastName | Gender | Country | City      | Id |
      | Maria | Gomez    | Female | Spain   | Barcelona | 1  |
    And validates the response with client JSON schema

  @smoke @regression
  Scenario: Delete an existing client
    Given there are registered clients in the system
    When I send a DELETE request to delete the client with ID "1"
    Then the response should have a status code of 200
    And the response should have the following details:
      | Name  | LastName | Gender | Country | City      | Id |
      | Maria | Gomez    | Female | Spain   | Barcelona | 1  |
    And validates the response with client JSON schema

