package by.solbegsoft.urlshorteneruaa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@Table(name = "activate_key")
public class ActivateKey {
    @Id
    @Column(name = "uuid", nullable = false)
    @GeneratedValue
    private UUID uuid;
    @Column(name = "simple_key", nullable = false)
    private String simpleKey;
    @OneToOne
    private User user;
}