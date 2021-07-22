package by.solbegsoft.urlshorteneruaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static by.solbegsoft.urlshorteneruaa.dto.ValidationConstant.*;

@Schema(description = "Object for registration new user")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter @Setter
public class UserCreateRequest {
    @Schema(example = "Yuriy")
    @NotNull @NotBlank
    private String firstName;

    @Schema(example = "Musienko")
    @NotNull @NotBlank
    private String lastName;

    @Schema(example = "97musienko@gmail.com")
    @Pattern(regexp = EMAIL_REGEXP)
    @NotNull @NotBlank
    private String email;

    @Schema(example = "11111")
    @NotNull @NotBlank @Length(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH, message = PASSWORD_MSG)
    private String password;
}