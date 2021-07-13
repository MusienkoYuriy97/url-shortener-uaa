package by.solbegsoft.urlshorteneruaa.repository;

import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ActivateKeyRepository extends JpaRepository<ActivateKey, UUID> {
    long deleteActivateKeyBySimpleKey(String simpleKey);
    Optional<ActivateKey> getBySimpleKey(String simpleKey);
}