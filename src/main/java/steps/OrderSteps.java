package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.OrderModel;
import static data.Endpoints.CREATE_ORDER_PATH;
import static io.restassured.RestAssured.given;
public class OrderSteps {

    @Step("Создание заказа с авторизацией")
    public static Response createOrderWithAuthToken(OrderModel order, String authToken) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authToken.substring(7))
                .body(order)
                .when()
                .post(CREATE_ORDER_PATH)
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Создание заказа без авторизации")
    public static Response createOrderWithoutAuthToken(OrderModel order) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(CREATE_ORDER_PATH)
                .then()
                .log().all()
                .extract().response();
    }
}
