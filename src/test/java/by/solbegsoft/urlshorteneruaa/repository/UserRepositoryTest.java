package by.solbegsoft.urlshorteneruaa.repository;

import by.solbegsoft.urlshorteneruaa.config.PasswordEncoderConfig;
import by.solbegsoft.urlshorteneruaa.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import java.util.UUID;

import static by.solbegsoft.urlshorteneruaa.util.UserRole.ROLE_USER;
import static by.solbegsoft.urlshorteneruaa.util.UserStatus.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(PasswordEncoderConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoderTest;
    private final String FIRST_NAME = "Yury";
    private final String LAST_NAME = "Musienko";
    private final String EMAIL = "musienko97@gmail.com";
    private final String PASSWORD = "12345";

    @Test
    void existsByEmail() {
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(passwordEncoderTest.encode(PASSWORD))
                .userRole(ROLE_USER)
                .userStatus(ACTIVE)
                .build();
        User savedUser = userRepository.save(user);
        assertEquals(user, savedUser);
    }

    @Test
    void getByEmail() {
    }
}