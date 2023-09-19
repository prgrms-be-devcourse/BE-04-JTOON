package shop.jtoon.security.service;

public interface RefreshTokenService {

	String getRefreshTokenEmail(String refreshToken);

	void verifyRefreshTokenDb(String refreshToken);

	void updateRefreshTokenDb(String email, String newRefreshToken);

	void saveRefreshTokenDb(String email, String refreshToken);
}
