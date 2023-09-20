package shop.jtoon.security.application;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.jtoon.security.service.RefreshTokenService;
import shop.jtoon.service.RedisTokenDomainService;

@Service
@RequiredArgsConstructor
public class JwtApplicationService implements RefreshTokenService {

	private final RedisTokenDomainService redisTokenDomainService;

	@Override
	public String getRefreshTokenEmail(String refreshToken) {
		return redisTokenDomainService.getEmail(refreshToken);
	}

	@Override
	public void saveRefreshToken(String refreshToken, String email) {
		redisTokenDomainService.saveRefreshToken(refreshToken, email);
	}

	@Override
	public void updateRefreshToken(String newRefreshToken, String email, String oldRefreshToken) {
		redisTokenDomainService.updateRefreshToken(newRefreshToken, email, oldRefreshToken);
	}

	@Override
	public boolean hasRefreshToken(String refreshToken) {
		return redisTokenDomainService.hasRefreshToken(refreshToken);
	}
}
