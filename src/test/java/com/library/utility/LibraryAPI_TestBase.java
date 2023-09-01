package com.library.utility;

import io.cucumber.java.BeforeAll;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public abstract class LibraryAPI_TestBase {



    @BeforeAll
    public static void init(){
        RestAssured.baseURI="https://library2.cydeo.com/rest/v1";
    }

}
