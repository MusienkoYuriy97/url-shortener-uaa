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
@Operation(summary = "Activate account")
@ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "ACCEPTED", content = @Content(mediaType = "application/json",schema = @Schema(example = "Successful activate account."))),
        @ApiResponse(responseCode = "409", description = "Activate key is expired", content = @Content),
        @ApiResponse(responseCode = "409", description = "Activate key doesn't exist", content = @Content)})
@SecurityRequirements
public @interface ApiGetActivate {
}
