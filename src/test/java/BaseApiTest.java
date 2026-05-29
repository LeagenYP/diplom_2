import data.UserData;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

public class BaseApiTest {

    public static final String BASE_URI = "https://stellarburgers.education-services.ru/";

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    public void loginTest(){
        System.out.println("Email Test: " + UserData.EMAIL);
        System.out.println("Password Test: " + UserData.PASSWORD);
        System.out.println("Name Test: " + UserData.NAME);
    }
}
