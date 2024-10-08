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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Resource steps.
 */
public class ResourceSteps {

    private static final Logger logger = LogManager.getLogger(com.automation.api.stepDefinitions.ResourceSteps.class);
    private final ResourceRequest resourceRequest = new ResourceRequest();

    private Response response;
    private Resource resource;
    private List<Resource> activeList = new ArrayList<>();

    /**
     * There are registered resources in the system.
     *
     * @param resourcesAmount int
     */
    @Given("there are at least {int} resources in the system")
    public void thereAreRegisteredResourcesInTheSystem(int resourcesAmount) {
        iSendAGETRequestToViewAllTheResources();
        generateMoreResources(resourcesAmount);
    }

    /**
     * Generate more resources
     *
     * @param resourcesAmount int
     */
    private void generateMoreResources(int resourcesAmount) {
        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);

        if (resourceList.size() < resourcesAmount) {
            int resourcesToCreate = resourcesAmount - resourceList.size();
            for (int i = 0; i < resourcesToCreate; i++) {
                response = resourceRequest.createDefaultResource();
                logger.info("Create more resources status code: " + response.statusCode());
                Assert.assertEquals(201, response.statusCode());
            }
        }
    }

    /**
     * Have a resource with the following details.
     *
     * @param resourceData Datatable
     */
    @Given("I have a resource with the following details:")
    public void iHaveAResourceWithTheFollowingDetails(DataTable resourceData) {
        Map<String, String> resourceDataMap = resourceData.asMaps()
                .get(0);
        resource = Resource.builder()
                .name(resourceDataMap.get("Name"))
                .trademark(resourceDataMap.get("Trademark"))
                .stock(Integer.parseInt(resourceDataMap.get("Stock")))
                .price(Float.parseFloat(resourceDataMap.get("Price")))
                .description(resourceDataMap.get("Description"))
                .tags(resourceDataMap.get("Tags"))
                .active(Boolean.parseBoolean(resourceDataMap.get("Active")))
                .build();
        logger.info("Resource mapped: " + resource);
    }

    /**
     * Send get request.
     */
    @When("I find all the active resources")
    public void sendGETRequest() {
        iSendAGETRequestToViewAllTheResources();
        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);
        for (Resource resource : resourceList) {
            if (resource.isActive()) {
                activeList.add(resource);
            }
        }
        Assert.assertNotNull(activeList);
    }

    @Then("I update them as inactive")
    public void updateResourcesToInactive() {
        for (Resource resource : activeList) {
            resource.setActive(false);
            response = resourceRequest.updateResource(resource, resource.getId());
            logger.info("Resources updated: " + response.jsonPath()
                    .prettify());
            Assert.assertEquals(200, response.statusCode());
        }
    }

    private void iSendAGETRequestToViewAllTheResources() {
        response = resourceRequest.getResources();
        Assert.assertEquals(200, response.statusCode());
    }

    @When("I find the latest resource")
    public void findLastResource() {
        iSendAGETRequestToViewAllTheResources();
        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);
        resource = resourceList.get(resourceList.size() - 1);
    }

    @When("I update all the parameters of this resource")
    public void iSendAPUTRequestToUpdateTheResource() {
        resource.setName("Gold salmon");
        resource.setTrademark("Trinks");
        resource.setStock(10);
        resource.setPrice(200);
        resource.setDescription("Best salmon");
        resource.setTags("id");
        resource.setActive(false);
        response = resourceRequest.updateResource(resource, resource.getId());
        Assert.assertEquals(200, response.getStatusCode());
    }

    /**
     * Delete all resources.
     */
    @Then("delete all the resources")
    public void deleteAllResources() {
        response = resourceRequest.getResources();
        logger.info("All resources: " + response.jsonPath()
                .prettify());

        List<Resource> resourceList = resourceRequest.getResourcesEntity(response);

        for (Resource resource : resourceList) {
            response = resourceRequest.deleteResource(resource.getId());
        }

        logger.info("All resources are deleted");
        Assert.assertEquals(200, response.statusCode());
    }

    @Then("the resource response should have a status code of {int}")
    public void theResourceResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
    }

    /**
     * User validates response with resource json schema.
     */
    @Then("validates the response with resource JSON schema")
    public void userValidatesResponseWithResourceJSONSchema() {
        String path = "schemas/resource/resourceSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Resource object");
    }

    /**
     * User validates response with resource list json schema.
     */
    @Then("validates the response with resource list JSON schema")
    public void userValidatesResponseWithResourceListJSONSchema() {
        String path = "schemas/resource/resourceListSchema.json";
        Assert.assertTrue(resourceRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Resource List object");
    }
}
