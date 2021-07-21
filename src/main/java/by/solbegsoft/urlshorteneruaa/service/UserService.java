package by.solbegsoft.urlshorteneruaa.service;

import by.solbegsoft.urlshorteneruaa.exception.ActiveKeyNotValidException;
import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.dto.UpdatePasswordRequest;
import by.solbegsoft.urlshorteneruaa.repository.ActivateKeyRepository;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.UserDetailServiceImpl;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static by.solbegsoft.urlshorteneruaa.util.UserStatus.*;

@Slf4j
@Service
public class UserService {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.claimSimpleKey}")
    private String claimSimpleKey;
    @Value("${jwt.claimExpiration}")
    private String claimExpiration;
    private final UserRepository userRepository;
    private final ActivateKeyRepository activateKeyRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailServiceImpl userDetailService;

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

    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User currentUser = userDetailService.getCurrentUser();
        log.debug("Current user email is " + currentUser.getEmail());

        boolean matchesOldPasswords = passwordEncoder.matches(updatePasswordRequest.getOldPassword(),
                                                              currentUser.getPassword());
        if (matchesOldPasswords
                && updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getRepeatedPassword())){

            currentUser.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
            userRepository.save(currentUser);
            log.info("Successfully updated password");
        }else {
            log.warn("Passwords entered incorrectly.");
            throw new UserDataException("Passwords entered incorrectly.");
        }
    }

    @Transactional
    public void activate(String jwtKey){
        if(jwtKey == null){
            log.warn("Activate key cannot be null.");
            throw new ActiveKeyNotValidException("Activate key cannot be null.");
        }
        Map<String, Object> claimsMap = getClaimsMap(jwtKey);
        String simpleKey = claimsMap.get(claimSimpleKey).toString();
        Object expiration = claimsMap.get(claimExpiration);
        if (isExpired(expiration)){
            activateKeyRepository.deleteActivateKeyBySimpleKey(simpleKey);
            log.warn("Activate key is expired");
            throw new ActiveKeyNotValidException("Activate key is expired");
        } else {
            ActivateKey activateKey = getActivateKeyOrThrowException(simpleKey);
            User user = activateKey.getUser();
            user.setUserStatus(ACTIVE);
            userRepository.save(user);
            activateKeyRepository.deleteActivateKeyBySimpleKey(simpleKey);
        }
    }

    private Map<String, Object> getClaimsMap(String jwtKey){
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtKey);
            return new HashMap<>(claimsJws.getBody());
        }catch (JwtException | IllegalArgumentException e){
            throw new ActiveKeyNotValidException("Activate key not valid");
        }
    }

    private boolean isExpired(Object expiration) {
        LocalDateTime expirationDate = LocalDateTime.parse(expiration.toString());
        return LocalDateTime.now().isAfter(expirationDate);
    }

    private ActivateKey getActivateKeyOrThrowException(String simpleKey) {
        return activateKeyRepository.getBySimpleKey(simpleKey)
                .orElseThrow(() -> new ActiveKeyNotValidException("Activate key doesn't exist"));
    }
}