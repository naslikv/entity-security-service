package entityportal.security.persistence.repository;

import entityportal.security.persistence.entity.JWTTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JWTTokenRepository extends JpaRepository<JWTTokenEntity,Long> {
    Optional<JWTTokenEntity> findByToken(String token);
}
