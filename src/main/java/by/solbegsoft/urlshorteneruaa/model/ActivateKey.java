package by.solbegsoft.urlshorteneruaa.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "ActivateKey(Unuseful in Controller)")
@Entity
@Builder
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
    @Column(name = "created_at", nullable = false)
    @Schema(example = "")
    private LocalDateTime createdAt;

    @PrePersist
    public void dateInit(){
        createdAt = LocalDateTime.now();
    }
}