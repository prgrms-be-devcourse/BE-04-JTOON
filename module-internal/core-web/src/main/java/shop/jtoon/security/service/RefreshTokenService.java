package shop.jtoon.security.service;

public interface RefreshTokenService {

	String getRefreshTokenEmail(String refreshToken);

	void saveRefreshTokenDb(String refreshToken, String email);

	void updateRefreshToken(String newRefreshToken, String email, String oldRefreshToken);

	boolean hasRefreshToken(String refreshToken);
}
