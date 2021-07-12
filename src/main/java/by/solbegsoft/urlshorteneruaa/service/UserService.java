package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.exception.ActiveKeyNotValidException;
import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.model.dto.UpdateUserPasswordDto;
import by.solbegsoft.urlshorteneruaa.repository.ActivateKeyRepository;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.UserDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import static by.solbegsoft.urlshorteneruaa.model.UserStatus.*;

@Slf4j
@Service
@Transactional
public class UserService {
    private UserRepository userRepository;
    private ActivateKeyRepository activateKeyRepository;
    private PasswordEncoder passwordEncoder;
    private UserDetailServiceImpl userDetailService;

    @Autowired
    public UserService(UserRepository userRepository,
                       ActivateKeyRepository activateKeyRepository,
                       PasswordEncoder passwordEncoder,
                       @Qualifier("userDetailsServiceImpl") UserDetailServiceImpl userDetailService) {
        this.userRepository = userRepository;
        this.activateKeyRepository = activateKeyRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailService = userDetailService;
    }

    public void updatePassword(UpdateUserPasswordDto dto) {
        User currentUser = userDetailService.getCurrentUser();
        log.debug("current user is " + currentUser.getEmail());

        boolean matchesOldPasswords = passwordEncoder.matches(dto.getOldPassword(),
                                                              currentUser.getPassword());
        if (matchesOldPasswords
                && dto.getNewPassword().equals(dto.getRepeatedPassword())){
            currentUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            userRepository.save(currentUser);
        }else {
            log.warn("Passwords entered incorrectly.");
            throw new UserDataException("Passwords entered incorrectly.");
        }
    }

    public void activate(String key){
        if (activateKeyRepository.existsByKey(key)) {
            ActivateKey activateKey = activateKeyRepository.getByKey(key).get();
            User user = activateKey.getUser();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expirationDate = activateKey.getExpirationDate();

            if (now.isAfter(expirationDate)){
                activateKeyRepository.deleteActivateKeyByKey(key);
                throw new ActiveKeyNotValidException("Activate key is expired.");
            }
            user.setUserStatus(ACTIVE);
            userRepository.save(user);
            activateKeyRepository.deleteActivateKeyByKey(key);
        }else {
            throw new ActiveKeyNotValidException("Activate key not valid.");
        }
    }
}