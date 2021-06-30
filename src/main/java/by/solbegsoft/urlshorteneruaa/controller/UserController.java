package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.model.dto.UserCreateDto;
import by.solbegsoft.urlshorteneruaa.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    @PreAuthorize("hasAuthority('link:read')")
    public ResponseEntity<?> getAllLink(){
        return new ResponseEntity<>("hello",HttpStatus.OK);
    }

    @PostMapping("/reg")
    @PreAuthorize("hasAuthority(permitAll())")
    public ResponseEntity<?> registration(@Valid @RequestBody UserCreateDto userCreateDto){
        userService.save(userCreateDto);
        return new ResponseEntity<>("successful added", HttpStatus.ACCEPTED);
    }
}