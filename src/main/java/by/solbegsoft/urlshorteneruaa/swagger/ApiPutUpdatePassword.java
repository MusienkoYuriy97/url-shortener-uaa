package by.solbegsoft.urlshorteneruaa.swagger;

import by.solbegsoft.urlshorteneruaa.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Update password for user")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json",schema = @Schema(example = "Successfully updated password"))),
        @ApiResponse(responseCode = "400", description = "Fields entered incorrectly", content = @Content),
        @ApiResponse(responseCode = "409", description = "Passwords entered incorrectly", content = @Content),
})
public @interface ApiPutUpdatePassword {
}
