import static org.hamcrest.Matchers.*;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;


@RunWith(Parameterized.class)
public class OrderCreationTest extends OrderTest {


    @Parameterized.Parameters
    public static Object[][] testData() {
//        return new Object[][] {
//        }
    }


    @Test
    @DisplayName("Pick only black color")
    public void canPickOnlyBlackColor() {
        File json = new File("src/main/resources/order-data-only-black-color.json");
        Response response = createOrder(json);
        response.then()
                .assertThat()
                .body("track", notNullValue())
                .and()
                .statusCode(201);
        int track = getOrderTrackFromResponseBody(response);
        cancelOrder(track);
    }

    @Test
    @DisplayName("Pick only grey color")
    public void canPickOnlyGreyColor() {
        File json = new File("src/main/resources/order-data-only-grey-color.json");
        Response response = createOrder(json);
        response.then()
                .assertThat()
                .body("track", notNullValue())
                .and()
                .statusCode(201);
        int track = getOrderTrackFromResponseBody(response);
        cancelOrder(track);
    }
}
