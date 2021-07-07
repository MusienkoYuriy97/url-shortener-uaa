package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.model.dto.AuthenticationRequestDto;
import by.solbegsoft.urlshorteneruaa.model.dto.UserCreateDto;
import by.solbegsoft.urlshorteneruaa.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "${api.path}"+"auth")
public class AuthenticationController {
    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> save(@Valid @RequestBody UserCreateDto userCreateDto){
        User user = authenticationService.save(userCreateDto);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto request){
        try {
            String token = authenticationService.login(request);
            return new ResponseEntity<>(token, HttpStatus.OK);
        }catch (AuthenticationException e){
            return new ResponseEntity<>("Wrong email or password.", HttpStatus.FORBIDDEN);
        }
    }
}