package by.solbegsoft.urlshorteneruaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Schema(description = "Object for update user role by admin")
@Getter @Setter
public class UpdateRoleUserDto {
    @Schema(example = "pavel@gmail.com")
    @Pattern(regexp = "\\b[a-zA-Z0-9._%-]+@[a-zA-Z]+\\.[a-zA-Z]{2,3}\\b")
    private String email;
}