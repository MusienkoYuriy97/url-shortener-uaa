package by.solbegsoft.urlshorteneruaa.swagger;

import by.solbegsoft.urlshorteneruaa.dto.UserCreateResponse;
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
@Operation(summary = "Registration new user")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(mediaType = "application/json",schema = @Schema(implementation = UserCreateResponse.class))),
        @ApiResponse(responseCode = "400", description = "Fields entered incorrectly", content = @Content),
        @ApiResponse(responseCode = "409", description = "User with this email already exist", content = @Content)})
@SecurityRequirements
public @interface ApiPostRegistration {
}

