package shop.jtoon.security.service;

import static shop.jtoon.type.ErrorStatus.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.jtoon.exception.InvalidRequestException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorizationService {

	@Value("${jwt.secret.access-key}")
	private String SALT;

	private Key secretKey;

	@PostConstruct
	private void init() {
		secretKey = Keys.hmacShaKeyFor(SALT.getBytes(StandardCharsets.UTF_8));
	}

	public boolean isTokenValid(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);

			return true;
		} catch (ExpiredJwtException e) {
			log.error("Expired access Token", e);
		} catch (Exception e) {
			log.error("Expire가 아닌 무효한 Token", e);
			throw new InvalidRequestException(MEMBER_INVALID_ACCESS_TOKEN);
		}

		return false;
	}
}
