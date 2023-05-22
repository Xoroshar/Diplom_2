package user;

import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserCreateTest {
    private UserClient userClient;
    private User user;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
    }

    @After
    public void cleanUp() {
        userClient.delete(token);
    }

    @Test
    @DisplayName("Создание пользователя, позитивный тест")
    @Description("Проверка статус кода и тела ответа для /api/auth/register")
    public void createUserAndCheckResponse() {
        ValidatableResponse createUserResponse = userClient.create(user);
        assertEquals("Статус код ответа не соответствует ожидаемому", 200, createUserResponse.extract().statusCode());
        assertTrue("Тело ответа не соответствует ожидаемому", createUserResponse.extract().path("success"));

        ValidatableResponse loginUserResponse = userClient.login(user);
        assertEquals("Статус код ответа не соответствует ожидаемому", 200, loginUserResponse.extract().statusCode());
        assertTrue("Тело ответа не соответствует ожидаемому", loginUserResponse.extract().path("success"));

        token = loginUserResponse.extract().path("accessToken");
    }
}
