package com.automation.api.stepDefinitions;

import com.automation.api.models.Client;
import com.automation.api.requests.ClientRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;

public class UpdateClientPhoneNumberSteps {
    private static final Logger logger = LogManager.getLogger(ClientSteps.class);
    private final ClientRequest clientRequest = new ClientRequest();

    private Response response;
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
                logger.info(response.statusCode());
                Assert.assertEquals(201, response.statusCode());
            }
        }
    }


    @When("find the first client with Name {string}")
    public void sendGETNameRequest(String clientName) {
        response = clientRequest.getClientByName(clientName);
        List<Client> clientList = clientRequest.getClientsEntity(response);

        Client firstClientWithName = null;

        for (Client client : clientList) {
            if (clientName.equalsIgnoreCase(client.getName())) {
                firstClientWithName = client;
                break;
            }
        }

        if (firstClientWithName == null) {
            Client newClient = new Client();
            newClient.setName(clientName);
            newClient.setLastName("Matthews");
            newClient.setCountry("UK");
            newClient.setCity("London");
            newClient.setEmail("mathews12@email.com");
            newClient.setPhone("31832323");
            response = clientRequest.createClient(newClient);
            Assert.assertEquals(201, response.statusCode());
            client = clientRequest.getClientEntity(response);
            return;
        }

        client = firstClientWithName;

        logger.info(firstClientWithName);

        logger.info(response.jsonPath()
                .prettify());
        logger.info("The first client is: " + firstClientWithName);
    }

    @When("I save the current phone number of the client")
    public void saveCurrentPhoneNumber() {
        currentPhone = client.getPhone();
        logger.info(currentPhone + " Saved");
    }

    @When("Update the phone number of the client to {string}")
    public void updatePhoneNumber(String newPhone) {
        client.setPhone(newPhone);
        response = clientRequest.updateClientById(client, client.getId());
        logger.info(client.getPhone());
    }

    @Then("validates that the new phone number is different")
    public void theNewPhoneNumberIsDifferent() {
        Client updatedClient = clientRequest.getClientEntity(response);
        Assert.assertNotEquals(currentPhone, updatedClient.getPhone());
        logger.info(updatedClient);
    }


}
