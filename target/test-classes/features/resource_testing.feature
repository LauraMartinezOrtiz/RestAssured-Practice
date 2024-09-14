@active
Feature: Resource testing

  @smoke
  Scenario: Get the list of active resources
    Given there are at least 5 active resources in the system
    When I find all the active resources
    Then I update them as inactive
    And validates the response with resource list JSON schema


  @smoke
  Scenario: Update the last created resource
    Given there are at least 15 active resources in the system
    When I find the latest resource
    And I update all the parameters of this resource
    Then validates the response with resource JSON schema