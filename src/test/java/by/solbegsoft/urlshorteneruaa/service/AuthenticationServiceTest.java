package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.mapper.UserMapper;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.dto.LoginUserRequest;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateRequest;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateResponse;
import by.solbegsoft.urlshorteneruaa.repository.ActivateKeyRepository;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static by.solbegsoft.urlshorteneruaa.util.UserStatus.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;
import static by.solbegsoft.urlshorteneruaa.util.UserRole.ROLE_USER;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
class AuthenticationServiceTest {
    private AuthenticationService authenticationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ActivateKeyRepository activateKeyRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final String FIRST_NAME = "Yury";
    private final String LAST_NAME = "Musienko";
    private final String EMAIL = "musienko97@gmail.com";
    private final String PASSWORD = "12345";

    @BeforeEach
    void setConstruct() {
        MockitoAnnotations.initMocks(this);
        authenticationService = new AuthenticationService(userRepository,
                activateKeyRepository,
                authenticationManager,
                jwtTokenProvider,
                emailService,
                userMapper);
    }

    @BeforeEach
    void setUser() {
        User user = User.builder()
                .uuid(UUID.randomUUID())
                .email(EMAIL)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(passwordEncoder.encode(PASSWORD))
                .userRole(ROLE_USER)
                .userStatus(ACTIVE)
                .build();

        BDDMockito
                .doNothing().when(emailService)
                .sendEmail(eq(user.getEmail()), eq(user.getFirstName()), anyObject());

        BDDMockito
                .given(userRepository.existsByEmail(EMAIL))
                .willReturn(false);

        BDDMockito
                .given(userRepository.getByEmail(EMAIL))
                .willReturn(Optional.of(user));

        BDDMockito
                .given(userRepository.save(any(User.class)))
                .willReturn(user);
        BDDMockito
                .given(authenticationManager.authenticate(any()))
                .willReturn(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
    }


    @Test
    void save() {
        UserCreateRequest dto = new UserCreateRequest();
        dto.setFirstName(FIRST_NAME);
        dto.setLastName(LAST_NAME);
        dto.setEmail(EMAIL);
        dto.setPassword(PASSWORD);
        UserCreateResponse save = authenticationService.save(dto);
        assertEquals(dto.getFirstName(), save.getFirstName());
        assertEquals(dto.getLastName(), save.getLastName());
        assertEquals(dto.getEmail(), save.getEmail());
    }


    @Test
    void login() {
        LoginUserRequest dto = new LoginUserRequest();
        dto.setEmail(EMAIL);
        dto.setPassword(PASSWORD);
        authenticationService.login(dto);
    }
}