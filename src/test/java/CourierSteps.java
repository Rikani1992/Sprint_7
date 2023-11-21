import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.pojo.Courier;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierSteps {
    @Step("Check that response is ok and status code is 201")
    public void checkResponseIsOkAndStatusCodeIs201(Response response) {
        response.then().assertThat().body("ok", equalTo(true)).and().statusCode(201);
    }

    @Step("Login to get created courier id")
    public int loginAndGetCreatedCourierId(Courier courier) {
        Response response = logIn(courier);
        return getCourierIdFromResponseBody(response);
    }

    @Step("Create courier by request /api/v1/courier")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Remove created courier")
    public void removeCreatedCourierById(int id) {
        given()
                .delete("/api/v1/courier/{id}", id)
                .then().assertThat().statusCode(200);
    }

    @Step("Trying to log in with courier data")
    public Response logIn(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Get courier id from response body")
    public int getCourierIdFromResponseBody(Response response) {
        return response.then().extract().body().path("id");
    }

}
