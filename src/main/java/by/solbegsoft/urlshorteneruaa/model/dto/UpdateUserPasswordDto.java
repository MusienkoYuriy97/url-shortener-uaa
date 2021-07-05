package by.solbegsoft.urlshorteneruaa.model.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class UpdateUserPasswordDto {
    @NotNull @NotBlank
    private String oldPassword;
    @NotNull @NotBlank
    private String newPassword;
    @NotNull @NotBlank
    private String repeatedPassword;
}