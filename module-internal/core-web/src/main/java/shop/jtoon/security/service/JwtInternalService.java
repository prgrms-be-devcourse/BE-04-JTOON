package shop.jtoon.security.service;

import static shop.jtoon.type.ErrorStatus.*;
import static shop.jtoon.util.SecurityConstant.*;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.jtoon.exception.InvalidRequestException;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtInternalService {

	@Value("${jwt.iss}")
	private String ISS;

	@Value("${jwt.secret.access-key}")
	private String SALT;

	@Value("${jwt.access-expire}")
	private long ACCESS_EXPIRE;

	@Value("${jwt.refresh-expire}")
	private long REFRESH_EXPIRE;

	private Key secretKey;
	private final RefreshTokenService jwtService;
	private final AuthenticationService authenticationService;

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

	public void verifyRefreshTokenDb(String refreshToken) {
		jwtService.verifyRefreshTokenDb(refreshToken);
	}

	public String reGenerateAccessToken(String refreshToken) {
		String email = jwtService.getRefreshTokenEmail(refreshToken);
		return generateAccessToken(email);
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

	public Date getExpireTime(Date now, long expire) {
		return new Date(now.getTime() + MINUTE * expire);
	}

	public Map<String, Object> getHeaders() {
		Map<String, Object> headers = new HashMap<>();
		headers.put("alg", "HS256");
		headers.put("typ", "JWT");

		return headers;
	}

	public void updateRefreshTokenDb(String accessToken, String newRefreshToken) {
		String email = getClaimsBodyEmail(accessToken);
		jwtService.updateRefreshTokenDb(email, newRefreshToken);
	}

	public String getClaimsBodyEmail(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build().parseClaimsJws(token).getBody().getSubject();
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

	public Authentication getAuthentication(String accessToken) {
		String claimsEmail = getClaimsBodyEmail(accessToken);
		return authenticationService.getAuthentication(claimsEmail);
	}
}
