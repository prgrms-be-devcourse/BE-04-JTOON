package shop.jtoon.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.jtoon.repository.StringRedisRepository;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

	@Value("${jwt.refresh-expire}")
	private long REFRESH_EXPIRE;

	private final StringRedisRepository redisRepository;

	public void saveRefreshToken(String refreshToken, String email) {
		redisRepository.save(refreshToken, email, REFRESH_EXPIRE);
	}

	public void updateRefreshToken(String newRefreshToken, String email, String oldRefreshToken) {
		redisRepository.delete(oldRefreshToken);
		saveRefreshToken(newRefreshToken, email);
	}

	public String getEmail(String refreshToken) {
		return redisRepository.getData(refreshToken);
	}

	public boolean hasRefreshToken(String refreshToken) {
		return redisRepository.hasKey(refreshToken);
	}
}
