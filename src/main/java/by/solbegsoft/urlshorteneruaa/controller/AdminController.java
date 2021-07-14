package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.dto.UpdateRoleUserDto;
import by.solbegsoft.urlshorteneruaa.dto.UserResponseDto;
import by.solbegsoft.urlshorteneruaa.service.AdminService;
import by.solbegsoft.urlshorteneruaa.swagger.ApiPutUpdateRole;
import by.solbegsoft.urlshorteneruaa.util.UserRole;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "${api.path}"+"/admin")
@Tag(name = "AdminController", description = "End points for users who has role ADMIN")
public class AdminController {
    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PutMapping("/role/{newRole}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiPutUpdateRole
    public ResponseEntity<?> updateRole(@Valid @RequestBody UpdateRoleUserDto updateRoleUserDto, @PathVariable(value = "newRole") UserRole newRole){
        if (adminService.isCurrentAdmin(updateRoleUserDto.getEmail())){
            return new ResponseEntity<>("You can't change the role for yourself", HttpStatus.CONFLICT);
        }

        UserResponseDto userResponseDto = adminService.updateUserRole(updateRoleUserDto, newRole);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }
}