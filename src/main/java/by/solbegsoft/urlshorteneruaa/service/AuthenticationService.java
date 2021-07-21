package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.common.StringGenerator;
import by.solbegsoft.urlshorteneruaa.exception.NoActivatedAccountException;
import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.mapper.UserMapper;
import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.util.UserStatus;
import by.solbegsoft.urlshorteneruaa.dto.LoginUserRequest;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateRequest;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateResponse;
import by.solbegsoft.urlshorteneruaa.repository.ActivateKeyRepository;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static by.solbegsoft.urlshorteneruaa.util.UserRole.*;
import static by.solbegsoft.urlshorteneruaa.util.UserStatus.BLOCKED;

@Slf4j
@Service
public class AuthenticationService {
    private UserRepository userRepository;
    private ActivateKeyRepository activateKeyRepository;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private EmailService emailService;
    private UserMapper userMapper;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 ActivateKeyRepository activateKeyRepository,
                                 AuthenticationManager authenticationManager,
                                 JwtTokenProvider jwtTokenProvider,
                                 EmailService emailService,
                                 UserMapper userMapper) {
        this.userRepository = userRepository;
        this.activateKeyRepository = activateKeyRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserCreateResponse save(UserCreateRequest userCreateRequest){
        if (userRepository.existsByEmail(userCreateRequest.getEmail())) {
            log.warn("User with this email already exist");
            throw new UserDataException("User with this email already exist");
        }
        User user = userMapper.toUser(userCreateRequest);
        user.setUserRole(ROLE_USER);
        user.setUserStatus(BLOCKED);
        log.debug("Try save user " + user);
        userRepository.save(user);

        String simpleKey = saveSimpleKey(user.getEmail());
        log.debug(String.format("Send email to email:%s; first name: %s; simpleKey:%s", user.getEmail(), user.getFirstName(), simpleKey));
        emailService.sendEmail(user.getEmail(), user.getFirstName(), simpleKey);
        log.info("Successfully register a new user and send activate key");
        return userMapper.toDto(user);
    }

    private String saveSimpleKey(String email){
        User user = getByEmailOrThrowException(email);
        ActivateKey activateKey = new ActivateKey();
        activateKey.setSimpleKey(StringGenerator.generate(12));
        activateKey.setUser(user);
        return activateKeyRepository.save(activateKey).getSimpleKey();
    }

    public String login(LoginUserRequest loginUserRequest){
        User user;
        try {
            user = getByEmailOrThrowException(loginUserRequest.getEmail());
            log.debug("Get by email:" + user);
        }catch (UserDataException ex){
            log.warn(String.format("User with emil:%s doesn't exist", loginUserRequest.getEmail()));
            throw new UsernameNotFoundException("Wrong email/password");
        }

        if (UserStatus.BLOCKED.equals(user.getUserStatus())){
            log.warn("Account is BLOCKED.");
            throw new NoActivatedAccountException("Account not active.");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserRequest.getEmail(),
                                                                                    loginUserRequest.getPassword()));
        String token = jwtTokenProvider.getPrefix() +
                       jwtTokenProvider.createToken(user.getUuid().toString(),
                                             user.getEmail(),
                                             user.getUserRole().name());
        log.info("Successfully generate token for " + loginUserRequest.getEmail());
        return token;
    }

    private User getByEmailOrThrowException(String email){
        if (email == null){
            throw new UserDataException("Email is null.");
        }

        return userRepository
                .getByEmail(email)
                .orElseThrow(() -> new UserDataException("User with this email doesn't exist"));
    }
}