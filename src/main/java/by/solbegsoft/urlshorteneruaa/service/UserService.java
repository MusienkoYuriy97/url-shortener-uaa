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
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static by.solbegsoft.urlshorteneruaa.util.UserStatus.*;

@Slf4j
@Service
@PropertySource("classpath:constant.properties")
public class UserService {
    @Value("${JWT_SECRET}")
    private String JWT_SECRET;
    @Value("${JWT_CLAIM_SIMPLE_KEY}")
    private String JWT_CLAIM_SIMPLE_KEY;
    @Value("${JWT_CLAIM_EXPIRATION}")
    private String JWT_CLAIM_EXPIRATION;

    private final UserRepository userRepository;
    private final ActivateKeyRepository activateKeyRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailServiceImpl userDetailService;
    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository,
                       ActivateKeyRepository activateKeyRepository,
                       PasswordEncoder passwordEncoder,
                       @Qualifier("userDetailsServiceImpl") UserDetailServiceImpl userDetailService,
                       AuthenticationService authenticationService,
                       EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.activateKeyRepository = activateKeyRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailService = userDetailService;
        this.authenticationService = authenticationService;
        this.emailService = emailService;
    }

    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = userDetailService.getCurrentUser();
        log.debug("Current user email is " + user.getEmail());

        boolean matchesOldPasswords = passwordEncoder.matches(updatePasswordRequest.getOldPassword(),
                                                              user.getPassword());
        if (matchesOldPasswords
                && updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getRepeatedPassword())){

            user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
            userRepository.save(user);
            log.info("Successfully updated password");
        }else {
            log.warn("Passwords entered incorrectly.");
            throw new UserDataException("Passwords entered incorrectly.");
        }
    }

    @Transactional
    public void activate(String jwtActivateKey){
        if(jwtActivateKey == null){
            log.warn("Activate key cannot be null.");
            throw new ActiveKeyNotValidException("Activate key cannot be null.");
        }
        Map<String, Object> claimsMap = getClaimsMap(jwtActivateKey);
        String simpleKey = claimsMap.get(JWT_CLAIM_SIMPLE_KEY).toString();
        Object expiration = claimsMap.get(JWT_CLAIM_EXPIRATION);
        if (isExpired(expiration)){
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

    @Transactional
    public void resetActivateKey(String jwtActivateKeyOld) {
        if(jwtActivateKeyOld == null){
            log.warn("Activate key cannot be null.");
            throw new ActiveKeyNotValidException("Activate key cannot be null.");
        }
        Map<String, Object> claimsMap = getClaimsMap(jwtActivateKeyOld);
        String simpleKeyOld = claimsMap.get(JWT_CLAIM_SIMPLE_KEY).toString();
        Object expiration = claimsMap.get(JWT_CLAIM_EXPIRATION);
        if (isExpired(expiration)){
            //get old activate key from database
            ActivateKey activateKeyOld = getActivateKeyOrThrowException(simpleKeyOld);
            String email = activateKeyOld.getUser().getEmail();
            String firstName = activateKeyOld.getUser().getFirstName();
            //generate and save to db new key
            String simpleActivateKey = authenticationService.saveSimpleKey(email);
            emailService.sendEmail(email, firstName, simpleActivateKey);
            activateKeyRepository.deleteActivateKeyBySimpleKey(activateKeyOld.getSimpleKey());
        }else {
            log.warn("Activate key isn't expired");
            throw new ActiveKeyNotValidException("Activate key isn't expired");
        }
    }

    private Map<String, Object> getClaimsMap(String jwtKey){
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
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