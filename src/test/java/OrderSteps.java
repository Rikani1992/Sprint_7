import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.pojo.Color;
import model.pojo.Order;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderSteps {
    @Step("Cancel order")
    public void cancelOrder(int track) {
        given()
                .header("Content-type", "application/json")
                .body(new Order(track))
                .when()
                .put("/api/v1/orders/cancel");

    }

    @Step("Create random order with provided colors")
    public Order createRandomOrderWithColors(Color... colors) {
        Faker faker = new Faker();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return new Order(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.address().streetName(),
                faker.number().randomDigitNotZero(),
                faker.phoneNumber().phoneNumber(),
                faker.number().numberBetween(0, 10),
                sdf.format(faker.date().birthday()),
                faker.howIMetYourMother().toString(),
                List.of(colors)
        );
    }

    @Step("Create an order")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order).when().post("/api/v1/orders");
    }

    @Step("Get order track number from response body")
    public int getOrderTrackFromResponseBody(Response response) {
        return response.then().extract().body().path("track");
    }

    @Step("Get orders")
    public Response getOrders(RequestSpecification rs) {
        return rs.get("/api/v1/orders");
    }

    @Step("Get all orders")
    public Response getAllOrders() {
        return getOrders(given());
    }

    @Step("Get courier's orders")
    public Response getCourierOrders(int courierId) {
        RequestSpecification rs = given()
                .queryParam("courierId", courierId)
                .when();
        return getOrders(rs);
    }

    @Step("Get courier's orders")
    public Response getCourierOrdersOnProvidedStations(int courierId, String... stations) {
        RequestSpecification rs = given()
                .queryParam("courierId", courierId)
                .queryParam("nearestStation", (Object) stations)
                .when();
        return getOrders(rs);
    }

    @Step("Get ten available orders")
    public Response getTenAvailableOrders() {
        RequestSpecification rs = given()
                .queryParam("limit", 10)
                .queryParam("page", 0)
                .when();
        return getOrders(rs);
    }

    @Step("Get ten available orders near provided station")
    public Response getTenAvailableOrdersNearStation(String... stations) {
        RequestSpecification rs = given()
                .queryParam("limit", 10)
                .queryParam("page", 0)
                .queryParam("nearestStation", (Object) stations)
                .when();
        return getOrders(rs);
    }
}
