package user;

import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {
    public static User getRandom() {
        return new User(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru",
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10));
    }

    public static User getRandomWithoutEmail() {
        return new User(null,
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10));
    }

    public static User getRandomWithoutPassword() {
        return new User(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru",
                null,
                RandomStringUtils.randomAlphabetic(10));
    }

    public static User getRandomWithoutName() {
        return new User(RandomStringUtils.randomAlphabetic(10) + "yandex.ru",
                RandomStringUtils.randomAlphabetic(10),
                null);
    }
}
