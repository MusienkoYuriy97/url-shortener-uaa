package by.solbegsoft.urlshorteneruaa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class ActivateKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private User user;
    private String key;
    private LocalDateTime createdAt;
    private LocalDateTime expirationDate;

    @PrePersist
    public void initDate(){
        createdAt = LocalDateTime.now();
        expirationDate = createdAt.plusHours(1);
    }
}
