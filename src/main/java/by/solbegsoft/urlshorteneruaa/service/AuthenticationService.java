package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.common.StringGenerator;
import by.solbegsoft.urlshorteneruaa.exception.NoActivatedAccountException;
import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.model.UserRole;
import by.solbegsoft.urlshorteneruaa.model.dto.AuthenticationRequestDto;
import by.solbegsoft.urlshorteneruaa.model.dto.UserCreateDto;
import by.solbegsoft.urlshorteneruaa.repository.ActivateKeyRepository;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 ActivateKeyRepository activateKeyRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.activateKeyRepository = activateKeyRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User save(UserCreateDto userCreateDto){
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            log.warn("User with this email already exist");
            throw new UserDataException("User with this email already exist");
        }
        User user = new User();
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setEmail(userCreateDto.getEmail());
        user.setUserRole(USER);
        user.setUserStatus(BLOCKED);
        log.debug("Try save user " + user);
        User save = userRepository.save(user);
        log.debug("Saved user " + save);
        return save;
    }

    public void setActivatedKey(String email){
        if (userRepository.existsByEmail(email)){
            User user = userRepository.getByEmail(email).get();
            ActivateKey activateKey = new ActivateKey();
            activateKey.setKey(StringGenerator.generate(12));
            activateKey.setUser(user);

            activateKeyRepository.save(activateKey);
        }else {
            throw new UserDataException("User doesn't exist");
        }
    }

    public String getToken(AuthenticationRequestDto request) throws NoActivatedAccountException {
        Optional<User> user = userRepository
                .getByEmail(request.getEmail());
        if (user.isPresent() & user.get().getUserStatus().equals(BLOCKED)){
            throw new NoActivatedAccountException("Account not active");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                request.getPassword()));
        String token = jwtTokenProvider.getPrefix() + jwtTokenProvider.createToken(request.getEmail(),
                user.get().getUserRole().name());
        return token;
    }
}