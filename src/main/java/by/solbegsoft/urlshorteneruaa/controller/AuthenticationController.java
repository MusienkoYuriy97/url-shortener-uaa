package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.exception.NoActivatedAccountException;
import by.solbegsoft.urlshorteneruaa.model.TokenResponse;
import by.solbegsoft.urlshorteneruaa.model.dto.AuthenticationRequestDto;
import by.solbegsoft.urlshorteneruaa.model.dto.UserCreateDto;
import by.solbegsoft.urlshorteneruaa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "${api.path.auth}")
public class AuthenticationController {
    private UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/reg")
    public ResponseEntity<?> create(@Valid @RequestBody UserCreateDto userCreateDto){
        userService.save(userCreateDto);
        return new ResponseEntity<>("Successful added new user:", HttpStatus.ACCEPTED);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto request){
        try {
            TokenResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        }catch (AuthenticationException e){
            return new ResponseEntity<>("Wrong email or password", HttpStatus.FORBIDDEN);
        }catch (NoActivatedAccountException e){
            return new ResponseEntity<>("Please activate your account", e.getHttpStatus());
        }
    }

//    @GetMapping("/logout")
//    public void logout(HttpServletRequest request, HttpServletResponse response){
//        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
//        securityContextLogoutHandler.logout(request, response, null);
//    }
}
