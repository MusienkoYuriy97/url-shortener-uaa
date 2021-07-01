package by.solbegsoft.urlshorteneruaa.controller;


import by.solbegsoft.urlshorteneruaa.model.dto.UpdateRoleUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "${api.path.admin}")
public class AdminController {

    @PutMapping("/update/role")
    public ResponseEntity<?> updateRole(@Valid @RequestBody UpdateRoleUserDto dto, HttpServletRequest request){
        return null;
    }
}
