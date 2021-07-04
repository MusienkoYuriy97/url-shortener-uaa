package by.solbegsoft.urlshorteneruaa.service;



import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.model.dto.UpdateUserPasswordDto;
import by.solbegsoft.urlshorteneruaa.repository.ActivateKeyRepository;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Optional;

import static by.solbegsoft.urlshorteneruaa.model.UserStatus.*;


@Slf4j
@Service
@Transactional
public class UserService {
    private UserRepository userRepository;
    private ActivateKeyRepository activateKeyRepository;
    private JwtTokenProvider jwtTokenProvider;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       ActivateKeyRepository activateKeyRepository,
                       JwtTokenProvider jwtTokenProvider,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.activateKeyRepository = activateKeyRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
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
            throw new UserDataException("Passwords not matching");
        }
    }

    public void activate(long userId, String key){
        User user = userRepository.getById(userId);
        if (user == null){
            throw new UserDataException("User doesn't exist");
        }

        if (activateKeyRepository.existsByUserAndAndKey(user, key)){
            ActivateKey activateKey = activateKeyRepository.getByUser(user).get();
            long id = activateKey.getId();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expirationDate = activateKey.getExpirationDate();

            if (expirationDate.isAfter(now)){
                user.setUserStatus(ACTIVE);
                userRepository.save(user);
                activateKeyRepository.deleteActivateKeyById(id);
            }else {
                activateKeyRepository.deleteActivateKeyById(id);
                throw new RuntimeException("Activate key is expired");
            }
        }else {
            throw new RuntimeException("Active key not valid");
        }
    }
}