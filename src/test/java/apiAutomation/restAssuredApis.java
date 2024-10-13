package apiAutomation;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class restAssuredApis {

    // Class-level variable to store the token
    private String token;

    @BeforeClass
    public void setupBaseURI() {
        // Set base URI for all tests
        baseURI = "https://app.respond.io/";
    }

    @Test
    public void testLoginPostRequest() {
        // Create request request body for login
    	Map<String, Object> requestBody = new HashMap<String, Object>();
    	requestBody.put("email", "saimahanif1938+00@gmail.com");
    	requestBody.put("password", "Click123#");
    	
        // Send Get request to the login endpoint
        Response loginResponse = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
            .when()
                .post("auth/login")
            .then()
                .assertThat()
                .statusCode(200)
                .body("message", equalTo("Login successful"))  // Asserting the message in the response
                .body("status", equalTo("success")) // Asserting the status in the response
                .body("data.idToken", notNullValue()) // Asserting the presence of token in the response
                .extract()
                .response();

        // Validate login response status code
        Assert.assertEquals(loginResponse.getStatusCode(), 200, "Expected status code is 200");
        
        // Save token for accessing other pages after login
        token = loginResponse.jsonPath().getString("data.idToken");
    }

    @Test(dependsOnMethods = "testLoginPostRequest")
    public void testSecuritySSOGetRequest() {
        // Use the token retrieved in the testLogin method
    	// We area accessing security SSO page with the help of token and orgid
    	given()
	    	.header("Authorization", "Bearer " + token)
	        .header("orgid", "249553")
        .when()
        	.get("api/organization/249553/security/sso")
        .then()
	        .assertThat()
	        .statusCode(403)
	        .body("message", equalTo("Access denied: Your organization subscription trial has ended. Please subscribe to a new plan."));
    }
    
    @Test(dependsOnMethods = "testLoginPostRequest")
    public void testUpdateProfilePutRequest() {
    	 // Generate the current timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        // Create a dynamic firstName using the timestamp
        String dynamicFirstName = "Saima Rehman" + timeStamp;
        
        // Create request body
        String requestBody = "{\"firstName\":\""+ dynamicFirstName +"\",\"lastName\":\"User\",\"lang\":\"en\"}";
    	
        // Use the token retrieved in the testLogin method
        given()
	        .header("Authorization", "Bearer " + token)
	        .header("orgid", "249553")
	        .contentType(ContentType.JSON)
	        .body(requestBody)  // Sending the request body
        .when()
        	.put("auth/user/profile")  // Sending a PUT request to update the user profile
        	.then()
        	.assertThat()
	        .statusCode(200)  // Validate the response status code is 200 (OK)
	        .body("message", equalTo("Profile updated successfully"))  // Match updated body: message
	        .body("status", equalTo("success"));  // Match updated body: status
    }
}
