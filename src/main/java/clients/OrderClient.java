package clients;

import io.restassured.response.ValidatableResponse;
import order.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    private static final String ORDER_CREATE_PATH = "/api/orders";
    private static final String ORDER_GET_PATH = "/api/orders";

    public ValidatableResponse create(Order order) {
        return given().spec(getBaseSpec()).body(order).when().post(ORDER_CREATE_PATH).then();
    }

    public ValidatableResponse create(Order order, String token) {
        return given().spec(getBaseSpec())
                .auth().oauth2(token.replace("Bearer ", ""))
                .body(order)
                .when().post(ORDER_CREATE_PATH).then();
    }

    public ValidatableResponse get(Order order, String token) {
        return given().spec(getBaseSpec())
                .auth().oauth2(token.replace("Bearer ", ""))
                .body(order)
                .when().get(ORDER_GET_PATH).then();
    }

    public ValidatableResponse get(Order order) {
        return given().spec(getBaseSpec()).body(order).when().get(ORDER_GET_PATH).then();
    }

}

