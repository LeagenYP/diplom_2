import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.LoginModel;
import model.OrderModel;
import model.UserModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static data.OrderData.*;
import static data.UserData.*;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static steps.LoginSteps.loginUser;
import static steps.OrderSteps.createOrderWithAuthToken;
import static steps.OrderSteps.createOrderWithoutAuthToken;
import static steps.UserSteps.createUser;
import static steps.UserSteps.deleteUser;

public class TestCreateOrder extends BaseApiTest {

    private String userAuthToken;
    Response response;

    @Description("Подготовка пользователя")
    @Before
    public void setUp() {
        UserModel user = new UserModel(EMAIL, PASSWORD, NAME);
        response = createUser(user);
        LoginModel loginModel = new LoginModel(EMAIL, PASSWORD);
        loginUser(loginModel);
        userAuthToken = response.jsonPath().get("accessToken");
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void testCreateOrderWithoutAuthToken() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add(INGREDIENT_BUN);
        OrderModel order = new OrderModel(ingredients);

        createOrderWithoutAuthToken(order)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("order", notNullValue())
                .extract().response();
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и с ингредиентами")
    public void testCreateOrderWithAuthTokenAndWithIngredients() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add(INGREDIENT_SAUCE);
        ingredients.add(INGREDIENT_MEAT);
        OrderModel order = new OrderModel(ingredients);

        createOrderWithAuthToken(order, userAuthToken)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("order", notNullValue())
                .extract().response();
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и без ингредиентов")
    public void testCreateOrderWithAuthTokenAndWithoutIngredients() {
        List<String> ingredients = new ArrayList<>();
        OrderModel order = new OrderModel(ingredients);

        createOrderWithAuthToken(order, userAuthToken)
                .then()
                .log().all()
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", equalTo("Ingredient ids must be provided"))
                .extract().response();
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и без ингредиентов")
    public void testCreateOrderWithAuthTokenAndWithWrongIngredientsHash() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add(INGREDIENT_BUN + "2222dds");
        OrderModel order = new OrderModel(ingredients);

        createOrderWithAuthToken(order, userAuthToken)
                .then()
                .log().all()
                .statusCode(HTTP_INTERNAL_ERROR) // По документации ожидаем 500, но правильнее было бы ожидать 400
                .extract().response();
    }

    @After
    @Step("Очистка тестовых данных: удаление пользователя")
    public void clearData() {
        if (userAuthToken != null) {
            deleteUser(userAuthToken);
        }
    }
}
