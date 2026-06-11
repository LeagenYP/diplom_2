package data;

import com.github.javafaker.Faker;

public class UserData {

    static Faker user = new Faker();
    public static final String EMAIL = user.name().lastName().toLowerCase() + user.regexify("[0-9]{6}") + "@gmail.com";
    public static final String PASSWORD = user.regexify("[0-9]{6}");
    public static final String NAME = user.name().firstName();
}
