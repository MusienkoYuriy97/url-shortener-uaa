package by.solbegsoft.urlshorteneruaa.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static by.solbegsoft.urlshorteneruaa.util.UserConstant.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PasswordEncoderTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void passwordEncoder() {
        //encode
        String encodedPassword = passwordEncoder.encode(USER_PASSWORD);
        //assert
        assertTrue(passwordEncoder.matches(USER_PASSWORD, encodedPassword));
    }
}