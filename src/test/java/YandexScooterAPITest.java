import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import org.junit.Before;

public class YandexScooterAPITest {

    public static final String resourcesPath = "src/main/resources/";

    public static final Faker faker = new Faker();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

}
