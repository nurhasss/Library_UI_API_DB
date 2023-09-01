package com.library.steps;

import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class US02 {

    static int pathParam;

Response response;
String actualField;
    @Given("Path param is {string}")
    public void path_param_is(String param) {

   pathParam= Integer.parseInt(param);
    }
    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String fieldToGet) {
         int actualID= Integer.parseInt(US01_GETusers.BodyMap.get(fieldToGet));
        assertEquals(actualID, pathParam);
    }
    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> dataTable) {

        for(String each : dataTable) {
            Assert.assertTrue(US01_GETusers.BodyMap.get(each) !=null);

        }

    }

}
