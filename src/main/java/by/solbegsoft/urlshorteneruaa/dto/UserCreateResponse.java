package by.solbegsoft.urlshorteneruaa.dto;

import by.solbegsoft.urlshorteneruaa.util.UserRole;
import by.solbegsoft.urlshorteneruaa.util.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Response for user")
@Getter @Setter
public class UserCreateResponse {
    @Schema(example = "Yury")
    private String firstName;

    @Schema(example = "Musienko")
    private String lastName;

    @Schema(example = "97musienko@gmail.com")
    private String email;

    @Schema(example = "ACTIVE")
    private UserStatus userStatus;

    @Schema(example = "ROLE_USER")
    private UserRole userRole;
}