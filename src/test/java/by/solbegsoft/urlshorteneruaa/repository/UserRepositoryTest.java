package by.solbegsoft.urlshorteneruaa.repository;

import by.solbegsoft.urlshorteneruaa.config.PasswordEncoderConfig;
import by.solbegsoft.urlshorteneruaa.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static by.solbegsoft.urlshorteneruaa.util.UserConstant.*;
import static by.solbegsoft.urlshorteneruaa.util.UserRole.ROLE_USER;
import static by.solbegsoft.urlshorteneruaa.util.UserStatus.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(PasswordEncoderConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void init(){
        User user = activeUser();
        userRepository.save(user);
    }

    @Test
    void existsByEmail() {
        assertTrue(userRepository.existsByEmail(USER_EMAIL));
    }

    @Test
    void existsByEmailNotExist() {
        assertFalse(userRepository.existsByEmail(ADMIN_EMAIL));
    }

    @Test
    void getByEmail() {
        Optional<User> byEmail = userRepository.getByEmail(USER_EMAIL);
        assertFalse(byEmail.isEmpty());
    }

    @Test
    void getByEmailIsNull() {
        Optional<User> byEmail = userRepository.getByEmail(ADMIN_EMAIL);
        assertTrue(byEmail.isEmpty());
    }
    private User activeUser(){
        return User.builder()
                .firstName(FIRST_USER_NAME)
                .lastName(LAST_USER_NAME)
                .email(DB_TEST_EMAIL)
                .password(passwordEncoder.encode(USER_PASSWORD))
                .userRole(ROLE_USER)
                .userStatus(ACTIVE)
                .build();
    }
}