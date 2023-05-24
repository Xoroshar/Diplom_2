package order;

import clients.OrderClient;
import clients.UserClient;
import io.qameta.allure.Description;
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
    private UserClient userClient;
    private String token;

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
        OrderClient orderClient = new OrderClient();
        ValidatableResponse createOrderResponse = orderClient.create(new Order(ingredients), token);
        assertEquals("Статус код ответа не соответствует ожидаемому", 200, createOrderResponse.extract().statusCode());
        assertTrue("Тело ответа не соответствует ожидаемому", createOrderResponse.extract().path("success"));
    }

}
