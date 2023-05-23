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

public class UserChangeData {
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
    @DisplayName("Изменение данных пользователя")
    @Description("Проверка статус кода и тела ответа для /api/auth/user")
    public void changeDataAuthorizedUserAndCheckResponse() {
        User newUserData = UserGenerator.getRandom();
        ValidatableResponse changeUserResponse = userClient.change(token, newUserData);
        assertEquals("Статус код ответа не соответствует ожидаемому", 200, changeUserResponse.extract().statusCode());
        assertTrue("Тело ответа не соответствует ожидаемому", changeUserResponse.extract().path("success"));

        ValidatableResponse loginUserResponse = userClient.login(newUserData);
        assertEquals("Статус код ответа не соответствует ожидаемому", 200, loginUserResponse.extract().statusCode());
        assertTrue("Тело ответа не соответствует ожидаемому", loginUserResponse.extract().path("success"));

        token = loginUserResponse.extract().path("accessToken");
    }

}
