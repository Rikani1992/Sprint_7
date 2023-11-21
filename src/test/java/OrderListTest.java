import io.restassured.response.Response;
import model.pojo.Courier;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class OrderListTest extends OrderTest {
    @Test
    public void getOrdersWithNoParamsReturnsAllOrders() {
        Response response = orderSteps.getAllOrders();
        response
                .then()
                .assertThat()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void getExistingCourierOrdersReturnsOrders() {
        Courier courier = new Courier(faker.name().username(),
                faker.internet().password(), faker.name().firstName());
        courierSteps.createCourier(courier);
        int courierId = courierSteps.getCourierIdFromResponseBody(courierSteps.logIn(courier));

        Response response = orderSteps.getCourierOrders(courierId);
        courierSteps.removeCreatedCourierById(courierId);
        response
                .then()
                .assertThat()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void getExistingCourierOrdersOnProvidedStationsReturnsOrders() {
        Courier courier = new Courier(faker.name().username(),
                faker.internet().password(), faker.name().firstName());
        courierSteps.createCourier(courier);
        int courierId = courierSteps.getCourierIdFromResponseBody(courierSteps.logIn(courier));

        Response response = orderSteps.getCourierOrdersOnProvidedStations(courierId, "1", "2");
        courierSteps.removeCreatedCourierById(courierId);
        response
                .then()
                .assertThat()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void whenTryToGetTenAvailableOrdersReturnsOrderList() {
        orderSteps.getTenAvailableOrders()
                .then().assertThat()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void whenTryToGetTenAvailableOrdersNearProvidedStationReturnsOrderList() {
        orderSteps.getTenAvailableOrdersNearStation("110")
                .then().assertThat()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void getNotExistingCourierOrdersReturnsErrorMessage() {
        Courier courier = new Courier(faker.name().username(),
                faker.internet().password(), faker.name().firstName());
        courierSteps.createCourier(courier);
        int courierId = courierSteps.getCourierIdFromResponseBody(courierSteps.logIn(courier));
        courierSteps.removeCreatedCourierById(courierId);
        orderSteps.getCourierOrders(courierId)
                .then()
                .assertThat()
                .body("message", equalTo("Курьер с идентификатором " + courierId + " не найден"))
                .and()
                .statusCode(404);
    }

}
