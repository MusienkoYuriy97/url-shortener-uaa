package by.solbegsoft.urlshorteneruaa.security;

import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.util.ObjectCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;

import static by.solbegsoft.urlshorteneruaa.util.UserConstant.USER_EMAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserDetailServiceImplTest {
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private ObjectCreator objectCreator;
    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @Test
    void loadUserByUsername() {
        //mock
        when(userRepository.getByEmail(USER_EMAIL)).thenReturn(Optional.of(objectCreator.activeUser()));
        User user = objectCreator.activeUser();
        //call service
        UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(USER_EMAIL);
        //assert
        assertEquals(user.getEmail(), userDetails.getUsername());
    }

    @Test
    void loadUserByUsernameThrowException() {
        //mock
        when(userRepository.getByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        //call service and assert
        assertThrows(UsernameNotFoundException.class, () -> userDetailServiceImpl.loadUserByUsername(USER_EMAIL));
    }

    @Test
    void getCurrentEmail() {
        //mock
        when(userRepository.getByEmail(USER_EMAIL)).thenReturn(Optional.of(objectCreator.activeUser()));
        objectCreator.buildSecurityContext();
        //call service
        Optional<String> currentEmail = UserDetailServiceImpl.getCurrentEmail();
        //assert
        assertEquals(USER_EMAIL, currentEmail.get());
    }

    @Test
    void getCurrentUser() {
        //mock
        when(userRepository.getByEmail(USER_EMAIL)).thenReturn(Optional.of(objectCreator.activeUser()));
        when(userRepository.existsByEmail(USER_EMAIL)).thenReturn(true);
        objectCreator.buildSecurityContext();
        //call service
        User user = userDetailServiceImpl.getCurrentUser();
        //assert
        assertEquals(USER_EMAIL, user.getEmail());
    }
}