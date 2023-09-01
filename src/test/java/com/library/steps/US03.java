package com.library.steps;

import com.library.pages.BasePage;
import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.ConfigurationReader;
import com.library.utility.DB_Util;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.Select;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class US03 {

    static RequestSpecification reqSpec;

    static Response response;

    static JsonPath jsonPath;
    Map<String,String> randomBookLoad;
    static Map<String,Object> randomUserLoad;
    Map<String,Object> expectedBookInfo;
    LoginPage loginPage = new LoginPage();
    BasePage basePage = new BookPage();
    BookPage bookPage = new BookPage();
    static String actualID;
    static String fullName;
    static String email;
    public static Map<String, Object> apiGetBookwIdResponse;
    public static Map<String, Object> dbGetBookwId;
    public static Map<String,Object> userMap;




    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String contentHeader) {

        given().header("Content-Type", contentHeader);
    }
    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String payload) {

   switch(payload){
       case "book":
           randomBookLoad= LibraryAPI_Util.getRandomBookMap();
           reqSpec= given().formParams(randomBookLoad).log().all();
           break;

       case "user":
           randomUserLoad= LibraryAPI_Util.getRandomUserMap();
           reqSpec= given().formParams(randomUserLoad).log().all();
           break;
   }

    }

    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endpoint) {
        if (endpoint.contains("decode")) {
            response = given().spec(US05.reqSpec)
                    .post(endpoint);
        }else {
            response = US01_GETusers.addRandomBookFunctionHandler(endpoint);
        }

    }
    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String pathName, String pathValue) {
 if(pathValue.contains("user")){
     jsonPath= response.jsonPath();
     actualID= jsonPath.getString("user_id");
     userMap= new LinkedHashMap<>();
     userMap.put("email",randomUserLoad.get("email"));
     userMap.put("full_name",randomUserLoad.get("full_name"));
     userMap.put("id",actualID);
 } else {
     jsonPath= response.jsonPath();
     actualID= jsonPath.getString("book_id");
     String actualMessageField= jsonPath.getString(pathName);
     assertEquals(pathValue,actualMessageField);
 }


    }


    @Given("I logged in Library UI as {string}")
    public void i_logged_in_library_ui_as(String role) {
      loginPage.login(role);
    }
    @Given("I navigate to {string} page")
    public void i_navigate_to_page(String page) {
        BasePage.navigateModule(page);
    }


    @Then("UI, Database and API created book information must match")
    public void ui_database_and_api_created_book_information_must_match() {
String query="SELECT NAME, ISBN, YEAR, AUTHOR, BOOK_CATEGORY_ID FROM books WHERE ID ="+ response.path("book_id");

            DB_Util.runQuery(query);
List<String> expectedBook= new ArrayList<>();
expectedBook.add((String)expectedBookInfo.get("name"));
        expectedBook.add((String)expectedBookInfo.get("isbn"));
        expectedBook.add((String)expectedBookInfo.get("year"));
        expectedBook.add((String)expectedBookInfo.get("author"));
        expectedBook.add((String)expectedBookInfo.get("book_category_id"));


       List<String> DB_BookInfo= DB_Util.getRowDataAsList(1);

        assertEquals(expectedBook, DB_BookInfo);
        BrowserUtil.waitForClickablility(bookPage.search, 5).sendKeys((String)expectedBookInfo.get("name")+Keys.ENTER);


       List<String> UI_BookInfo= new ArrayList<>();
        String isbn=bookPage.isbn.getAttribute("value");
        String year= bookPage.year.getAttribute("value");
        String author= bookPage.author.getAttribute("value");
        Select select = new Select(bookPage.categoryDropdown);
        String bookCategoryId= select.getFirstSelectedOption().getText();
       UI_BookInfo.addAll(Arrays.asList((String)expectedBookInfo.get("name"), isbn, year,author,bookCategoryId));
 //  bookPage.editBook((String)expectedBookInfo.get("name")).click();

        assertEquals(expectedBook,UI_BookInfo);










    }

}
