package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.dto.UpdateRoleRequest;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateResponse;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.UserDetailServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.Optional;
import java.util.UUID;

import static by.solbegsoft.urlshorteneruaa.util.UserRole.*;
import static by.solbegsoft.urlshorteneruaa.util.UserStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdminServiceTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private AdminService adminService;
    private final UUID UUID_ADMIN = UUID.fromString("dfb5305a-24ca-42f2-84e7-3edc5edd8e29");
    private final UUID UUID_USER = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Test
    void updateUserRole() {
        //when
        User user = User.builder()
                .userRole(ROLE_USER)
                .userStatus(ACTIVE)
                .build();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        //given
        UpdateRoleRequest updateRoleRequest = new UpdateRoleRequest();
        updateRoleRequest.setUuid(UUID_USER.toString());
        updateRoleRequest.setRole("ROLE_USER");
        UserCreateResponse response = adminService.updateUserRole(updateRoleRequest);
        //than
        assertEquals("ROLE_USER", response.getUserRole().name());
    }

    @Test
    void isCurrentUser(){
        //when
        User currentAdmin = User.builder()
                .uuid(UUID_ADMIN)
                .userRole(ROLE_ADMIN)
                .build();
        when(userDetailService.getCurrentUser()).thenReturn(currentAdmin);
        //given
        boolean isCurrentAdmin = adminService.isCurrentAdmin(UUID_ADMIN.toString());
        //than
        assertTrue(isCurrentAdmin);
    }

    @Test
    void notCurrentUser(){
        //when
        User currentAdmin = User.builder()
                .uuid(UUID_ADMIN)
                .userRole(ROLE_ADMIN)
                .build();
        when(userDetailService.getCurrentUser()).thenReturn(currentAdmin);
        //given
        boolean isCurrentAdmin = adminService.isCurrentAdmin(UUID_USER.toString());
        //than
        assertFalse(isCurrentAdmin);
    }
}