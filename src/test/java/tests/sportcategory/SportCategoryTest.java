package tests.sportcategory;

import base.BaseTest;
import body.sportcategory.SportCategoryBody;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.TokenHelper;
import utils.Utils;

public class SportCategoryTest extends BaseTest {
    private String categoryId;

    //Ambil token dari src/resources/json/token.json
    //Create
    @Test
    public void createSportCategories() {
        SportCategoryBody sportCategoryBody = new SportCategoryBody();
        String token = TokenHelper.getToken();
        String randomName = Utils.getCategoryName();

        Response response = RestAssured.given()
                .header("Authorization","Bearer " + token)
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .body(sportCategoryBody.createSportCategoryData(randomName).toString())
                .when()
                .post("v1/sport-categories/create")
                .then()
                .extract().response();

        System.out.println("Create Response: " + response.asString());

        //Assertion
        Assert.assertEquals(response.getStatusCode(), 200, "Status code must be 200");

        //Get category from response
        categoryId = response.jsonPath().getString("result.id");
        Assert.assertNotNull(categoryId,"Category ID should not be null");
        System.out.println("Create Category ID: " + categoryId);
    }

    //Get
    @Test
    public void getSportCategories(){
        String token = TokenHelper.getToken();
 
        Response response = RestAssured.given()
                .header("Authorization","Bearer " + token)
                .header("Content-Type","application/json")
                .queryParam("is_paginate","false")
                .queryParam("per_page","")
                .queryParam("page","")
                .when()
                .get("v1/sport-categories")
                .then()
                .extract().response();

        System.out.println("Get Response: " + response.asString());

        //Assertion
        Assert.assertEquals(response.getStatusCode(), 200, "Status code 400");
        Assert.assertNotNull(response.jsonPath().getList("result"), "Category list should not be null");
    }

    //Update
    @Test
    public void updateSportCategory(){
        String token = TokenHelper.getToken();

        String updatedName = "testing day 15";

        Response response = RestAssured.given()
                .header("Authorization","Bearer " + token)
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .body("{ \"name\": \"" + updatedName + "\" }")
                .when()
                .post("v1/sport-categories/update/" + categoryId)
                .then()
                .extract().response();

        System.out.println("Update Response: " + response.asString());

        //Assertion
        Assert.assertEquals(response.getStatusCode(), 200, "Status code 400");

        String name = response.jsonPath().getString("result.name");
        Assert.assertEquals(name, updatedName, "Category name not updated");
    }

    //Delete
    @Test
    public void deleteSportCategory(){
        String token = TokenHelper.getToken();

        Response response = RestAssured.given()
                .header("Authorization","Bearer " + token)
                .header("Content-Type","application/json")
                .when()
                .delete("v1/sport-categories/delete" + categoryId)
                .then()
                .extract().response();

        System.out.println("Delete Response: " + response.asString());
    }
}
