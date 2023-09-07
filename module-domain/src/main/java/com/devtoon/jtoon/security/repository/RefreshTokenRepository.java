package com.devtoon.jtoon.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devtoon.jtoon.security.entity.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

	Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
