package com.automation.api.requests;

import com.automation.api.models.Client;
import com.automation.api.utils.Constants;
import com.automation.api.utils.JsonFileReader;
import com.google.gson.Gson;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * The type Client request.
 */
public class ClientRequest extends BaseRequest {
    private String endpoint;

    /**
     * Get Client list
     *
     * @return rest-assured response
     */
    public Response getClients() {
        endpoint = String.format(Constants.URL, Constants.CLIENTS_PATH);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Get client by id
     *
     * @param clientId string
     * @return rest-assured response
     */
    public Response getClient(String clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestGet(endpoint, createBaseHeaders());
    }


    public Response getClientByName(String clientName) {
        String encodedClientName = URLEncoder.encode(clientName, StandardCharsets.UTF_8);
        endpoint = String.format(Constants.URL_WITH_NAME_PARAM, Constants.CLIENTS_PATH, encodedClientName);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Create client
     *
     * @param client model
     * @return rest-assured response
     */
    public Response createClient(Client client) {
        endpoint = String.format(Constants.URL, Constants.CLIENTS_PATH);
        return requestPost(endpoint, createBaseHeaders(), client);
    }

    /**
     * Update client by id
     *
     * @param client   model
     * @param clientId string
     * @return rest-assured response
     */
    public Response updateClientById(Client client, String clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestPut(endpoint, createBaseHeaders(), client);
    }

    /**
     * Delete client by id
     *
     * @param clientId string
     * @return rest-assured response
     */
    public Response deleteClient(String clientId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.CLIENTS_PATH, clientId);
        return requestDelete(endpoint, createBaseHeaders());
    }

    /**
     * Gets client entity.
     *
     * @param response rest-assured response
     * @return the client model
     */
    public Client getClientEntity(@NotNull Response response) {
        return response.as(Client.class);
    }

    /**
     * Gets clients entity.
     *
     * @param response rest-assured response
     * @return list of clients
     */
    public List<Client> getClientsEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Client.class);
    }

    public Response createDefaultClient() {
        JsonFileReader jsonFile = new JsonFileReader();
        return this.createClient(jsonFile.getClientByJson(Constants.DEFAULT_CLIENT_FILE_PATH));
    }

    public Client getClientEntity(String clientJson) {
        Gson gson = new Gson();
        return gson.fromJson(clientJson, Client.class);
    }
}
