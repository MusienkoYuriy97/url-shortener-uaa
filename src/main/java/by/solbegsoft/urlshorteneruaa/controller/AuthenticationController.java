package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.dto.AuthenticationRequestDto;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateDto;
import by.solbegsoft.urlshorteneruaa.dto.UserResponseDto;
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
@RequestMapping(value = "${api.path}"+"/auth")
@Tag(name = "AuthenticationController", description = "End points for login and registration new account")
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/registration")
    @ApiPostRegistration
    public ResponseEntity<?> registration(@Valid @RequestBody UserCreateDto userCreateDto){
        UserResponseDto userResponseDto = authenticationService.save(userCreateDto);
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @PreAuthorize("!isAuthenticated()")
    @ApiPostLogin
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto authenticationRequestDto){
        String jwtBearerToken = authenticationService.login(authenticationRequestDto);
        return new ResponseEntity<>(jwtBearerToken, HttpStatus.OK);
    }
}