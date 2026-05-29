package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.LoginModel;

import static data.Endpoints.LOGIN_USER_PATH;
import static io.restassured.RestAssured.given;

public class LoginSteps {

    @Step("Авторизация курьера")
    public static Response loginUser(LoginModel loginModel) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(loginModel)
                .when()
                .post(LOGIN_USER_PATH)
                .then()
                .log().all()
                .extract().response();
    }
}
