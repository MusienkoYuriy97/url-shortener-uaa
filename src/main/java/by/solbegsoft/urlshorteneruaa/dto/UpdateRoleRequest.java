package by.solbegsoft.urlshorteneruaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Schema(description = "Object for update user role by admin")
@Getter @Setter
public class UpdateRoleRequest {
    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    @NotBlank @NotNull
    private String uuid;
    @Schema(example = "ROLE_ADMIN")
    @Pattern(regexp = "^(ROLE_ADMIN|ROLE_USER)")
    @NotBlank @NotNull
    private String role;
}