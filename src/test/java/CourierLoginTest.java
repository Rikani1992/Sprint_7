import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.pojo.Courier;
import org.junit.Test;
import static org.hamcrest.Matchers.*;


public class CourierLoginTest extends CourierTest {

    @Test
    public void courierCanAuthorize() {
        Courier courier = new Courier("eugenymc07", "1234", "saske123");
        createCourier(courier);
        Response response = logIn(courier);
        response.then().assertThat().statusCode(200)
                .and().body("id", notNullValue());
        int courierId = getCourierIdFromResponseBody(response);
        removeCreatedCourierById(courierId);
    }

    @Test
    @DisplayName("Trying to log in courier without all required fields")
    @Description("When trying to log in courier without all required fields it is supposed to get and" +
            " 400 error status code and reporting message")
    public void whenLoggingInCourierWithoutRequiredField() {
        Courier courier = new Courier("eugenymc07");
        logIn(courier)
                .then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Trying to log in courier with correct login and incorrect password")
    @Description("When trying to log in courier with correct login and incorrect password it is supposed" +
            " to get and 404 error status code and reporting message")
    public void whenLoggingInCourierWithCorrectLoginAndIncorrectPasswordReturnsError() {
        Courier courier1 = new Courier("eugenymc07", "1234");
        createCourier(courier1);
        Response response = logIn(courier1);
        int createdCourierId = getCourierIdFromResponseBody(response);
        Courier courier2 = new Courier("eugenymc07", "12345");
        logIn(courier2)
                .then()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
        removeCreatedCourierById(createdCourierId);
    }

    @Test
    @DisplayName("Trying to log in unregistered courier ")
    @Description("When trying to log in unregistered courier it is supposed to get and" +
            " 404 error status code and reporting message")
    public void whenLoggingInUnregisteredCourier() {
        Courier courier = new Courier("avadvsdfssgsdgsgdsgadsfsad", "12345");
        logIn(courier)
                .then()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

    @Test
    @DisplayName("Log in to return id")
    @Description("When courier is successfully logged in it is supposed to be returned courier id")
    public void checkSuccessfulLoggingInReturnsCourierId() {
        Courier courier = new Courier("eugenymc07", "1234");
        createCourier(courier);
        Response response = logIn(courier);
        response.then()
                .assertThat()
                .body("id", notNullValue());
    }

}
