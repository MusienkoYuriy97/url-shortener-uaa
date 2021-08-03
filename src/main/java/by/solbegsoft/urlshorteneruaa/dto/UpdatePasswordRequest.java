package by.solbegsoft.urlshorteneruaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static by.solbegsoft.urlshorteneruaa.dto.ValidationConstant.*;

@Schema(description = "Object for update password")
@Builder
@Data
@AllArgsConstructor @NoArgsConstructor
public class UpdatePasswordRequest {
    @Schema(example = "12345678")
    @NotNull @NotBlank
    @Length(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH, message = PASSWORD_MSG)
    private String oldPassword;

    @Schema(example = "11111")
    @NotNull @NotBlank
    @Length(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH, message = PASSWORD_MSG)
    private String newPassword;

    @Schema(example = "11111")
    @NotNull @NotBlank
    @Length(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH, message = PASSWORD_MSG)
    private String repeatedPassword;
}