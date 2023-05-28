package user.change;

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

public class UserChangeData {
    private final int statusCode = 200;
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
        compareStatusCodeWithExpected(changeUserResponse);
        comparePathSuccessWithExpected(changeUserResponse);

        ValidatableResponse loginUserResponse = userClient.login(newUserData);
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
