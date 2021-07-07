package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.model.dto.UpdateUserPasswordDto;
import by.solbegsoft.urlshorteneruaa.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "${api.path}"+"user")
public class UserController {
    private UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public ResponseEntity<?> home(){
        return new ResponseEntity<>("Home page", HttpStatus.OK);
    }

    @PutMapping("/update/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdateUserPasswordDto dto){
       // userService.updatePassword(user, dto);
        return ResponseEntity.ok()
                             .build();
    }

    @GetMapping("/{activateKey}")
    public ResponseEntity<?> activate(@PathVariable String activateKey){
        userService.activate(activateKey);
        return new ResponseEntity<>("Successful activate account.",
                HttpStatus.ACCEPTED);
    }
}