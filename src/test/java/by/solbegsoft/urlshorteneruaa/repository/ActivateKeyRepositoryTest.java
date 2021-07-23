package by.solbegsoft.urlshorteneruaa.repository;

import by.solbegsoft.urlshorteneruaa.config.PasswordEncoderConfig;
import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
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
import static by.solbegsoft.urlshorteneruaa.util.UserStatus.BLOCKED;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(PasswordEncoderConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ActivateKeyRepositoryTest {
    @Autowired
    private ActivateKeyRepository activateKeyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void init(){
        ActivateKey activateKey = new ActivateKey();
        User save = userRepository.save(user());
        activateKey.setUser(save);
        activateKey.setSimpleKey(SIMPLE_ACTIVATE_KEY);
        activateKeyRepository.save(activateKey);
    }

    @Test
    void deleteActivateKeyBySimpleKey() {
        long count = activateKeyRepository.deleteActivateKeyBySimpleKey(SIMPLE_ACTIVATE_KEY);
        assertEquals(1, count);
    }

    @Test
    void getBySimpleKey() {
        Optional<ActivateKey> bySimpleKey = activateKeyRepository.getBySimpleKey(SIMPLE_ACTIVATE_KEY);
        assertFalse(bySimpleKey.isEmpty());
    }

    private User user(){
        return User.builder()
                .firstName(FIRST_USER_NAME)
                .lastName(LAST_USER_NAME)
                .email(DB_TEST_EMAIL)
                .password(passwordEncoder.encode(USER_PASSWORD))
                .userRole(ROLE_USER)
                .userStatus(BLOCKED)
                .build();
    }
}