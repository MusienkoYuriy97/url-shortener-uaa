package by.solbegsoft.urlshorteneruaa.mapper;

import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateRequest;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static by.solbegsoft.urlshorteneruaa.util.UserRole.*;
import static by.solbegsoft.urlshorteneruaa.util.UserStatus.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperImplTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final String FIRST_NAME = "Yury";
    private final String LAST_NAME = "Musienko";
    private final String EMAIL = "musienko97@gmail.com";
    private final String PASSWORD = "12345";


    @Test
    void toUser() {
        UserCreateRequest dto = new UserCreateRequest();
        dto.setFirstName(FIRST_NAME);
        dto.setLastName(LAST_NAME);
        dto.setEmail(EMAIL);
        dto.setPassword(PASSWORD);
        User user = userMapper.toUser(dto);

        assertEquals(dto.getFirstName(), user.getFirstName());
        assertEquals(dto.getLastName(), user.getLastName());
        assertEquals(dto.getEmail(), user.getEmail());
        assertTrue(passwordEncoder.matches(dto.getPassword(), user.getPassword()));
    }

    @Test
    void toDto() {
        User user = new User();
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setEmail(EMAIL);
        user.setUserStatus(ACTIVE);
        user.setUserRole(ROLE_USER);
        UserCreateResponse dto = userMapper.toDto(user);

        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getUserRole(), dto.getUserRole());
        assertEquals(user.getUserStatus(), dto.getUserStatus());
    }
}