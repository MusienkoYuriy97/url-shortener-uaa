package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.dto.UpdatePasswordRequest;
import by.solbegsoft.urlshorteneruaa.service.UserService;
import by.solbegsoft.urlshorteneruaa.swagger.ApiGetActivate;
import by.solbegsoft.urlshorteneruaa.swagger.ApiPutUpdatePassword;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "${api.path}"+"/user")
@Tag(name = "UserController", description = "End points for update password and activate account after registration")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/password")
    @ApiPutUpdatePassword
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest){
        userService.updatePassword(updatePasswordRequest);
        return new ResponseEntity<>("Successfully updated password",
                HttpStatus.OK);
    }

    @GetMapping("/activate/{jwtActivateKey}")
    @ApiGetActivate
    public ResponseEntity<?> activate(@PathVariable String jwtActivateKey){
        userService.activate(jwtActivateKey);
        return new ResponseEntity<>("Successful activate account.",
                HttpStatus.ACCEPTED);
    }
}