package by.solbegsoft.urlshorteneruaa.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final String PASSWORD = "1234";

    @Test
    void passwordEncoder() {

        String encode = passwordEncoder.encode(PASSWORD);
        assertTrue(passwordEncoder.matches(PASSWORD, encode));
    }
}