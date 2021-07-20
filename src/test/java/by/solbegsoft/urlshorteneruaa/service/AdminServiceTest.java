package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.mapper.UserMapper;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.dto.UpdateRoleUserDto;
import by.solbegsoft.urlshorteneruaa.dto.UserResponseDto;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.UserDetailServiceImpl;
import by.solbegsoft.urlshorteneruaa.util.UserRole;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import static by.solbegsoft.urlshorteneruaa.util.UserRole.*;
import static by.solbegsoft.urlshorteneruaa.util.UserStatus.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private UserMapper userMapper;
    @Mock
    private AuthenticationService authenticationService;
    private AdminService adminService;
    private final String EMAIL_ADMIN = "admin@gmail.com";
    private final String EMAIL_USER = "user@gmail.com";

    @BeforeEach
    void setConstruct() {
        MockitoAnnotations.initMocks(this);
        adminService = new AdminService(userRepository, userDetailService, authenticationService, userMapper);
    }

    @BeforeEach
    void setUser() {
        User admin = User.builder()
                .userRole(ROLE_ADMIN)
                .email(EMAIL_ADMIN)
                .build();

        User user = User.builder()
                .email(EMAIL_USER)
                .userRole(ROLE_USER)
                .userStatus(ACTIVE)
                .build();

        BDDMockito
                .given(userDetailService.getCurrentUser())
                .willReturn(admin);
        BDDMockito
                .given(authenticationService.getByEmailOrThrowException(EMAIL_USER))
                .willReturn(user);
        BDDMockito
                .given(userRepository.existsByEmail(EMAIL_USER))
                .willReturn(true);
        BDDMockito
                .given(userRepository.getByEmail(EMAIL_USER))
                .willReturn(Optional.of(user));
        BDDMockito
                .given(userRepository.save(user))
                .willReturn(user);
    }

    @Test
    void updateUserRole() {
        UpdateRoleUserDto dto = new UpdateRoleUserDto();
        dto.setEmail(EMAIL_USER);
        UserRole newRole = ROLE_USER;
        UserResponseDto response = adminService.updateUserRole(dto, newRole);

        assertEquals(newRole, response.getUserRole());
    }

    @Test
    void isCurrentUser(){
        boolean currentAdmin = adminService.isCurrentAdmin(EMAIL_ADMIN);

        assertTrue(currentAdmin);
    }

    @Test
    void notCurrentUser(){
        boolean currentAdmin = adminService.isCurrentAdmin(EMAIL_USER);

        assertFalse(currentAdmin);
    }
}