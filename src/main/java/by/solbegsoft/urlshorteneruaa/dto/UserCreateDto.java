package by.solbegsoft.urlshorteneruaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Schema(description = "Object for registration new user")
@Getter @Setter
public class UserCreateDto {
    @Schema(example = "Yuriy")
    @NotNull @NotBlank
    private String firstName;

    @Schema(example = "Musienko")
    @NotNull @NotBlank
    private String lastName;

    @Schema(example = "97musienko@gmail.com")
    @Pattern(regexp = "\\b[a-zA-Z0-9._%-]+@[a-zA-Z]+\\.[a-zA-Z]{2,3}\\b")
    @NotNull @NotBlank
    private String email;

    @Schema(example = "11111")
    @NotNull @NotBlank @Length(min = 4, max = 14, message = "Password must contain from 4 to 14 characters")
    private String password;
}