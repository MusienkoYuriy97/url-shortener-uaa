package by.solbegsoft.urlshorteneruaa.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UserCreateDto {
    @NotNull @NotBlank
    private String firstName;
    @NotNull @NotBlank
    private String lastName;
    @Pattern(regexp = "\\b[a-zA-Z0-9._%-]+@[a-zA-Z]+\\.[a-zA-Z]{2,3}\\b")
    @NotNull @NotBlank
    private String email;
    @NotNull @NotBlank
    private String password;
}