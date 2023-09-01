package com.library.steps;

import com.library.utility.ConfigurationReader;
import com.library.utility.LibraryAPI_TestBase;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.lang.ref.ReferenceQueue;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class US01_GETusers {
    static String token;
    static Response response;
    public static ResponseSpecification resSpec;

    JsonPath jsonPath;
  static  Map<String, String> BodyMap;

    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String user) {
        token = LibraryAPI_Util.getToken(user);


    }

    @Given("Accept header is {string}")
    public void accept_header_is(String acceptHeader) {
        given().accept(acceptHeader);

    }

    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {

           response= given().header("x-library-token", token)
                    .when().get(endpoint);


    }

    @Then("status code should be {int}")
    public void status_code_should_be(int statusCode) {
        if (response == null){
            response = US03.response;
        }
        assertEquals(statusCode,response.getStatusCode());

    }

    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        assertEquals(contentType, response.contentType());

    }

    @Then("{string} field should not be null")
    public void field_should_not_be_null(String field) {
        assertTrue(response.getHeader(field) !="NULL");


    }

    public static Response addRandomBookFunctionHandler(String endpoint) {
        if (endpoint.contains("user")) {
            System.out.println(token);
            response = given().spec(US03.reqSpec).header("x-library-token", token)
                    .post(endpoint);
            response.prettyPrint();
            return response;

        } else if (endpoint.contains("decode")) {
            response = given().spec(US05.reqSpec)
                    .post(endpoint);
            return response;
        } else {
            response = given().spec(US03.reqSpec).header("x-library-token", token)
                    .post(endpoint);
            return response;


        }
    }
}
/*
if (endpoint.contains("{")) {
            response = LibraryAPI_Util.idPathParamHandler(US02.pathParam)
                    .accept(ContentType.JSON)
                    .header("x-library-token", token).when()
                    .get(endpoint);
            jsonPath= response.jsonPath();
            BodyMap=jsonPath.getMap("");

        } else  {
 */

