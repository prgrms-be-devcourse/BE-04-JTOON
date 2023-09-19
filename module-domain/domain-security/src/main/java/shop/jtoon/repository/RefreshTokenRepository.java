package shop.jtoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import shop.jtoon.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
	
	Optional<RefreshToken> findByRefreshToken(String refreshToken);
}

