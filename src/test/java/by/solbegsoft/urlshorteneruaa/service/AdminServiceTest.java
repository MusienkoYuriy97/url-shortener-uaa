package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.dto.UpdateRoleRequest;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateResponse;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.UserDetailServiceImpl;
import by.solbegsoft.urlshorteneruaa.util.ObjectCreator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static by.solbegsoft.urlshorteneruaa.util.UserConstant.*;
import static by.solbegsoft.urlshorteneruaa.util.UserRole.*;
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
    @Autowired
    private ObjectCreator objectCreator;

    @Test
    void updateUserRole() {
        //mock
        User user = objectCreator.activeUser();
        UpdateRoleRequest updateRoleRequest = objectCreator.updateRoleRequest();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        //call service
        UserCreateResponse response = adminService.updateUserRole(updateRoleRequest);
        //assert
        assertEquals(ROLE_USER, response.getUserRole());
    }

    @Test
    void isCurrentUser(){
        //mock
        User currentAdmin = objectCreator.admin();
        when(userDetailService.getCurrentUser()).thenReturn(currentAdmin);
        //call service
        boolean isCurrentAdmin = adminService.isCurrentAdmin(ADMIN_UUID.toString());
        //assert
        assertTrue(isCurrentAdmin);
    }

    @Test
    void notCurrentUser(){
        //mock
        User currentAdmin = objectCreator.admin();
        when(userDetailService.getCurrentUser()).thenReturn(currentAdmin);
        //call service
        boolean isCurrentAdmin = adminService.isCurrentAdmin(USER_UUID.toString());
        //assert
        assertFalse(isCurrentAdmin);
    }
}