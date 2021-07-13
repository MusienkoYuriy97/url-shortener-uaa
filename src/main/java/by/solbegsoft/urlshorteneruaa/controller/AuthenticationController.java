package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.dto.AuthenticationRequestDto;
import by.solbegsoft.urlshorteneruaa.dto.UserCreateDto;
import by.solbegsoft.urlshorteneruaa.dto.UserResponseDto;
import by.solbegsoft.urlshorteneruaa.service.AuthenticationService;
import by.solbegsoft.urlshorteneruaa.swagger.ApiPostRegistrationMethod;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "${api.path}"+"/auth")
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/registration")
    @ApiPostRegistrationMethod
    public ResponseEntity<?> registration(@Valid @RequestBody UserCreateDto userCreateDto){
        UserResponseDto userResponseDto = authenticationService.save(userCreateDto);
        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    @PreAuthorize("!isAuthenticated()")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto authenticationRequestDto){
        String jwtBearerToken = authenticationService.login(authenticationRequestDto);
        return new ResponseEntity<>(jwtBearerToken, HttpStatus.OK);
    }
}