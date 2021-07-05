package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.model.User;
import by.solbegsoft.urlshorteneruaa.model.dto.UpdateRoleUserDto;
import by.solbegsoft.urlshorteneruaa.service.AdminService;
import by.solbegsoft.urlshorteneruaa.service.TokenService;
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
    private TokenService tokenService;

    @Autowired
    public AdminController(AdminService adminService,
                           TokenService tokenService) {
        this.adminService = adminService;
        this.tokenService = tokenService;
    }

    @PutMapping("/update/role")
    @PreAuthorize("hasAuthority('role:update')")
    public ResponseEntity<?> updateRole(@Valid @RequestBody UpdateRoleUserDto dto,
                                        HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        long currentUserId = tokenService.getUserByToken(bearerToken).getId();
        if (currentUserId == dto.getUserId()){
            return new ResponseEntity<>("You can't change the role for yourself", HttpStatus.CONFLICT);
        }
        User user = adminService.updateUserRole(dto);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }
}