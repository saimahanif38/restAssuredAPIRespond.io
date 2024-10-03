package apiAutomation;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class restAssuredApis {

	@Test
	public void testLogin() {
		
		// Set base URI
        RestAssured.baseURI = "https://app.respond.io/auth/"; // Replace with your API base URL

        // Create request payload for login
        String requestBody = "{\"email\": \"saimahanif1938@gmail.com\", \"password\": \"Click123#\"\n"
        		+ "}";

        // Send POST request to the login endpoint
        Response loginResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("login");

        // Validate login response status code
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Expected status code is 200");
	}
	
	@Test
	public void testDashboardAccess() {

        // Get the token from the response
        String token = "XX";

		Response apiResponse = RestAssured.given()
		        .header("Authorization", "Bearer " + token)
		        .header("Content-Type", "application/json")
		        .header("Accept", "application/json")
		        .post("user/activity");
		
        Assert.assertEquals(apiResponse.getStatusCode(), 200, "Expected status code is 200");
	}
} 
