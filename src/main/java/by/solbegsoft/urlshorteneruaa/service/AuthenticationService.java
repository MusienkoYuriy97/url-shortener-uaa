package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.common.StringGenerator;
import by.solbegsoft.urlshorteneruaa.exception.NoActivatedAccountException;
import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.mapper.UserMapper;
import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.util.UserStatus;
import by.solbegsoft.urlshorteneruaa.dto.AuthenticationRequestDto;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateDto;
import by.solbegsoft.urlshorteneruaa.dto.UserResponseDto;
import by.solbegsoft.urlshorteneruaa.repository.ActivateKeyRepository;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    public UserResponseDto save(UserCreateDto userCreateDto){
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            log.warn("User with this email already exist");
            throw new UserDataException("User with this email already exist");
        }
        User user = userMapper.toUser(userCreateDto);
        user.setUserRole(ROLE_USER);
        user.setUserStatus(BLOCKED);
        log.debug("Try save user " + user);
        User savedUser = userRepository.save(user);
        log.debug("Saved user " + savedUser);
        String email = savedUser.getEmail();
        String simpleKey = saveSimpleKey(email);
        emailService.sendEmail(email, savedUser.getFirstName(), simpleKey);
        return userMapper.toDto(savedUser);
    }

    private String saveSimpleKey(String email){
        User user = getByEmailOrThrowException(email);
        ActivateKey activateKey = new ActivateKey();
        String simpleKey = StringGenerator.generate(12);
        activateKey.setSimpleKey(simpleKey);
        activateKey.setUser(user);
        activateKeyRepository.save(activateKey);
        log.info("Successfully added activated key for " + user.getEmail());
        return simpleKey;
    }

    public String login(AuthenticationRequestDto dto) throws NoActivatedAccountException {
        User user = getByEmailOrThrowException(dto.getEmail());

        if (UserStatus.BLOCKED.equals(user.getUserStatus())){
            log.warn("Account is BLOCKED.");
            throw new NoActivatedAccountException("Account not active.");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword()));
        String token = jwtTokenProvider.getPrefix() +
                       jwtTokenProvider.createToken(user.getUuid().toString(),
                                             user.getEmail(),
                                             user.getUserRole().name());
        log.info("Successfully generate token for " + dto.getEmail());
        return token;
    }

    public User getByEmailOrThrowException(String email){
        if (email == null){
            throw new UserDataException("Email is null.");
        }

        return userRepository
                .getByEmail(email)
                .orElseThrow(() -> new UserDataException("User with this email doesn't exist"));
    }
}