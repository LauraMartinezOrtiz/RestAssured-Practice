@active
Feature: Resource testing

  @smoke
  Scenario: Get the list of active resources
    Given there are at least 5 active resources in the system
    When I find all the active resources
    Then I update them as inactive
    And validates the response with resource list JSON schema