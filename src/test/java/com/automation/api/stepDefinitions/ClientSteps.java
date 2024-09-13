package com.automation.api.stepDefinitions;

import com.automation.api.models.Client;
import com.automation.api.requests.ClientRequest;
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

public class ClientSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);
    private final ClientRequest clientRequest = new ClientRequest();

    public Response response;
    private Client client;
    private String currentPhone;


    @Given("there are {int} registered clients in the system")
    public void thereAreRegisteredClientsInTheSystem(int clientsAmount) {
        response = clientRequest.getClients();
        logger.info(response.jsonPath()
                .prettify());
        Assert.assertEquals(200, response.statusCode());

        List<Client> clientList = clientRequest.getClientsEntity(response);

        if (clientList.size() < clientsAmount) {
            int clientsToCreate = clientsAmount - clientList.size();
            for (int i = 0; i < clientsToCreate; i++) {
                response = clientRequest.createDefaultClient();
                logger.info("Create more clients status code: " + response.statusCode());
                Assert.assertEquals(201, response.statusCode());
            }
        }
    }

    @When("find the first client with Name {string}")
    public void sendGETNameRequest(String clientName) {
        response = clientRequest.getClientByName(clientName);
        Client newClient = null;

        if (response.getStatusCode() == 404) {
            newClient = new Client();
            newClient.setName(clientName);
            newClient.setLastName("Matthews");
            newClient.setCountry("UK");
            newClient.setCity("London");
            newClient.setEmail("mathews12@email.com");
            newClient.setPhone("31832323");

            response = clientRequest.createClient(newClient);
            client = clientRequest.getClientEntity(response);
            logger.info(client);
            Assert.assertEquals(201, response.statusCode());
            return;
        } else {
            List<Client> clientList = clientRequest.getClientsEntity(response);
            for (Client client : clientList) {
                if (client.getName().equalsIgnoreCase(clientName)) {
                    newClient = client;
                    break;
                }
            }
        }

        client = newClient;

        logger.info("Found: " + client);
        logger.info(response.jsonPath()
                .prettify());
    }

    @When("I save the current phone number of the client")
    public void saveCurrentPhoneNumber() {
        currentPhone = client.getPhone();
        logger.info("Current phone saved: " + currentPhone);
    }

    @When("I send a PUT request to update the phone number of the client to {string}")
    public void updatePhoneNumber(String newPhone) {
        client.setPhone(newPhone);
        response = clientRequest.updateClientById(client, client.getId());
        Assert.assertEquals(200, response.statusCode());
        logger.info("Phone updated to: " + client.getPhone());
    }

    @When("validates that the new phone number is different")
    public void checkNewPhoneNumberIsDifferent() {
        Client updatedClient = clientRequest.getClientEntity(response);
        Assert.assertNotEquals(currentPhone, updatedClient.getPhone());
        logger.info("Updated client info:  " + updatedClient);
    }

    @Then("delete all the registered clients")
    public void deleteAllClients() {
        response = clientRequest.getClients();
        logger.info("All clients: " + response.jsonPath()
                .prettify());

        List<Client> clientList = clientRequest.getClientsEntity(response);

        for (Client client : clientList) {
            response = clientRequest.deleteClient(client.getId());
        }

        Assert.assertEquals(200, response.statusCode());
    }

    @Given("I have a client with the following details:")
    public void iHaveAClientWithTheFollowingDetails(DataTable clientData) {
        Map<String, String> clientDataMap = clientData.asMaps()
                .get(0);
        client = Client.builder()
                .name(clientDataMap.get("Name"))
                .lastName(clientDataMap.get("LastName"))
                .country(clientDataMap.get("Country"))
                .city(clientDataMap.get("City"))
                .email(clientDataMap.get("Email"))
                .phone(clientDataMap.get("Phone"))
                .build();
        logger.info("Client mapped: " + client);
        response = clientRequest.createClient(client);
        client = clientRequest.getClientEntity(response);
        Assert.assertEquals(201, response.statusCode());
    }

    @When("I find the new client")
    public void findTheNewClient() {
        response = clientRequest.getClient(client.getId());
        logger.info(response.jsonPath().prettify());
        logger.info("Found Client ID: " + client.getId());
        Assert.assertEquals(200, response.statusCode());
    }

    @When("I send a PUT request to update the name parameter to {string}")
    public void updateClientName(String newName) {
        client.setName(newName);
        response = clientRequest.updateClientById(client, client.getId());
        logger.info("New name: " + client.getName());
        Assert.assertEquals(200, response.statusCode());
    }

    @When("I send a DELETE request to delete the new client")
    public void iSendADELETERequestToDeleteTheClientWithID() {
        response = clientRequest.deleteClient(client.getId());
        Assert.assertEquals(200, response.statusCode());
    }

    @Then("the response should have the following details:")
    public void theResponseShouldHaveTheFollowingDetails(DataTable expectedData) {
        client = clientRequest.getClientEntity(response);
        Map<String, String> expectedDataMap = expectedData.asMaps()
                .get(0);

        Assert.assertEquals(expectedDataMap.get("Name"), client.getName());
        Assert.assertEquals(expectedDataMap.get("LastName"), client.getLastName());
        Assert.assertEquals(expectedDataMap.get("Country"), client.getCountry());
        Assert.assertEquals(expectedDataMap.get("City"), client.getCity());
        Assert.assertEquals(expectedDataMap.get("Email"), client.getEmail());
        Assert.assertEquals(expectedDataMap.get("Phone"), client.getPhone());
        Assert.assertEquals(expectedDataMap.get("Id"), client.getId());
    }

    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
    }

    @Then("validates the response with client JSON schema")
    public void userValidatesResponseWithClientJSONSchema() {
        String path = "schemas/client/clientSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client object");
    }

    @Then("validates the response with client list JSON schema")
    public void userValidatesResponseWithClientListJSONSchema() {
        String path = "schemas/client/clientListSchema.json";
        Assert.assertTrue(clientRequest.validateSchema(response, path));
        logger.info("Successfully Validated schema from Client List object");
    }
}
