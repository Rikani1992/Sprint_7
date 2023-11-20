import static org.hamcrest.Matchers.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.io.File;

@RunWith(Parameterized.class)
public class OrderCreationTest extends OrderTest {

    private final File json;
    private final int statusCode;

    public OrderCreationTest(File orderData, int statusCode) {
        this.json = orderData;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters
    public static Object[][] testData() {
        return new Object[][] {
                {new File(resourcesPath + "order-data-both-colors.json"), 201},
                {new File(resourcesPath + "order-data-no-color.json"), 201},
                {new File(resourcesPath + "order-data-only-black-color.json"), 201},
                {new File(resourcesPath + "order-data-only-grey-color.json"), 201}
        };
    }

    @Test
    @DisplayName("Trying to make order with different combinations of colors")
    public void shouldProceedAnyCombinationOfColors() {
        Response response = createOrder(json);
        response.then()
                .assertThat()
                .body("track", notNullValue())
                .and()
                .statusCode(statusCode);
        int track = getOrderTrackFromResponseBody(response);
        cancelOrder(track);
    }
}
