package by.solbegsoft.urlshorteneruaa.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
public class UpdateRoleUserDto {
    @Pattern(regexp = "\\b[a-zA-Z0-9._%-]+@[a-zA-Z]+\\.[a-zA-Z]{2,3}\\b")
    private String email;
    @Pattern(regexp = "^(ROLE_ADMIN|ROLE_USER)")
    private String newRole;
}