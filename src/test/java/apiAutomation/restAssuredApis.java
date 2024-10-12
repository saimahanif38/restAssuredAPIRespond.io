package apiAutomation;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class restAssuredApis {

    // Class-level variable to store the token
    private String token;

    @BeforeClass
    public void setup() {
        // Set base URI for all tests
        RestAssured.baseURI = "https://app.respond.io/";
    }

    @Test
    public void testLogin() {
        // Create request request body for login
        String requestBody = "{\"email\":\"saimahanif1938+00@gmail.com\",\"password\":\"Click123#\"}";

        // Send Get request to the login endpoint
        Response loginResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("auth/login");
       

        // Validate login response status code
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Expected status code is 200");
        token = loginResponse.jsonPath().getString("data.idToken");
    }

    @Test(dependsOnMethods = "testLogin")
    public void testSecurityPage() {
        // Use the token retrieved in the testLogin method
        Response apiResponse = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .header("orgid", "249553")
                .get("api/organization/249553/security");

        // Validate the response
        Assert.assertEquals(apiResponse.getStatusCode(), 200, "Expected status code is 200");
    }
    
    @Test(dependsOnMethods = "testLogin")
    public void testUpdateProfile() {
        String requestBody = "{\"firstName\":\"ssss\",\"lastName\":\"User\",\"lang\":\"en\"}";

    	
        // Use the token retrieved in the testLogin method
        Response apiResponse = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .header("orgid", "249553")
                .contentType(ContentType.JSON)
                .body(requestBody)
                .put("auth/user/profile");

        // Validate the response
        Assert.assertEquals(apiResponse.getStatusCode(), 200, "Expected status code is 200");
    }
}
