package clients;

import io.restassured.response.ValidatableResponse;
import user.User;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {
    private static final String USER_CREATE_PATH = "/api/auth/register";
    private static final String USER_DELETE_PATH = "/api/auth/user";
    private static final String USER_LOGIN_PATH = "/api/auth/login";
    private static final String USER_CHANGE_PATH = "/api/auth/user";

    public ValidatableResponse create(User user) {
        return given().spec(getBaseSpec()).body(user).when().post(USER_CREATE_PATH).then();
    }

    public ValidatableResponse delete(String token) {
        return given().spec(getBaseSpec()).auth().oauth2(token.replace("Bearer ", "")).when().delete(USER_DELETE_PATH).then();
    }

    public ValidatableResponse login(User user) {
        return given().spec(getBaseSpec()).body(user).when().post(USER_LOGIN_PATH).then();
    }

    public ValidatableResponse change(String token, User user) {
        return given().spec(getBaseSpec())
                .auth().oauth2(token.replace("Bearer ", ""))
                .body(user)
                .when().patch(USER_CHANGE_PATH).then();
    }

    public ValidatableResponse change(User user) {
        return given().spec(getBaseSpec()).body(user).when().patch(USER_CHANGE_PATH).then();
    }

}
