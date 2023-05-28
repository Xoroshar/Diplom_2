package user.login;

import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserGenerator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UserLoginNegativeTest {
    private UserClient userClient;
    private User user;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        ValidatableResponse createUserResponse = userClient.create(user);
        token = createUserResponse.extract().path("accessToken");
    }

    @After
    public void cleanUp() {
        userClient.delete(token);
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем.")
    @Description("Проверка статус кода и тела ответа для /api/auth/login")
    public void loginUserWithWrongNameAndPasswordAndCheckResponse() {
        ValidatableResponse loginUserResponse =
                userClient.login(new User(user.getEmail(),
                        RandomStringUtils.randomAlphabetic(10),
                        RandomStringUtils.randomAlphabetic(10)));
        assertEquals("Статус код ответа не соответствует ожидаемому", 401, loginUserResponse.extract().statusCode());
        assertFalse("Тело ответа не соответствует ожидаемому", loginUserResponse.extract().path("success"));
    }
}
