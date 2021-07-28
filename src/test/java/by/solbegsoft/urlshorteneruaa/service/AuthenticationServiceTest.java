package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.dto.LoginUserRequest;
import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateRequest;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateResponse;
import by.solbegsoft.urlshorteneruaa.repository.ActivateKeyRepository;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.util.ObjectCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;
import java.util.Optional;

import static by.solbegsoft.urlshorteneruaa.util.UserConstant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ObjectCreator objectCreator;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ActivateKeyRepository activateKeyRepository;
    @MockBean
    private EmailService emailService;

    @Test
    void saveUser() {
        //mock
        UserCreateRequest userCreateRequest = objectCreator.userCreateRequest();
        User blockedUser = objectCreator.blockedUser();
        when(userRepository.existsByEmail(USER_EMAIL)).thenReturn(false);
        when(userRepository.save(blockedUser)).thenReturn(blockedUser);
        when(userRepository.getByEmail(USER_EMAIL)).thenReturn(Optional.of(blockedUser));
        when(activateKeyRepository.save(any(ActivateKey.class))).thenReturn(new ActivateKey());
        doNothing().when(emailService).sendEmail(any(),any(),any());
        //call method
        UserCreateResponse userCreateResponse = authenticationService.save(userCreateRequest);
        //assert
        assertEquals(userCreateRequest.getFirstName(), userCreateResponse.getFirstName());
        assertEquals(userCreateRequest.getLastName(), userCreateResponse.getLastName());
        assertEquals(userCreateRequest.getEmail(), userCreateResponse.getEmail());
    }

    @Test
    void saveUserWithEmailAlreadyExist() {
        //mock
        UserCreateRequest userCreateRequest = objectCreator.userCreateRequest();
        when(userRepository.existsByEmail(USER_EMAIL)).thenReturn(true);
        //call method and assert throw
        assertThrows(UserDataException.class, () -> authenticationService.save(userCreateRequest));
    }
    
    @Test
    void login() {
        //mock
        LoginUserRequest loginUserRequest = objectCreator.loginUserRequest();
        User activeUser = objectCreator.activeUser();
        when(userRepository.getByEmail(USER_EMAIL)).thenReturn(Optional.of(activeUser));
        objectCreator.buildSecurityContext();
        //call method
        Map<String, String> tokenMap = authenticationService.login(loginUserRequest);
        //assert
        assertTrue(tokenMap.containsKey("access_token"));
        assertNotNull(tokenMap.get("access_token"));
    }

    @Test
    void loginUserNotExist() {
        //mock
        LoginUserRequest loginUserRequestNotExist = objectCreator.loginUserRequestNotExist();
        User activeUser = objectCreator.activeUser();
        when(userRepository.getByEmail(USER_EMAIL)).thenReturn(Optional.of(activeUser));
        //call method and assert throw
        assertThrows(UsernameNotFoundException.class, () -> authenticationService.login(loginUserRequestNotExist));
    }
}