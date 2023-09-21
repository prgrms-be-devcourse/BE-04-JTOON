package shop.jtoon.security.application;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.jtoon.security.service.RefreshTokenService;
import shop.jtoon.service.RedisTokenService;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final RedisTokenService redisTokenService;

	@Override
	public String getRefreshTokenEmail(String refreshToken) {
		return redisTokenService.getEmail(refreshToken);
	}

	@Override
	public void saveRefreshToken(String refreshToken, String email) {
		redisTokenService.saveRefreshToken(refreshToken, email);
	}

	@Override
	public void updateRefreshToken(String newRefreshToken, String email, String oldRefreshToken) {
		redisTokenService.updateRefreshToken(newRefreshToken, email, oldRefreshToken);
	}

	@Override
	public boolean hasRefreshToken(String refreshToken) {
		return redisTokenService.hasRefreshToken(refreshToken);
	}
}
