package by.solbegsoft.urlshorteneruaa.swagger;

import by.solbegsoft.urlshorteneruaa.dto.UserCreateResponse;
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
@Operation(summary = "Update role of user by the admin")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json",schema = @Schema(implementation = UserCreateResponse.class))),
        @ApiResponse(responseCode = "400", description = "Fields entered incorrectly", content = @Content)})
public @interface ApiPatchUpdateRole {
}
