package by.solbegsoft.urlshorteneruaa.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Test
    void passwordEncoder() {
        String password = "1234";
        String encode = passwordEncoder.encode(password);
        assertTrue(passwordEncoder.matches(password, encode));
    }
}