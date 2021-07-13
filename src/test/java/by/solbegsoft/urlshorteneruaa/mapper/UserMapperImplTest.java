package by.solbegsoft.urlshorteneruaa.mapper;

import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateDto;
import by.solbegsoft.urlshorteneruaa.dto.UserResponseDto;
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

    @Test
    void toUser() {
        UserCreateDto dto = new UserCreateDto();
        dto.setFirstName("Yuriy");
        dto.setLastName("Musienko");
        dto.setEmail("mus@gmail.com");
        dto.setPassword("1234");
        User user = userMapper.toUser(dto);

        assertEquals(dto.getFirstName(), user.getFirstName());
        assertEquals(dto.getLastName(), user.getLastName());
        assertEquals(dto.getEmail(), user.getEmail());
        assertTrue(passwordEncoder.matches(dto.getPassword(), user.getPassword()));
    }

    @Test
    void toDto() {
        User user = new User();
        user.setFirstName("Yuriy");
        user.setLastName("Musienko");
        user.setEmail("mus@gmail.com");
        user.setUserStatus(ACTIVE);
        user.setUserRole(ROLE_USER);
        UserResponseDto dto = userMapper.toDto(user);

        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getUserRole().name(), dto.getUserRole());
        assertEquals(user.getUserStatus().name(), dto.getUserStatus());
    }
}