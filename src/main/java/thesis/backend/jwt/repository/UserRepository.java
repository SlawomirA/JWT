package thesis.backend.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import thesis.backend.jwt.model.MySQL.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}

