package by.solbegsoft.urlshorteneruaa.model.dto;

import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class UpdateRoleUserDto {
    @Min(1)
    private long userId;
    @NotNull @NotBlank
    @Pattern(regexp = "^(ADMIN|USER)?")
    private String newRole;
}
