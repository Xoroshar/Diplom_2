package user;

import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserChangeDataNegativeTest {
    private UserClient userClient;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
        ValidatableResponse createUserResponse = userClient.create(UserGenerator.getRandom());
        token = createUserResponse.extract().path("accessToken");
    }

    @After
    public void cleanUp() {
        userClient.delete(token);
    }
    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Проверка статус кода и тела ответа для /api/auth/user")
    public void changeDataNotAuthorizedUserAndCheckResponse() {
        ValidatableResponse changeUserResponse = userClient.change(UserGenerator.getRandom());
        assertEquals("Статус код ответа не соответствует ожидаемому", 401, changeUserResponse.extract().statusCode());
        assertFalse("Тело ответа не соответствует ожидаемому", changeUserResponse.extract().path("success"));
    }
}
