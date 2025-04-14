package api;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ReqresApiTests {
    String baseUri = "https://reqres.in";
    String userId;

    @Test(priority = 1)
    public void createUser() {
        JSONObject request = new JSONObject();
        request.put("name", "John Doe");
        request.put("job", "Engineer");

        Response response = given()
                .contentType("application/json")
                .body(request.toString())
                .when()
                .post(baseUri + "/api/users");

        userId = response.jsonPath().getString("id");

        Assert.assertEquals(response.statusCode(), 201);
        Assert.assertEquals(response.jsonPath().getString("name"), "John Doe");
    }

    @Test(priority = 2)
    public void getUser() {
        int staticUserId = 2; // existing user
        Response response = given()
                .when()
                .get(baseUri + "/api/users/" + staticUserId);

        System.out.println(response.asPrettyString());
        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("janet.weaver"));
    }

    @Test(priority = 3, dependsOnMethods = "getUser")
    public void updateUser() {
        JSONObject request = new JSONObject();
        request.put("name", "John Smith");
        request.put("job", "Manager");

        Response response = given()
                .contentType("application/json")
                .body(request.toString())
                .when()
                .put(baseUri + "/api/users/" + userId);

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("job"), "Manager");
    }
}
