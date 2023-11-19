import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.pojo.Courier;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.*;

public class CourierCreationTest extends CourierTest {

    @Step("Check that response is ok and status code is 201")
    public void checkResponseIsOkAndStatusCodeIs201(Response response) {
        response.then().assertThat().body("ok", equalTo(true))
                .and().statusCode(201);
    }

    @Step("Login to get created courier id")
    public int loginAndGetCreatedCourierId(Courier courier) {
        Response response = logIn(courier);
        return getCourierIdFromResponseBody(response);
    }

    @Test
    public void canCreateCourier() {
        Courier courier = new Courier("eugenymc07", "1234", "saske123");
        Response response = createCourier(courier);
        checkResponseIsOkAndStatusCodeIs201(response);
        int courierId = loginAndGetCreatedCourierId(courier);
        removeCreatedCourierById(courierId);
    }

    @Test
    @DisplayName("Creating two couriers with the same login names")
    @Description("When trying to create two couriers with the same" +
            " login names it is supposed to get an error message")
    public void whenCreateTwoCouriersWithTheSameDataGetErrorMessage() {
        Courier courier1 = new Courier("eugenymc07", "1234", "saske123");
        Courier courier2 = new Courier("eugenymc07", "1234", "saske123");
        createCourier(courier1);
        createCourier(courier2)
                .then()
                .assertThat()
                .statusCode(409);
        int courierId = loginAndGetCreatedCourierId(courier1);
        removeCreatedCourierById(courierId);
    }

    @Test
    @DisplayName("Trying to create courier without all required fields")
    @Description("When trying to create courier without all required fields it is supposed to get and" +
            " 400 error status code and reporting message")
    public void whenCreatingCourierWithoutRequiredFields() {
        Courier courier = new Courier();
        createCourier(courier)
                .then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Trying to create courier without 1 required field")
    @Description("When trying to create courier without password it is supposed to get and 400 error status code")
    public void whenOneRequiredFieldIsMissedReturnsAnError() {
        Courier courier = new Courier("eugenymc07");
        createCourier(courier).then().assertThat().statusCode(400);
    }

    @Test
    @DisplayName("Creating two couriers with the same login names")
    @Description("When trying to create two couriers with the same" +
            " login names it is supposed to get an error message")
    public void whenCreateTwoCouriersWithTheSameLoginsGetErrorMessage() {
        Courier courier1 = new Courier("eugenymc07", "1234", "saske123");
        Courier courier2 = new Courier("eugenymc07", "4321", "Eugene");
        createCourier(courier1);
        createCourier(courier2)
                .then()
                .assertThat()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and().statusCode(409);
        int courierId = loginAndGetCreatedCourierId(courier1);
        removeCreatedCourierById(courierId);
    }

}
