package com.automation.api.requests;

import com.automation.api.models.Resource;
import com.automation.api.utils.Constants;
import com.automation.api.utils.JsonFileReader;
import com.google.gson.Gson;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ResourceRequest extends BaseRequest{

    private String endpoint;

    /**
     * Get Resource list
     *
     * @return rest-assured response
     */
    public Response getResources() {
        endpoint = String.format(Constants.URL, Constants.RESOURCES_PATH);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Get resource by id
     *
     * @param resurceId string
     * @return rest-assured response
     */
    public Response getResource(String resurceId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH, resurceId);
        return requestGet(endpoint, createBaseHeaders());
    }

    /**
     * Create resource
     *
     * @param resource model
     * @return rest-assured response
     */
    public Response createResource(Resource resource) {
        endpoint = String.format(Constants.URL, Constants.RESOURCES_PATH);
        return requestPost(endpoint, createBaseHeaders(),resource);
    }

    /**
     * Update resource by id
     *
     * @param resource model
     * @param resourceId string
     * @return rest-assured response
     */
    public Response updateResource(Resource resource, String resourceId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH, resourceId);
        return requestPut(endpoint, createBaseHeaders(), resource);
    }

    /**
     * Delete resource by id
     *
     * @param resourceId string
     * @return rest-assured response
     */
    public Response deleteResource(String resourceId) {
        endpoint = String.format(Constants.URL_WITH_PARAM, Constants.RESOURCES_PATH, resourceId);
        return requestDelete(endpoint, createBaseHeaders());
    }

    public Resource getResourceEntity(@NotNull Response response) {
        return response.as(Resource.class);
    }

    public List<Resource> getResourcesEntity(@NotNull Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath.getList("", Resource.class);
    }

    public Response createDefaultResource() {
        JsonFileReader jsonFile = new JsonFileReader();
        return this.createResource(jsonFile.getResourceByJson(Constants.DEFAULT_RESOURCE_FILE_PATH));
    }

    public Resource getResourceEntity(String resourceJson) {
        Gson gson = new Gson();
        return gson.fromJson(resourceJson, Resource.class);
    }

}
