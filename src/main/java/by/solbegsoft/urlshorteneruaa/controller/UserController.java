package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.model.dto.UpdateUserPasswordDto;
import by.solbegsoft.urlshorteneruaa.service.TokenService;
import by.solbegsoft.urlshorteneruaa.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "${api.path.user}")
public class UserController {
    private UserService userService;
    private TokenService tokenService;

    @Autowired
    public UserController(UserService userService,
                          TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @GetMapping("/home")
    public ResponseEntity<?> home(){
        return new ResponseEntity<>("Home page", HttpStatus.OK);
    }

    @PutMapping("/update/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdateUserPasswordDto dto,
                                            HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        User user = tokenService.getUserByToken(bearerToken);
        userService.updatePassword(user, dto);
        return ResponseEntity.ok()
                             .build();
    }

    @GetMapping("/activate/{userId}/{activateKey}")
    public ResponseEntity<?> activate(@PathVariable long userId,
                                      @PathVariable String activateKey){
        userService.activate(userId, activateKey);
        return new ResponseEntity<>("Successful activate account.",
                HttpStatus.ACCEPTED);
    }
}