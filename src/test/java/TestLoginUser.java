import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.LoginModel;
import model.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static data.UserData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static steps.LoginSteps.loginUser;
import static steps.UserSteps.createUser;
import static steps.UserSteps.deleteUser;

public class TestLoginUser extends BaseApiTest {

    @Description("Подготовка пользователя")
    @Before
    public void setUp() {
        UserModel user = new UserModel(EMAIL, PASSWORD, NAME);
        createUser(user);
    }

    @DisplayName("Проверка успешной авторизации пользователя")
    @Test
    public void testLoginUser() {
        LoginModel loginModel = new LoginModel(EMAIL, PASSWORD);
        loginUser(loginModel)
                .then()
                .log().all()
                .statusCode(equalTo(HTTP_OK))
                .body("success", equalTo(true));
    }

    @DisplayName("Проверка авторизации с неверными данными пользователя")
    @Test
    public void testLoginUserWithIncorrectPassword() {
        LoginModel loginModel = new LoginModel(EMAIL, PASSWORD + "1");
        loginUser(loginModel)
                .then()
                .log().all()
                .statusCode(HTTP_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }

    @DisplayName("Проверка авторизации с неверными данными пользователя")
    @Test
    public void testLoginUserWithIncorrectEmail() {
        LoginModel loginModel = new LoginModel(EMAIL + "1", PASSWORD);
        loginUser(loginModel)
                .then()
                .log().all()
                .statusCode(HTTP_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
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
