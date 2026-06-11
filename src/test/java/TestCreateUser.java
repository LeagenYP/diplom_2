import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.LoginModel;
import model.UserModel;
import org.junit.After;
import org.junit.Test;
import static data.UserData.*;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static steps.LoginSteps.loginUser;
import static steps.UserSteps.createUser;
import static steps.UserSteps.deleteUser;

public class TestCreateUser extends BaseApiTest {

    UserModel user = new UserModel(EMAIL, PASSWORD, NAME);

    @DisplayName("Проверка успешного создания пользователя")
    @Test
    public void testCreateUser()
    {
        createUser(user)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .extract().response();
    }

    @DisplayName("Нельзя создать пользователя, который уже зарегестрирован")
    @Test
    public void testCreateUserTwice()
    {
        // Первое создание юзера
        createUser(user)
                .then()
                .log().all()
                .extract().response();

        // Второе создание того же юзера
        Response response = createUser(user)
                .then()
                .log().all()
                .statusCode(HTTP_FORBIDDEN)
                .body("message", equalTo("User already exists"))
                .extract().response();

        // Очистка данных второго юзера, если он был создан
        String userAuthTokenTwo = response.jsonPath().get("accessToken");
        if (userAuthTokenTwo != null) {
            deleteUser(userAuthTokenTwo);
        }
    }

    @Test
    @DisplayName("Попытка создать пользователя без почты (не допускается)")
    public void testCannotCreateUserWithoutEmail() {
        UserModel user = new UserModel(null, PASSWORD, NAME);

        createUser(user)
                .then()
                .log().all()
                .statusCode(HTTP_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"))
                .extract().response();
    }

    @Test
    @DisplayName("Попытка создать пользователя без имени (не допускается)")
    public void testCannotCreateUserWithoutName() {
        UserModel user = new UserModel(EMAIL, PASSWORD, null);

        createUser(user)
                .then()
                .log().all()
                .statusCode(HTTP_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"))
                .extract().response();
    }

    @Test
    @DisplayName("Попытка создать пользователя без пароля (не допускается)")
    public void testCannotCreateUserWithoutPassword() {
        UserModel user = new UserModel(EMAIL, null, NAME);

        createUser(user)
                .then()
                .log().all()
                .statusCode(HTTP_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"))
                .extract().response();
    }

    @After
    @Step("Очистка тестовых данных: удаление пользователя")
    public void clearData() {
        LoginModel loginModel = new LoginModel(EMAIL, PASSWORD);
        Response response = loginUser(loginModel);
        String userAuthToken = response.jsonPath().get("accessToken");
        if (userAuthToken != null) {
            deleteUser(userAuthToken);
        }
    }
}
