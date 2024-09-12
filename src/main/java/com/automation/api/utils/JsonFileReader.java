package com.automation.api.utils;

import com.automation.api.models.Resource;
import com.google.gson.Gson;
import com.automation.api.models.Client;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class JsonFileReader {
    /**
     * This method read a JSON file and deserialize the body into a Client object
     *
     * @param jsonFileName json file location path
     *
     * @return Client : client
     */
    public Client getClientByJson(String jsonFileName) {
        Client client = new Client();
        try (Reader reader = new FileReader(jsonFileName)) {
            Gson gson = new Gson();
            client = gson.fromJson(reader, Client.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return client;
    }

    /**
     * This method read a JSON file and deserialize the body into a Client object
     *
     * @param jsonFileName json file location path
     *
     * @return Client : client
     */
    public Resource getResourceByJson(String jsonFileName) {
        Resource resource = new Resource();
        try (Reader reader = new FileReader(jsonFileName)) {
            Gson gson = new Gson();
            resource = gson.fromJson(reader, Resource.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resource;
    }
}