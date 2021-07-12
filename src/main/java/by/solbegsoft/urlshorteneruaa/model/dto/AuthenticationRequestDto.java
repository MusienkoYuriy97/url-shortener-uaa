package by.solbegsoft.urlshorteneruaa.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthenticationRequestDto {
    private String email;
    private String password;
}