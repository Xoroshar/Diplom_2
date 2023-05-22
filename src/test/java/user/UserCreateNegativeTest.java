package user;

import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class UserCreateNegativeTest {
    private static final int STATUS_CODE = 403;
    private final String message;
    private final boolean duplicateLogin;
    private UserClient userClient;
    private final User user;
    private String token;

    public UserCreateNegativeTest(User user, String message, boolean duplicateLogin) {
        this.user = user;
        this.message = message;
        this.duplicateLogin = duplicateLogin;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {UserGenerator.getRandom(), "User already exists", true},
                {UserGenerator.getRandomWithoutEmail(), "Email, password and name are required fields", false},
                {UserGenerator.getRandomWithoutPassword(), "Email, password and name are required fields", false},
                {UserGenerator.getRandomWithoutName(), "Email, password and name are required fields", false},
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }


    @Test
    @DisplayName("Создание пользователя, негативный тест")
    @Description("Проверка статус кода и тела ответа для /api/auth/register")
    public void createInvalidCouriersAndCheckResponse() {
        ValidatableResponse createUserResponse = userClient.create(user);
        if (duplicateLogin) {
            token = createUserResponse.extract().path("accessToken");
            createUserResponse = userClient.create(user);
        }

//        System.out.println("email : " + user.getEmail());
//        System.out.println("password : " + user.getPassword());
//        System.out.println("name : " + user.getName());
        assertEquals("Статус код ответа не соответствует ожидаемому", STATUS_CODE, createUserResponse.extract().statusCode());
        assertEquals("Тело ответа не соответствует ожидаемому", message, createUserResponse.extract().path("message"));

        if (duplicateLogin) userClient.delete(token);
    }
}
