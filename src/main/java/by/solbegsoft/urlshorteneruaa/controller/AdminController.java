package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.model.dto.UpdateRoleUserDto;
import by.solbegsoft.urlshorteneruaa.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping(value = "${api.path.admin}")
public class AdminController {
    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PutMapping("/update/role")
    @PreAuthorize("hasAuthority('role:update')")
    public ResponseEntity<?> updateRole(@Valid @RequestBody UpdateRoleUserDto updateRoleUserDto, HttpServletRequest request){
        String token = request.getHeader("Authorization");
        adminService.updateUserRole(token, updateRoleUserDto);
        return new ResponseEntity<>("Role successfully changed", HttpStatus.ACCEPTED);
    }
}
