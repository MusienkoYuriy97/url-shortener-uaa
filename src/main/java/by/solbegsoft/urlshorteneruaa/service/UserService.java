package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.exception.ActiveKeyNotValidException;
import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.model.dto.UpdateUserPasswordDto;
import by.solbegsoft.urlshorteneruaa.repository.ActivateKeyRepository;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserService(UserRepository userRepository,
                       ActivateKeyRepository activateKeyRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.activateKeyRepository = activateKeyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void updatePassword(User user, UpdateUserPasswordDto dto) {
        boolean matchesOldPasswords = passwordEncoder.matches(dto.getOldPassword(),user.getPassword());
        if (matchesOldPasswords && dto.getNewPassword().equals(dto.getRepeatedPassword())){
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            userRepository.save(user);
        }else {
            log.warn("Passwords entered incorrectly.");
            throw new UserDataException("Passwords entered incorrectly.");
        }
    }

    public void activate(long userId, String key){
        User user = userRepository.getById(userId);

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
                log.warn("Activate key is expired. Now:" + now + " Expiration:"+expirationDate);
                throw new ActiveKeyNotValidException("Activate key is expired.");
            }
        }else {
            log.warn("Active link not valid. User id:" + userId + " key:" + key);
            throw new ActiveKeyNotValidException("Active link not valid.");
        }
    }
}