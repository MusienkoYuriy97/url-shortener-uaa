package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.mapper.UserMapper;
import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.dto.AuthenticationRequestDto;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateDto;
import by.solbegsoft.urlshorteneruaa.dto.UserResponseDto;
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
                .email("musienko97@gmail.com")
                .firstName("Yuriy")
                .lastName("Musienko")
                .password(passwordEncoder.encode("12345"))
                .userRole(ROLE_USER)
                .userStatus(ACTIVE)
                .build();

        ActivateKey activateKey = new ActivateKey();
        activateKey.setSimpleKey("hello");

        BDDMockito
                .given(activateKeyRepository.save(any(ActivateKey.class)))
                .willReturn(activateKey);

        BDDMockito
                .doNothing().when(emailService)
                .sendEmail(eq(user.getEmail()), eq(user.getFirstName()), anyObject());

        BDDMockito
                .given(userRepository.existsByEmail("musienko97@gmail.com"))
                .willReturn(false);

        BDDMockito
                .given(userRepository.getByEmail("musienko97@gmail.com"))
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
        UserCreateDto dto = new UserCreateDto();
        dto.setFirstName("Yuriy");
        dto.setLastName("Musienko");
        dto.setEmail("musienko97@gmail.com");
        dto.setPassword("12345");
        UserResponseDto save = authenticationService.save(dto);
        assertEquals(dto.getFirstName(), save.getFirstName());
        assertEquals(dto.getLastName(), save.getLastName());
        assertEquals(dto.getEmail(), save.getEmail());
    }


    @Test
    void login() {
        AuthenticationRequestDto dto = new AuthenticationRequestDto();
        dto.setEmail("musienko97@gmail.com");
        dto.setPassword("12345");
        authenticationService.login(dto);
    }
}