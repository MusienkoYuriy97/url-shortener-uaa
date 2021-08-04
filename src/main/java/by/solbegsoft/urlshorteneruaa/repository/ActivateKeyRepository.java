package by.solbegsoft.urlshorteneruaa.repository;

import by.solbegsoft.urlshorteneruaa.model.ActivateKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ActivateKeyRepository extends JpaRepository<ActivateKey, UUID> {
    long deleteActivateKeyBySimpleKey(String simpleKey);
    Optional<ActivateKey> getBySimpleKey(String simpleKey);
}