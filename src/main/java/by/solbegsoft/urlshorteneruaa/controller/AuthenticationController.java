package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.dto.LoginUserRequest;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateRequest;
import by.solbegsoft.urlshorteneruaa.service.AuthenticationService;
import by.solbegsoft.urlshorteneruaa.swagger.ApiPostLogin;
import by.solbegsoft.urlshorteneruaa.swagger.ApiPostRegistration;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "AuthenticationController", description = "End points for login and registration new account")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/registration")
    @PreAuthorize("!isAuthenticated()")
    @ApiPostRegistration
    public ResponseEntity<?> registration(@Valid @RequestBody UserCreateRequest userCreateRequest){
        return new ResponseEntity<>(authenticationService.save(userCreateRequest),
                HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @PreAuthorize("!isAuthenticated()")
    @ApiPostLogin
    public ResponseEntity<?> login(@RequestBody LoginUserRequest loginUserRequest){
        return new ResponseEntity<>(authenticationService.login(loginUserRequest),
                HttpStatus.OK);
    }
}