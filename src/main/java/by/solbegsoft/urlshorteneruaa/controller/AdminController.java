package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.model.dto.UpdateRoleUserDto;
import by.solbegsoft.urlshorteneruaa.model.dto.UserResponseDto;
import by.solbegsoft.urlshorteneruaa.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

@RestController
@RequestMapping(value = "${api.path}"+"admin")
public class AdminController {
    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PutMapping("/role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateRole(@Valid @RequestBody UpdateRoleUserDto dto){
        if (adminService.isCurrentAdmin(dto.getEmail())){
            return new ResponseEntity<>("You can't change the role for yourself", HttpStatus.CONFLICT);
        }
        UserResponseDto response = adminService.updateUserRole(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}