package by.solbegsoft.urlshorteneruaa.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Schema(name = "ActivateKey(Unuseful in Controller)")
@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Table(name = "activate_key")
public class ActivateKey {
    @Id
    @Column(name = "uuid", nullable = false)
    @GeneratedValue
    @Schema(example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID uuid;
    @Column(name = "simple_key", nullable = false)
    @Schema(example = "sdf13Fas34f2")
    private String simpleKey;
    @OneToOne
    private User user;
}