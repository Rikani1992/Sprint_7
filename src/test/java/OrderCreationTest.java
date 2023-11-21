import static org.hamcrest.Matchers.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.pojo.Color;
import model.pojo.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class OrderCreationTest extends OrderTest {

    private final Order order;

    public OrderCreationTest(Order order) {
        this.order = order;
    }

    @Parameterized.Parameters
    public static Object[][] testData() {
        Order orderWithBothColors = orderSteps.createRandomOrderWithColors(Color.GREY, Color.GREY);
        Order orderWithNoColors = orderSteps.createRandomOrderWithColors();
        Order orderWithOnlyGrey = orderSteps.createRandomOrderWithColors(Color.GREY);;
        Order orderWithOnlyBlack = orderSteps.createRandomOrderWithColors(Color.BLACK);;

        return new Object[][] {
                {orderWithBothColors},
                {orderWithNoColors},
                {orderWithOnlyBlack},
                {orderWithOnlyGrey}
        };

    }

    @Test
    @DisplayName("Trying to make order with different combinations of colors")
    public void shouldProceedAnyCombinationOfColors() {
        Response response = orderSteps.createOrder(order);
        response.then()
                .assertThat()
                .body("track", notNullValue())
                .and()
                .statusCode(201);
        int track = orderSteps.getOrderTrackFromResponseBody(response);
        orderSteps.cancelOrder(track);
    }
}
