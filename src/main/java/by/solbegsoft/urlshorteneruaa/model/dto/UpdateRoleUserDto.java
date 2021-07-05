package by.solbegsoft.urlshorteneruaa.model.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
public class UpdateRoleUserDto {
    @Min(2)
    private long userId;
    @Pattern(regexp = "^(ADMIN|USER)")
    private String newRole;
}