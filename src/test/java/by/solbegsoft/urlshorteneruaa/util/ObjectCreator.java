package by.solbegsoft.urlshorteneruaa.util;

import by.solbegsoft.urlshorteneruaa.dto.*;
import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.repository.UserRepository;
import by.solbegsoft.urlshorteneruaa.security.JwtTokenProvider;
import by.solbegsoft.urlshorteneruaa.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static by.solbegsoft.urlshorteneruaa.util.UserConstant.*;
import static by.solbegsoft.urlshorteneruaa.util.UserRole.*;
import static by.solbegsoft.urlshorteneruaa.util.UserStatus.*;

@Component
@PropertySource("classpath:constant.properties")
public class ObjectCreator {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Value("${JWT_PREFIX}")
    private String prefix;

    public UserCreateRequest userCreateRequest(){
        return UserCreateRequest.builder()
                .firstName(FIRST_USER_NAME)
                .lastName(LAST_USER_NAME)
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();
    }

    public User blockedUser(){
        return User.builder()
                .uuid(USER_UUID)
                .firstName(FIRST_USER_NAME)
                .lastName(LAST_USER_NAME)
                .email(USER_EMAIL)
                .password(passwordEncoder.encode(USER_PASSWORD))
                .userRole(ROLE_USER)
                .userStatus(BLOCKED)
                .build();
    }

    public User activeUser(){
        return User.builder()
                .uuid(USER_UUID)
                .firstName(FIRST_USER_NAME)
                .lastName(LAST_USER_NAME)
                .email(USER_EMAIL)
                .password(passwordEncoder.encode(USER_PASSWORD))
                .userRole(ROLE_USER)
                .userStatus(ACTIVE)
                .build();
    }

    public User admin(){
        return User.builder()
                .uuid(ADMIN_UUID)
                .firstName(FIRST_ADMIN_NAME)
                .lastName(LAST_ADMIN_NAME)
                .email(ADMIN_EMAIL)
                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                .userRole(ROLE_ADMIN)
                .userStatus(ACTIVE)
                .build();
    }

    public LoginUserRequest loginUserRequest(){
        return LoginUserRequest.builder()
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();
    }

    public LoginUserRequest loginUserRequestNotExist(){
        return LoginUserRequest.builder()
                .email(USER_EMAIL + "hello")
                .password(USER_PASSWORD + "hello")
                .build();
    }

    public UpdatePasswordRequest updatePasswordRequest(){
        return UpdatePasswordRequest.builder()
                .oldPassword(USER_PASSWORD)
                .newPassword(USER_NEW_PASSWORD)
                .repeatedPassword(USER_NEW_PASSWORD)
                .build();
    }

    public UpdatePasswordRequest updatePasswordRequestWrongPassword(){
        return UpdatePasswordRequest.builder()
                .oldPassword(USER_PASSWORD + "hello")
                .newPassword(USER_NEW_PASSWORD)
                .repeatedPassword(USER_NEW_PASSWORD)
                .build();
    }


    public User userAfterPasswordUpdate(){
        return User.builder()
                .uuid(USER_UUID)
                .firstName(FIRST_USER_NAME)
                .lastName(LAST_USER_NAME)
                .email(USER_EMAIL)
                .password(passwordEncoder.encode(USER_NEW_PASSWORD))
                .userRole(ROLE_USER)
                .userStatus(ACTIVE)
                .build();
    }

    public String jwtActivateKey(){
        return emailService.toJwt(SIMPLE_ACTIVATE_KEY);
    }

    public String notValidJwt(){
        return "not valid jwt";
    }

    public ActivateKey activateKey(){
        ActivateKey activateKey = new ActivateKey();
        activateKey.setUser(blockedUser());
        activateKey.setSimpleKey(SIMPLE_ACTIVATE_KEY);
        return activateKey;
    }

    public void buildSecurityContext(){
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken(USER_EMAIL, USER_PASSWORD);
        Authentication authenticate = authenticationManager.authenticate(authReq);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticate);
    }

    public UpdateRoleRequest updateRoleRequest(){
        return UpdateRoleRequest.builder()
                .uuid(USER_UUID.toString())
                .role(ROLE_USER.name())
                .build();
    }

    public UserCreateResponse updateRoleResponse(){
        return UserCreateResponse.builder()
                .email(USER_EMAIL)
                .firstName(FIRST_USER_NAME)
                .lastName(LAST_USER_NAME)
                .userStatus(ACTIVE)
                .userRole(ROLE_USER)
                .build();
    }

    public UserCreateResponse registerUserResponse(){
        return UserCreateResponse.builder()
                .email(USER_EMAIL)
                .firstName(FIRST_USER_NAME)
                .lastName(LAST_USER_NAME)
                .userStatus(BLOCKED)
                .userRole(ROLE_USER)
                .build();
    }

    public String toJson(Object o) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(o);
    }

    public String adminJwtToken(){
        Optional<User> admin = userRepository.getByEmail(ADMIN_EMAIL);

        return prefix + admin.map(
                user -> jwtTokenProvider.createToken(user.getUuid().toString(), user.getEmail(), user.getUserRole().name())
                        .get("access_token")
                )
                .orElse(null);
    }

    public String userJwtToken(){
        Optional<User> admin = userRepository.getByEmail(USER_EMAIL);
        return prefix + admin.map(
                user -> jwtTokenProvider.createToken(user.getUuid().toString(), user.getEmail(), user.getUserRole().name())
                        .get("access_token")
                )
                .orElse(null);
    }
}
