package by.solbegsoft.urlshorteneruaa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class TokenResponse {
    private String email;
    private String token;
}
