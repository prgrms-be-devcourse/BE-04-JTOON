package shop.jtoon.repository;

import static shop.jtoon.util.SecurityConstant.*;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StringRedisRepository {

	private final StringRedisTemplate redisTemplate;

	public void save(String key, String value, Long expireMin) {
		redisTemplate.opsForValue().set(key, value, expireMin * MINUTE, TimeUnit.MILLISECONDS);
	}

	public String getData(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public void delete(String key) {
		redisTemplate.delete(key);
	}

	public boolean hasKey(String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}
}
