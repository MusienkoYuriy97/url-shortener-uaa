package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.model.UserRole;
import by.solbegsoft.urlshorteneruaa.model.dto.UpdateRoleUserDto;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.JwtTokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        adminService = new AdminService(userRepository, jwtTokenProvider);
    }

    @Test
    void updateUserRole() {
        User admin = new User();
        admin.setUserRole(UserRole.ADMIN);
        admin.setEmail("admin@gmail.com");
        User user = new User();
        user.setId(2L);
        user.setUserRole(UserRole.USER);
        user.setEmail("user@gmail.com");

        BDDMockito
                .given(jwtTokenProvider.getEmail("Bearer 12345"))
                .willReturn("admin@gmail.com");
        BDDMockito
                .given(userRepository.getByEmail("admin@gmail.com"))
                .willReturn(Optional.of(admin));
        BDDMockito
                .given(userRepository.existsById(2L))
                .willReturn(true);
        BDDMockito
                .given(userRepository.getById(2L))
                .willReturn(user);


        UpdateRoleUserDto dto = new UpdateRoleUserDto();
        dto.setUserId(2L);
        dto.setNewRole("ADMIN");
        User user1 = adminService.updateUserRole("Bearer 12345", dto);
        assertEquals(UserRole.ADMIN, user1.getUserRole());

    }
}