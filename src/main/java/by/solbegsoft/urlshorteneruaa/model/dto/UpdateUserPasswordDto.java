package by.solbegsoft.urlshorteneruaa.model.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class UpdateUserPasswordDto {
    @NotNull @NotBlank
    private String password;
    @NotNull @NotBlank
    private String repeatedPassword;
}
