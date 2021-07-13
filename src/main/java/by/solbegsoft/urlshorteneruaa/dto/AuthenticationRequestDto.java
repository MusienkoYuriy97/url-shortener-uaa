package by.solbegsoft.urlshorteneruaa.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class AuthenticationRequestDto {
    @NotNull @NotBlank
    private String email;
    @NotNull @NotBlank
    private String password;
}