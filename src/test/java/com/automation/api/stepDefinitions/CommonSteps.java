package com.automation.api.stepDefinitions;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import org.junit.Assert;

public class CommonSteps {

    Response response;

    @Then("the response should have a status code of {int}")
    public void theResponseShouldHaveAStatusCodeOf(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
    }
}
