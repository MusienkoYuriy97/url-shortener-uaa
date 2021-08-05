package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.dto.UpdatePasswordRequest;
import by.solbegsoft.urlshorteneruaa.exception.ActiveKeyNotValidException;
import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.repository.ActivateKeyRepository;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.UserDetailServiceImpl;
import by.solbegsoft.urlshorteneruaa.util.ObjectCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;

import java.util.Optional;

import static by.solbegsoft.urlshorteneruaa.util.UserConstant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectCreator objectCreator;
    @MockBean
    private UserDetailServiceImpl userDetailServiceImpl;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ActivateKeyRepository activateKeyRepository;

    @Test
    void updatePassword() {
        //mock
        UpdatePasswordRequest updatePasswordRequest = objectCreator.updatePasswordRequest();
        User beforeUpdate = objectCreator.activeUser();
        User afterUpdate = objectCreator.userAfterPasswordUpdate();
        when(userDetailServiceImpl.getCurrentUser()).thenReturn(beforeUpdate);
        when(userRepository.save(beforeUpdate)).thenReturn(afterUpdate);
        //call service
        userService.updatePassword(updatePasswordRequest);
        //verify
        verify(userRepository, times(1)).save(beforeUpdate);
        verify(userDetailServiceImpl, times(1)).getCurrentUser();

    }

    @Test
    void updatePasswordThrowException() {
        //mock
        UpdatePasswordRequest updatePasswordRequestWrongPassword = objectCreator.updatePasswordRequestWrongPassword();
        User beforeUpdate = objectCreator.activeUser();
        when(userDetailServiceImpl.getCurrentUser()).thenReturn(beforeUpdate);
        //call service and assert,verify
        assertThrows(UserDataException.class, () -> userService.updatePassword(updatePasswordRequestWrongPassword));
        verify(userDetailServiceImpl, times(1)).getCurrentUser();
    }

    @Test
    void activate() {
        //mock
        String jwtActivateKey = objectCreator.jwtActivateKey();
        when(activateKeyRepository.deleteActivateKeyBySimpleKey(SIMPLE_ACTIVATE_KEY)).thenReturn(1L);
        when(userRepository.save(objectCreator.blockedUser())).thenReturn(objectCreator.activeUser());
        when(activateKeyRepository.getBySimpleKey(SIMPLE_ACTIVATE_KEY)).thenReturn(Optional.of(objectCreator.activateKey()));
        //call service
        userService.activate(jwtActivateKey);
        //verify
        verify(userRepository, times(1)).save(any(User.class));
        verify(activateKeyRepository, times(1)).deleteActivateKeyBySimpleKey(SIMPLE_ACTIVATE_KEY);
    }

    @Test
    void activateKeyNotValid() {
        //jwt
        String notValidJwt = objectCreator.notValidJwt();
        //assert
        assertThrows(ActiveKeyNotValidException.class, () -> userService.activate(notValidJwt));
    }
}