package by.solbegsoft.urlshorteneruaa.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "Url request")
public class RequestUrlDto {
    @Schema(example = "google.com")
    @NotNull @NotBlank
    private String originUrl;
}
