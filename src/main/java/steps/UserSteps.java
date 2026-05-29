package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.UserModel;
import static data.Endpoints.CREATE_USER_PATH;
import static data.Endpoints.DELETE_USER_PATH;
import static io.restassured.RestAssured.given;

public class UserSteps {

    @Step("Создание пользователя")
    public static Response createUser(UserModel courier) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(CREATE_USER_PATH)
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Удаление пользователя")
    public static void deleteUser(String authToken) {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken.substring(7))
                .when()
                .log().all()
                .delete(DELETE_USER_PATH)
                .then()
                .log().all();
    }
}
