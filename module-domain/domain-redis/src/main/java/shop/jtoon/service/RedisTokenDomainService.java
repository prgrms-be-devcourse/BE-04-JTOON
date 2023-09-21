package shop.jtoon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.jtoon.util.StringRedisUtils;

@Service
@RequiredArgsConstructor
public class RedisTokenDomainService {

	@Value("${jwt.refresh-expire}")
	private long REFRESH_EXPIRE;

	private final StringRedisUtils redisUtils;

	public void saveRefreshToken(String refreshToken, String email) {
		redisUtils.save(refreshToken, email, REFRESH_EXPIRE);
	}

	public void updateRefreshToken(String newRefreshToken, String email, String oldRefreshToken) {
		redisUtils.delete(oldRefreshToken);
		saveRefreshToken(newRefreshToken, email);
	}

	public String getEmail(String refreshToken) {
		return redisUtils.getData(refreshToken);
	}

	public boolean hasRefreshToken(String refreshToken) {
		return redisUtils.hasKey(refreshToken);
	}
}
