package by.solbegsoft.urlshorteneruaa.repository;

import by.solbegsoft.urlshorteneruaa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> getByEmail(String email);
}