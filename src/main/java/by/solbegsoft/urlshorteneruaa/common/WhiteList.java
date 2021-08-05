package by.solbegsoft.urlshorteneruaa.common;

import java.util.Set;

public class WhiteList {
    private static final Set<String> whiteList = Set.of(
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/configuration/ui",
            "/swagger-ui/**",
            "/auth/login",
            "/url/redirect/**",
            "/auth/registration",
            "/user/activate/**"
    );

    public static String[] get(){
        return whiteList.toArray(new String[0]);
    }
}