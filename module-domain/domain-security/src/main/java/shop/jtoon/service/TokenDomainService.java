package shop.jtoon.service;

import static shop.jtoon.type.ErrorStatus.*;
import static shop.jtoon.util.SecurityConstant.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.jtoon.entity.RefreshToken;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.repository.RefreshTokenRepository;
import shop.jtoon.response.LoginRes;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenDomainService {

	@Value("${jwt.iss}")
	private String ISS;

	@Value("${jwt.secret.access-key}")
	private String SALT;

	@Value("${jwt.access-expire}")
	private long ACCESS_EXPIRE;

	@Value("${jwt.refresh-expire}")
	private long REFRESH_EXPIRE;

	private Key secretKey;
	private final RefreshTokenRepository refreshTokenRepository;

	@PostConstruct
	private void init() {
		secretKey = Keys.hmacShaKeyFor(SALT.getBytes(StandardCharsets.UTF_8));
	}

	public String generateAccessToken(String email) {
		return commonJwtBuilder(ACCESS_EXPIRE, secretKey)
			.setSubject(email)
			.compact();
	}

	public String generateRefreshToken() {
		return commonJwtBuilder(REFRESH_EXPIRE, secretKey)
			.compact();
	}

	public String reGenerateAccessToken(String refreshToken) {
		RefreshToken findRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new NotFoundException(MEMBER_REFRESH_TOKEN_NOT_FOUND));

		return generateAccessToken(findRefreshToken.getEmail());
	}

	private JwtBuilder commonJwtBuilder(long expire, Key key) {
		Date now = new Date();

		return Jwts.builder()
			.setIssuer(ISS)
			.setExpiration(getExpireTime(now, expire))
			.setIssuedAt(now)
			.signWith(key, SignatureAlgorithm.HS256)
			.setHeader(getHeaders());
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

	public void verifyRefreshTokenDb(String refreshToken) {
		RefreshToken findRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new NotFoundException(MEMBER_REFRESH_TOKEN_NOT_FOUND));

		if (!findRefreshToken.matches(refreshToken)) {
			throw new InvalidRequestException(MEMBER_REFRESH_TOKEN_NOT_MATCH);
		}
	}

	@Transactional
	public void updateRefreshTokenDb(String accessToken, String newRefreshToken) {
		String email = getClaimsBodyEmail(accessToken);
		RefreshToken findRefreshToken = refreshTokenRepository.findById(email)
			.orElseThrow(() -> new NotFoundException(MEMBER_REFRESH_TOKEN_NOT_FOUND));
		findRefreshToken.updateToken(newRefreshToken);
	}

	public Date getExpireTime(Date now, long expire) {
		return new Date(now.getTime() + MINUTE * expire);
	}

	public Map<String, Object> getHeaders() {
		Map<String, Object> headers = new HashMap<>();
		headers.put("alg", "HS256");
		headers.put("typ", "JWT");

		return headers;
	}

	public String getClaimsBodyEmail(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build().parseClaimsJws(token).getBody().getSubject();
	}

	@Transactional
	public LoginRes getLoginTokens(String email) {
		String accessToken = generateAccessToken(email);
		String refreshToken = generateRefreshToken();
		saveRefreshTokenDb(email, refreshToken);

		return LoginRes.of(accessToken, refreshToken);
	}

	@Transactional
	public void saveRefreshTokenDb(String email, String refreshToken) {
		refreshTokenRepository.save(RefreshToken.builder()
			.email(email)
			.refreshToken(refreshToken)
			.build());
	}
}
