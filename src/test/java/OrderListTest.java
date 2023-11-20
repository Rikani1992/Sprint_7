import io.restassured.response.Response;
import model.pojo.Courier;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderListTest extends OrderTest {

    private final CourierTest courierTest = new CourierTest();

    @Test
    public void getOrdersWithNoParamsReturnsAllOrders() {
        Response response = given().get("/api/v1/orders");
        response
                .then()
                .assertThat()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void getExistingCourierOrdersReturnsOrders() {
        Courier courier = new Courier("eugenymc07", "1234", "John");
        courierTest.createCourier(courier);
        int courierId = courierTest.getCourierIdFromResponseBody(courierTest.logIn(courier));

        Response response = given().get("/api/v1/orders?courierId=" + courierId);
        courierTest.removeCreatedCourierById(courierId);
        response
                .then()
                .assertThat()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void getExistingCourierOrdersOnProvidedStationsReturnsOrders() {
        Courier courier = new Courier("eugenymc07", "1234", "John");
        courierTest.createCourier(courier);
        int courierId = courierTest.getCourierIdFromResponseBody(courierTest.logIn(courier));

        Response response = given()
                        .get("/api/v1/orders?courierId=" + courierId + "&nearestStation=[\"1\", \"2\"]");
        courierTest.removeCreatedCourierById(courierId);
        response
                .then()
                .assertThat()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void whenTryToGetTenAvailableOrdersReturnsOrderList() {
        given().get("/api/v1/orders?limit=10&page=0")
                .then().assertThat()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void whenTryToGetTenAvailableOrdersNearProvidedStationReturnsOrderList() {
        given().get("/api/v1/orders?limit=10&page=0&nearestStation=[\"110\"]")
                .then().assertThat()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void getNotExistingCourierOrdersOnProvidedStationsReturnsErrorMessage() {
        Courier courier = new Courier("eugenymc07", "1234", "John");
        courierTest.createCourier(courier);
        int courierId = courierTest.getCourierIdFromResponseBody(courierTest.logIn(courier));
        courierTest.removeCreatedCourierById(courierId);
        Response response = given()
                .get("/api/v1/orders?courierId=" + courierId);
        response
                .then()
                .assertThat()
                .body("message", equalTo("Курьер с идентификатором " + courierId + " не найден"))
                .and()
                .statusCode(404);
    }

}
