import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.io.File;

import static io.restassured.RestAssured.given;

public class OrderTest extends YandexScooterAPITest {
    @Step("Cancel order")
    public void cancelOrder(int track) {
        given()
                .header("Content-type", "application/json")
                .body("{\"track\": \"" + track + "\"}")
                .when()
                .put("/api/v1/orders/cancel");

    }

    @Step("Create an order")
    public Response createOrder(File json) {
        return given()
                .header("Content-type", "application/json")
                .body(json).when().post("/api/v1/orders");
    }

    @Step("Get order track number from response body")
    public int getOrderTrackFromResponseBody(Response response) {
        return response.then().extract().body().path("track");
    }

}
