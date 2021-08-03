package by.solbegsoft.urlshorteneruaa.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "Url request")
public class UrlCreateRequest {
    @Schema(example = "google.com")
    @NotNull @NotBlank
    private String originUrl;
    private String userUuid;
}
