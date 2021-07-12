package by.solbegsoft.urlshorteneruaa.repository;

import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
import by.solbegsoft.urlshorteneruaa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivateKeyRepository extends JpaRepository<ActivateKey, Long> {
    boolean existsByKey(String key);
    long deleteActivateKeyByKey(String key);
    Optional<ActivateKey> getByKey(String key);
}