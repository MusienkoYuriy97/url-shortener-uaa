package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.model.dto.UpdateUserPasswordDto;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public ResponseEntity<?> home(){
        return new ResponseEntity<>("Home page", HttpStatus.OK);
    }

    @PutMapping("/update/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdateUserPasswordDto dto,
                                            HttpServletRequest request){
        String token = request.getHeader("Authorization");
        userService.updatePassword(token, dto);
        return ResponseEntity.ok()
                             .build();
    }

    @PostMapping("/{activateKey}")
    public ResponseEntity<?> activate(@PathVariable String activateKey){
        userService.activate(activateKey);
        return new ResponseEntity<>("Successful activate account.",
                HttpStatus.ACCEPTED);
    }
}