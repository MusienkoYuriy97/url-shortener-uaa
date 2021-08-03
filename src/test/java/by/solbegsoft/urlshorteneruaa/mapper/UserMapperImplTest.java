package by.solbegsoft.urlshorteneruaa.mapper;

import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateRequest;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateResponse;
import by.solbegsoft.urlshorteneruaa.util.ObjectCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperImplTest {
    @Autowired
    private ObjectCreator objectCreator;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void toUser() {
        //obj
        UserCreateRequest userCreateRequest = objectCreator.userCreateRequest();
        //call method
        User user = userMapper.toUser(userCreateRequest);
        //assert
        assertEquals(userCreateRequest.getFirstName(), user.getFirstName());
        assertEquals(userCreateRequest.getLastName(), user.getLastName());
        assertEquals(userCreateRequest.getEmail(), user.getEmail());
        assertTrue(passwordEncoder.matches(userCreateRequest.getPassword(), user.getPassword()));
    }

    @Test
    void toDto() {
        //obj
        User user = objectCreator.blockedUser();
        //call method
        UserCreateResponse userCreateResponse = userMapper.toDto(user);
        //assert
        assertEquals(user.getFirstName(), userCreateResponse.getFirstName());
        assertEquals(user.getLastName(), userCreateResponse.getLastName());
        assertEquals(user.getEmail(), userCreateResponse.getEmail());
        assertEquals(user.getUserRole(), userCreateResponse.getUserRole());
        assertEquals(user.getUserStatus(), userCreateResponse.getUserStatus());
    }
}