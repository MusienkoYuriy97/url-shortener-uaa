package by.solbegsoft.urlshorteneruaa.util;

import java.util.UUID;

import static by.solbegsoft.urlshorteneruaa.common.StringGenerator.*;

public class UserConstant {
    public static final UUID USER_UUID = UUID.randomUUID();
    public static final String FIRST_USER_NAME = "Ivan";
    public static final String LAST_USER_NAME = "Pavlov";
    public static final String USER_EMAIL = "musienko@gmail.com";
    public static final String USER_PASSWORD = "1111";
    public static final String USER_NEW_PASSWORD = "1234";
    public static final String SIMPLE_ACTIVATE_KEY = generate(10);

    public static final UUID ADMIN_UUID = UUID.randomUUID();
    public static final String FIRST_ADMIN_NAME = "Yuriy";
    public static final String LAST_ADMIN_NAME = "Musienko";
    public static final String ADMIN_EMAIL = "97musienko@gmail.com";
    public static final String ADMIN_PASSWORD = "admin";

    public static final String DB_TEST_EMAIL = "test@gmail.com";

}
