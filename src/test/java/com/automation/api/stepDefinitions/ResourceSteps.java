package com.automation.api.stepDefinitions;

import com.automation.api.models.Resource;
import com.automation.api.requests.ResourceRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

public class ResourceSteps {

    private static final Logger logger = LogManager.getLogger(com.automation.api.stepDefinitions.ResourceSteps.class);
    private final ResourceRequest resourceRequest = new ResourceRequest();

    private Response response;
    private Resource resource;

    @Given("there are registered resources in the system")
    public void thereAreRegisteredResourcesInTheSystem() {
        response = resourceRequest.getResources();
        logger.info(response.jsonPath()
                .prettify());
        Assert.assertEquals(200, response.statusCode());

        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);
        if (resourceList.isEmpty()) {
            response = resourceRequest.createDefaultResource();
            logger.info(response.statusCode());
            Assert.assertEquals(201, response.statusCode());
        }
    }

    @Given("I have a resource with the following details:")
    public void iHaveAResourceWithTheFollowingDetails(DataTable resourceData) {
        Map<String, String> resourceDataMap = resourceData.asMaps()
                .get(0);
        resource = Resource.builder()
                .name(resourceDataMap.get("Name"))
                .trademark(resourceDataMap.get("Trademark"))
                .stock(Integer.parseInt(resourceDataMap.get("Stock")))
                .price(Double.parseDouble(resourceDataMap.get("Price")))
                .description(resourceDataMap.get("Description"))
                .tags(resourceDataMap.get("Tags"))
                .active(Boolean.parseBoolean(resourceDataMap.get("Active")))
                .build();
        logger.info("Resource mapped: " + resource);
    }

    @When("I retrieve the details of the resource with ID {string}")
    public void sendGETRequest(String resourceId) {
        response = resourceRequest.getResource(resourceId);
        logger.info(response.jsonPath()
                .prettify());
        logger.info("The status code is: " + response.statusCode());
    }

    @When("I send a GET request to view all the resources")
    public void iSendAGETRequestToViewAllTheResources() {
        response = resourceRequest.getResources();
    }

    @When("I send a POST request to create a resource")
    public void iSendAPOSTRequestToCreateAResource() {
        response = resourceRequest.createResource(resource);
    }

    @When("I send a DELETE request to delete the resource with ID {string}")
    public void iSendADELETERequestToDeleteTheResourceWithID(String resourceId) {
        response = resourceRequest.deleteResource(resourceId);
    }

    @When("I send a PUT request to update the resource with ID {string}")
    public void iSendAPUTRequestToUpdateTheResourceWithID(String resourceId, String requestBody) {
        resource = resourceRequest.getResourceEntity(requestBody);
        response = resourceRequest.updateResource(resource, resourceId);
    }

   /* @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
    }*/

/*    @Then("the response should have the following details:")
    public void theResponseShouldHaveTheFollowingDetails(DataTable expectedData) {
        resource = resourceRequest.getResourceEntity(response);
        Map<String, String> expectedDataMap = expectedData.asMaps()
                .get(0);

        Assert.assertEquals(expectedDataMap.get("Name"), resource.getName());
        Assert.assertEquals(expectedDataMap.get("Trademark"), resource.getTrademark());
        //Assert.assertEquals(expectedDataMap.get("Stock"), resource.getStock());
        //Assert.assertEquals(expectedDataMap.get("Price"), resource.getPrice());
        Assert.assertEquals(expectedDataMap.get("Description"), resource.getDescription());
        Assert.assertEquals(expectedDataMap.get("Tags"), resource.getTags());
        Assert.assertTrue(expectedDataMap.get("Active"), resource.isActive());
        Assert.assertEquals(expectedDataMap.get("Id"), resource.getId());
    }*/

    @Then("the response should include the details of the created resource")
    public void theResponseShouldIncludeTheDetailsOfTheCreatedResource() {
        Resource new_resource = resourceRequest.getResourceEntity(response);
        new_resource.setId(null);
        Assert.assertEquals(resource, new_resource);
    }

    @Then("validates the response with resource JSON schema")
    public void userValidatesResponseWithResourceJSONSchema() {
        String path = "schemas/resourceSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Resource object");
    }

    @Then("validates the response with resource list JSON schema")
    public void userValidatesResponseWithResourceListJSONSchema() {
        String path = "schemas/resourceListSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Resource List object");
    }
}
