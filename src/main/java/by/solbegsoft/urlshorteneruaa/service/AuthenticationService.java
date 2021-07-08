package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.common.StringGenerator;
import by.solbegsoft.urlshorteneruaa.exception.NoActivatedAccountException;
import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.mapper.UserRequestMapper;
import by.solbegsoft.urlshorteneruaa.mapper.UserResponseMapper;
import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.model.UserStatus;
import by.solbegsoft.urlshorteneruaa.model.dto.AuthenticationRequestDto;
import by.solbegsoft.urlshorteneruaa.model.dto.UserCreateDto;
import by.solbegsoft.urlshorteneruaa.model.dto.UserResponseDto;
import by.solbegsoft.urlshorteneruaa.repository.ActivateKeyRepository;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static by.solbegsoft.urlshorteneruaa.model.UserRole.*;
import static by.solbegsoft.urlshorteneruaa.model.UserStatus.BLOCKED;

@Slf4j
@Service
@Transactional
public class AuthenticationService {
    private UserRepository userRepository;
    private ActivateKeyRepository activateKeyRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private EmailService emailService;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 ActivateKeyRepository activateKeyRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 JwtTokenProvider jwtTokenProvider,
                                 EmailService emailService) {
        this.userRepository = userRepository;
        this.activateKeyRepository = activateKeyRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
    }

    public UserResponseDto save(UserCreateDto userCreateDto){
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            log.warn("User with this email already exist");
            throw new UserDataException("User with this email already exist");
        }
        User user = UserRequestMapper.INSTANCE.toUser(userCreateDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserRole(ROLE_USER);
        user.setUserStatus(BLOCKED);
        log.debug("Try save user " + user);
        User savedUser = userRepository.save(user);
        log.debug("Saved user " + savedUser);
        String email = savedUser.getEmail();
        emailService.sendEmail(email, savedUser.getFirstName(), saveActivateKey(email));
        UserResponseDto userResponseDto = UserResponseMapper.INSTANCE.toDto(savedUser);
        return userResponseDto;
    }

    private String saveActivateKey(String email){
        User user = userRepository.getByEmail(email).get();
        ActivateKey activateKey = new ActivateKey();
        activateKey.setKey(StringGenerator.generate(12));
        activateKey.setUser(user);
        ActivateKey save = activateKeyRepository.save(activateKey);
        log.info("Successfully added activated key for " + user.getEmail());
        return save.getKey();
    }

    public String login(AuthenticationRequestDto dto) throws NoActivatedAccountException {
        Optional<User> user = userRepository.getByEmail(dto.getEmail());

        if (user.isPresent() && UserStatus.BLOCKED.equals(user.get().getUserStatus())){
            log.warn("Account is BLOCKED.");
            throw new NoActivatedAccountException("Account not active.");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword()));
        String token = jwtTokenProvider.getPrefix() +
                       jwtTokenProvider.createToken(user.get().getId().toString(),
                                             user.get().getEmail(),
                                             user.get().getUserRole().name());
        log.info("Successfully generate token for " + dto.getEmail());
        return token;
    }
}