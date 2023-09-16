package shop.jtoon.security.service;

import org.springframework.security.core.Authentication;

public interface JwtService {

	String generateAccessToken(String email);

	String reGenerateAccessToken(String refreshToken);

	String generateRefreshToken();

	boolean isTokenValid(String token);

	Authentication getAuthentication(String token);

	void verifyRefreshTokenDb(String refreshToken);

	void updateRefreshTokenDb(String accessToken, String newRefreshToken);

	void saveRefreshTokenDb(String email, String refreshToken);
}
