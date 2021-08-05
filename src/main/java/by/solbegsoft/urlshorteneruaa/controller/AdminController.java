package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.dto.UpdateRoleRequest;
import by.solbegsoft.urlshorteneruaa.service.AdminService;
import by.solbegsoft.urlshorteneruaa.swagger.ApiPatchUpdateRole;
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
@RequestMapping("/admin")
@Tag(name = "AdminController", description = "End points for users who has role ADMIN")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PatchMapping("/role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiPatchUpdateRole
    public ResponseEntity<?> updateRole(@Valid @RequestBody UpdateRoleRequest updateRoleRequest){
        if (adminService.isCurrentAdmin(updateRoleRequest.getUuid())){
            return new ResponseEntity<>("You can't change the role for yourself",
                    HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(adminService.updateUserRole(updateRoleRequest),
                HttpStatus.OK);
    }
}