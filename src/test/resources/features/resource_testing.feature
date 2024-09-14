@active
Feature: Resource testing

  @smoke
  Scenario: Get the list of active resources
    Given there are at least 5 resources in the system
    When I find all the active resources
    Then I update them as inactive
    And validates the response with resource JSON schema
    And the resource response should have a status code of 200
    And delete all the resources

  @smoke
  Scenario: Update the last created resource
    Given there are at least 15 resources in the system
    When I find the latest resource
    And I update all the parameters of this resource
    Then the resource response should have a status code of 200
    And validates the response with resource JSON schema
    And delete all the resources