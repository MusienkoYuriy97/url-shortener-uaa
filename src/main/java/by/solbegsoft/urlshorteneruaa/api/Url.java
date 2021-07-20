package by.solbegsoft.urlshorteneruaa.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Url {
    private UUID uuid;
    private String originUrl;
    private String shortUrl;
}