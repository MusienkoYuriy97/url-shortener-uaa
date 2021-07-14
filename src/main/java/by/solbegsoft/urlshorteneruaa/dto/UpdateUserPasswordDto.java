package by.solbegsoft.urlshorteneruaa.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Schema(description = "Object for update password")
@Getter
public class UpdateUserPasswordDto {
    @Schema(example = "12345678")
    @NotNull @NotBlank
    @Length(min = 4, max = 14, message = "Password must contain from 4 to 14 characters")
    private String oldPassword;

    @Schema(example = "11111")
    @NotNull @NotBlank
    @Length(min = 4, max = 14, message = "Password must contain from 4 to 14 characters")
    private String newPassword;

    @Schema(example = "11111")
    @NotNull @NotBlank
    @Length(min = 4, max = 14, message = "Password must contain from 4 to 14 characters")
    private String repeatedPassword;
}