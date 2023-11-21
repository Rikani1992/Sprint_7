import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.pojo.Courier;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CourierCreationTest extends CourierTest {
    @Test
    public void canCreateCourier() {
        Courier courier = new Courier(faker.name().username(),
                faker.internet().password(), faker.name().firstName());
        Response response = courierSteps.createCourier(courier);
        courierSteps.checkResponseIsOkAndStatusCodeIs201(response);
        int courierId = courierSteps.loginAndGetCreatedCourierId(courier);
        courierSteps.removeCreatedCourierById(courierId);
    }

    @Test
    @DisplayName("Creating two couriers with the same login names")
    @Description("When trying to create two couriers with the same" +
            " login names it is supposed to get an error message")
    public void whenCreateTwoCouriersWithTheSameDataGetErrorMessage() {
        Courier courier = new Courier(faker.name().username(),
                faker.internet().password(), faker.name().firstName());

        courierSteps.createCourier(courier);
        courierSteps.createCourier(courier).then().assertThat().statusCode(409);
        int courierId = courierSteps.loginAndGetCreatedCourierId(courier);
        courierSteps.removeCreatedCourierById(courierId);
    }

    @Test
    @DisplayName("Trying to create courier without all required fields")
    @Description("When trying to create courier without all required fields it is supposed to get and" +
            " 400 error status code and reporting message")
    public void whenCreatingCourierWithoutRequiredFields() {
        Courier courier = new Courier();
        courierSteps.createCourier(courier)
                .then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Trying to create courier without 1 required field")
    @Description("When trying to create courier without password it" +
            " is supposed to get and 400 error status code")
    public void whenOneRequiredFieldIsMissedReturnsAnError() {
        Courier courier = new Courier(faker.name().username());
        courierSteps.createCourier(courier).then().assertThat().statusCode(400);
    }

    @Test
    @DisplayName("Creating two couriers with the same login names")
    @Description("When trying to create two couriers with the same" +
            " login names it is supposed to get an error message")
    public void whenCreateTwoCouriersWithTheSameLoginsGetErrorMessage() {
        String login = faker.name().username();
        Courier courier1 = new Courier(login, faker.internet().password(), faker.name().firstName());
        Courier courier2 = new Courier(login, faker.internet().password(), faker.name().firstName());
        courierSteps.createCourier(courier1);
        courierSteps.createCourier(courier2)
                .then()
                .assertThat()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
        int courierId = courierSteps.loginAndGetCreatedCourierId(courier1);
        courierSteps.removeCreatedCourierById(courierId);
    }

}
