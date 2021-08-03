package by.solbegsoft.urlshorteneruaa.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Save short link representation")
@ApiResponses(value = {
        @ApiResponse(responseCode = "301", description = "MOVED_PERMANENTLY", content = @Content(mediaType = "text/html"))})
public @interface ApiGetRedirectUrl {
}
