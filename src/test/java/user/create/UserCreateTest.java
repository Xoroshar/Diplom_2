package user.create;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserCreateTest {
    private final int statusCode = 200;
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
        compareStatusCodeWithExpected(createUserResponse);
        comparePathSuccessWithExpected(createUserResponse);

        ValidatableResponse loginUserResponse = userClient.login(user);
        compareStatusCodeWithExpected(loginUserResponse);
        comparePathSuccessWithExpected(loginUserResponse);

        token = loginUserResponse.extract().path("accessToken");
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
