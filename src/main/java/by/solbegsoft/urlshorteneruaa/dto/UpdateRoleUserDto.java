package by.solbegsoft.urlshorteneruaa.dto;


import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class UpdateRoleUserDto {
    @Pattern(regexp = "\\b[a-zA-Z0-9._%-]+@[a-zA-Z]+\\.[a-zA-Z]{2,3}\\b")
    private String email;
    @Pattern(regexp = "^(ROLE_ADMIN|ROLE_USER)")
    private String newRole;
}