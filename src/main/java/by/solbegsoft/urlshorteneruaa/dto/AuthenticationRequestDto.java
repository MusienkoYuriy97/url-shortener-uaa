package by.solbegsoft.urlshorteneruaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "Object for login user in system")
@Getter @Setter
public class AuthenticationRequestDto {
    @Schema(example = "97musienko@gmail.com")
    @NotNull @NotBlank
    private String email;

    @Schema(example = "12345")
    @NotNull @NotBlank
    private String password;
}