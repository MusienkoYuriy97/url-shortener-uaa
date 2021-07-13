package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.mapper.UserMapper;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.dto.UpdateRoleUserDto;
import by.solbegsoft.urlshorteneruaa.dto.UserResponseDto;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.UserDetailServiceImpl;
import org.checkerframework.checker.units.qual.A;
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
    @Autowired
    private AuthenticationService authenticationService;
    private AdminService adminService;

    @BeforeEach
    void setConstruct() {
        MockitoAnnotations.initMocks(this);
        adminService = new AdminService(userRepository, userDetailService, authenticationService, userMapper);
    }

    @BeforeEach
    void setUser() {
        User admin = User.builder()
                .userRole(ROLE_ADMIN)
                .email("admin@gmail.com")
                .build();

        User user = User.builder()
                .email("user@gmail.com")
                .userRole(ROLE_USER)
                .userStatus(ACTIVE)
                .build();

        BDDMockito
                .given(userDetailService.getCurrentUser())
                .willReturn(admin);
        BDDMockito
                .given(userRepository.existsByEmail("user@gmail.com"))
                .willReturn(true);
        BDDMockito
                .given(userRepository.getByEmail("user@gmail.com"))
                .willReturn(Optional.of(user));
        BDDMockito
                .given(userRepository.save(user))
                .willReturn(user);
    }

    @Test
    void updateUserRole() {
        UpdateRoleUserDto dto = new UpdateRoleUserDto();
        dto.setEmail("user@gmail.com");
        dto.setNewRole("ROLE_ADMIN");
        UserResponseDto response = adminService.updateUserRole(dto);

        assertEquals(dto.getNewRole(), response.getUserRole());
    }

    @Test
    void updateUserRoleThrowException() {
        UpdateRoleUserDto dto = new UpdateRoleUserDto();
        dto.setEmail("user@gmail.com");
        dto.setNewRole("ROLE_USER");

        assertThrows(UserDataException.class, () -> adminService.updateUserRole(dto));
    }

    @Test
    void isCurrentUser(){
        boolean currentAdmin = adminService.isCurrentAdmin("admin@gmail.com");

        assertTrue(currentAdmin);
    }

    @Test
    void notCurrentUser(){
        boolean currentAdmin = adminService.isCurrentAdmin("user@gmail.com");

        assertFalse(currentAdmin);
    }
}