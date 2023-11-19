import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.pojo.Courier;

import static io.restassured.RestAssured.given;

public class CourierTest extends YandexScooterAPITest {
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
