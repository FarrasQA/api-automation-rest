package tests.auth;

import body.auth.LoginBody;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.io.FileWriter;
import java.io.IOException;

public class LoginTest {

    //hit endpoint url auth
    @Test
    public void logintest() throws IOException {
        RestAssured.baseURI = ConfigReader.getProperty("baseUrl");

        //generate payload login
        LoginBody loginBody = new LoginBody();

        //hit endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(loginBody.logindata().toString())
                .when()
                .post("v1/login")
                .then()
                .extract().response();

        System.out.println("Response: " + response.asString());

        //Assertion
        Assert.assertEquals(response.getStatusCode(), 200, "Status code 400 ");

        String message = response.jsonPath().getString("message");
        Assert.assertEquals(message, "User login successfully.", "User login failed.");

        //Extract Token
        String token = response.jsonPath().getString("data.token");
        System.out.println("Token: " + token);

        //Simpan token di suatu tempat
        JSONObject tokenJson = new JSONObject();
        tokenJson.put("token", token);

        try (FileWriter file = new FileWriter("src/resources/json/token.json")){
            file.write(tokenJson.toJSONString());
            file.flush();
        }
        System.out.println("Token berhasil tersimpan di src/resources/json/token.json");

    }
}
