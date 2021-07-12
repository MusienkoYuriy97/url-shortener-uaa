package by.solbegsoft.urlshorteneruaa.config;

import java.util.Set;

public class WhiteList {
    private static final Set<String> whiteList = Set.of(
            "/api/v1/auth/login",
            "/api/v1/auth/registration",
            "/api/v1/user/activate/{activateKey}"
    );

    public static String[] get(){
        return whiteList.toArray(new String[0]);
    }
}