package by.solbegsoft.urlshorteneruaa.service;


import by.solbegsoft.urlshorteneruaa.exception.NoActivatedAccountException;
import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.model.TokenResponse;
import by.solbegsoft.urlshorteneruaa.model.User;

import by.solbegsoft.urlshorteneruaa.model.dto.AuthenticationRequestDto;
import by.solbegsoft.urlshorteneruaa.model.dto.UpdateUserPasswordDto;
import by.solbegsoft.urlshorteneruaa.model.dto.UserCreateDto;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static by.solbegsoft.urlshorteneruaa.model.UserRole.USER;
import static by.solbegsoft.urlshorteneruaa.model.UserStatus.*;


@Slf4j
@Service
@Transactional
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void save(UserCreateDto userCreateDto){
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
        user.setUserStatus(ACTIVE);
        log.debug("Try save user " + user);
        User save = userRepository.save(user);
        log.debug("Saved user " + save);
    }

    public TokenResponse login(AuthenticationRequestDto request) throws NoActivatedAccountException {
        Optional<User> user = userRepository
                .getByEmail(request.getEmail());
//        if (user.isPresent() & user.get().getUserStatus().equals(BLOCKED)){
//            throw new NoActivatedAccountException();
//        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                                                                                   request.getPassword()));
        String token = jwtTokenProvider.createToken(request.getEmail(),
                                                    user.get().getUserRole().name());
        return new TokenResponse("email:" + request.getEmail(),
                                 "token:" + token);
    }

    public void activate(String activateKey){

    }

    public void updatePassword(String token, UpdateUserPasswordDto dto) {
        if (dto.getPassword().equals(dto.getRepeatedPassword())){
            String email = jwtTokenProvider.getEmail(token);
            Optional<User> byEmail = userRepository.getByEmail(email);
            if (byEmail.isPresent()) {
                User user = byEmail.get();
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
                userRepository.save(user);
            }
        }else {
            throw new UserDataException("Password not matching");
        }
    }
}