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

import java.util.Map;

import static by.solbegsoft.urlshorteneruaa.util.UserRole.ROLE_USER;
import static by.solbegsoft.urlshorteneruaa.util.UserStatus.BLOCKED;

@Slf4j
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final ActivateKeyRepository activateKeyRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final UserMapper userMapper;

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
        //user save
        User user = userMapper.toUser(userCreateRequest);
        user.setUserRole(ROLE_USER);
        user.setUserStatus(BLOCKED);
        log.debug("Try save user {}", user);
        userRepository.save(user);
        //send activate key to email
        String simpleKey = saveSimpleKey(user.getEmail());
        log.debug("Send email to email:{}; first name: {}; simpleKey:{}", user.getEmail(), user.getFirstName(), simpleKey);
        emailService.sendEmail(user.getEmail(), user.getFirstName(), simpleKey);

        log.info("Successfully register a new user and send activate key");
        return userMapper.toDto(user);
    }

    public String saveSimpleKey(String email){
        User user = getByEmailOrThrowException(email);
        ActivateKey activateKey = new ActivateKey();
        activateKey.setSimpleKey(StringGenerator.generate(12));
        activateKey.setUser(user);
        return activateKeyRepository.save(activateKey).getSimpleKey();
    }

    public Map<String, String> login(LoginUserRequest loginUserRequest){
        User user;
        try {
            user = getByEmailOrThrowException(loginUserRequest.getEmail());
            log.debug("Get by email:" + user);
        }catch (UserDataException ex){
            log.warn("User with emil:{} doesn't exist", loginUserRequest.getEmail());
            throw new UsernameNotFoundException("Wrong email/password");
        }

        if (UserStatus.BLOCKED.equals(user.getUserStatus())){
            log.warn("Account is BLOCKED.");
            throw new NoActivatedAccountException("Account not active.");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserRequest.getEmail(),
                                                                                    loginUserRequest.getPassword()));
        Map<String, String> tokenMap = jwtTokenProvider.createToken(user.getUuid().toString(),
                                             user.getEmail(),
                                             user.getUserRole().name());
        log.info("Successfully generate jwtToken for {}", loginUserRequest.getEmail());
        return tokenMap;
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