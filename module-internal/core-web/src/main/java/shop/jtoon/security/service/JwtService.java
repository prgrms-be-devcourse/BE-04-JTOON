package shop.jtoon.security.service;

public interface JwtService {

	String generateAccessToken(String email);

	String reGenerateAccessToken(String refreshToken);

	String generateRefreshToken();

	void updateRefreshTokenDb(String accessToken, String newRefreshToken, String oldRefreshToken);

	void validateRefreshTokenRedis(String refreshToken);
}
