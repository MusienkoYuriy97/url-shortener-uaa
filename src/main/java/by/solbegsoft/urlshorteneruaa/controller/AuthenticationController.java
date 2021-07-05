package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.model.dto.AuthenticationRequestDto;
import by.solbegsoft.urlshorteneruaa.model.dto.UserCreateDto;
import by.solbegsoft.urlshorteneruaa.service.AuthenticationService;
import by.solbegsoft.urlshorteneruaa.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "${api.path.auth}")
public class AuthenticationController {
    private AuthenticationService authenticationService;
    private EmailService emailService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService,
                                    EmailService emailService) {
        this.authenticationService = authenticationService;
        this.emailService = emailService;
    }

    @PostMapping("/reg")
    public ResponseEntity<?> save(@Valid @RequestBody UserCreateDto userCreateDto){
        User user = authenticationService.save(userCreateDto);
        String link = authenticationService.saveActivatedKey(user.getEmail());
        emailService.sendEmail(user.getEmail(),"activate account", link);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto request,
                                   HttpServletResponse response){
        try {
            String token = authenticationService.login(request);
            response.addHeader("Authorization", token);
            return ResponseEntity.ok().build();
        }catch (AuthenticationException e){
            return new ResponseEntity<>("Wrong email or password.", HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request,
                       HttpServletResponse response){
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
        cookieClearingLogoutHandler.logout(request, response, null);
        securityContextLogoutHandler.logout(request, response, null);
    }
}