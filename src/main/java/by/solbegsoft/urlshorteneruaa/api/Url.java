package by.solbegsoft.urlshorteneruaa.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(name = "Url")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Url {
    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID uuid;
    @Schema(example = "http://google.com")
    private String originUrl;
    @Schema(example = "GFDGdv422")
    private String shortUrl;
}