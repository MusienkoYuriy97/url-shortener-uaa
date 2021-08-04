package by.solbegsoft.urlshorteneruaa.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Reset activate key")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(example = "access_token:{jwtToken}"))),
        @ApiResponse(responseCode = "400", description = "Fields entered incorrectly", content = @Content),
        @ApiResponse(responseCode = "403", description = "Account doesn't active", content = @Content),
        @ApiResponse(responseCode = "403", description = "Wrong password/email", content = @Content)})
@SecurityRequirements
public @interface ApiPostActivateReset {
}
