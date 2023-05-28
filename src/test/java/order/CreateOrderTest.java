package order;

import clients.OrderClient;
import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserGenerator;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateOrderTest {
    private final List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6e");
    private final int statusCode = 200;
    private UserClient userClient;
    private String token;
    private OrderClient orderClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
        User user = UserGenerator.getRandom();
        ValidatableResponse createUserResponse = userClient.create(user);
        token = createUserResponse.extract().path("accessToken");
        orderClient = new OrderClient();
    }

    @After
    public void cleanUp() {
        userClient.delete(token);
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка статус кода и тела ответа для /api/orders")
    public void createOrderAndCheckResponse() {
        ValidatableResponse createOrderResponse = orderClient.create(new Order(ingredients), token);
        compareStatusCodeWithExpected(createOrderResponse);
        comparePathSuccessWithExpected(createOrderResponse);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка статус кода и тела ответа для /api/orders")
    public void createOrderWithoutAuthorizationAndCheckResponse() {
        ValidatableResponse createOrderResponse = orderClient.create(new Order(ingredients));
        compareStatusCodeWithExpected(createOrderResponse);
        comparePathSuccessWithExpected(createOrderResponse);
    }

    @Step("Проверка статус кода")
    public void compareStatusCodeWithExpected(ValidatableResponse response) {
        assertEquals("Статус код ответа не соответствует ожидаемому", statusCode, response.extract().statusCode());
    }

    @Step("Проверка тела ответа")
    public void comparePathSuccessWithExpected(ValidatableResponse response) {
        assertTrue("Тело ответа не соответствует ожидаемому", response.extract().path("success"));
    }
}
