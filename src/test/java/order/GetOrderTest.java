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

import static org.junit.Assert.*;

public class GetOrderTest {
    static final List<String> ingredients = Arrays.asList("60d3b41abdacab0026a733c6", "609646e4dc916e00276b2870");
    private UserClient userClient;
    private String token;
    private Order order;
    private OrderClient orderClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
        User user = UserGenerator.getRandom();
        ValidatableResponse createUserResponse = userClient.create(user);
        token = createUserResponse.extract().path("accessToken");
        order = new Order(ingredients);
        orderClient = new OrderClient();
    }

    @After
    public void cleanUp() {
        userClient.delete(token);
    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    @Description("Проверка статус кода и тела ответа для /api/orders")
    public void getOrderAndCheckResponse() {
        ValidatableResponse getOrderResponse = orderClient.get(order, token);
        assertEquals("Статус код ответа не соответствует ожидаемому", 200, getOrderResponse.extract().statusCode());
        assertTrue("Тело ответа не соответствует ожидаемому", getOrderResponse.extract().path("success"));
        assertNotNull("Должны вернуться заказы", getOrderResponse.extract().path("orders"));
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    @Description("Проверка статус кода и тела ответа для /api/orders")
    public void getOrderAndCheckResponseForNoAutorizedUser() {
        ValidatableResponse getOrderResponse = orderClient.get(order);
        assertEquals("Статус код ответа не соответствует ожидаемому", 401, getOrderResponse.extract().statusCode());
        assertFalse("Тело ответа не соответствует ожидаемому", getOrderResponse.extract().path("success"));
        assertEquals("id курьера должен быть не null", "You should be authorised", getOrderResponse.extract().path("message"));
    }
}
