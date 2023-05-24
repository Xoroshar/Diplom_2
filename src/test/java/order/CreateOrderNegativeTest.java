package order;

import clients.OrderClient;
import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.User;
import user.UserGenerator;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class CreateOrderNegativeTest {
    private final List<String> ingredients;
    private UserClient userClient;
    private String token;
    private final int statusCode;
    private final String message;

    public CreateOrderNegativeTest(List<String> ingredients, int statusCode, String message) {
        this.ingredients = ingredients;
        this.statusCode = statusCode;
        this.message = message;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {Arrays.asList("60d3b41abdacab0026a733c6", "609646e4dc916e00276b2870"), 400, "One or more ids provided are incorrect"},
                {List.of(), 400, "Ingredient ids must be provided"},
                {List.of("test"), 500, ""},
                {List.of(""), 500, ""},
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        User user = UserGenerator.getRandom();
        ValidatableResponse createUserResponse = userClient.create(user);
        token = createUserResponse.extract().path("accessToken");
    }

    @After
    public void cleanUp() {
        userClient.delete(token);
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка статус кода и тела ответа для /api/orders")
    public void createOrderAndCheckResponse() {
        Order order = new Order(ingredients);
        OrderClient orderClient = new OrderClient();
        ValidatableResponse createOrderResponse = orderClient.create(order, token);
        assertEquals("Статус код ответа не соответствует ожидаемому", statusCode, createOrderResponse.extract().statusCode());
        if (statusCode != 500) {
            assertFalse("Тело ответа не соответствует ожидаемому", createOrderResponse.extract().path("success"));
            assertEquals("Тело ответа не соответствует ожидаемому", message, createOrderResponse.extract().path("message"));
        }
    }

}
