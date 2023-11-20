import io.restassured.RestAssured;
import org.junit.Before;

public class YandexScooterAPITest {

    public static final String resourcesPath = "src/main/resources/";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

}
