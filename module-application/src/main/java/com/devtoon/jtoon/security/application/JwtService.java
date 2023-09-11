package com.devtoon.jtoon.security.application;

import static com.devtoon.jtoon.security.util.SecurityConstant.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.security.domain.jwt.CustomUserDetails;
import com.devtoon.jtoon.security.entity.RefreshToken;
import com.devtoon.jtoon.security.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
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

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JwtService {

	@Value("${jwt.iss}")
	private String ISS;

	@Value("${jwt.secret.access-key}")
	private String SALT;

	@Value("${jwt.access-expire}")
	private long ACCESS_EXPIRE;

	@Value("${jwt.refresh-expire}")
	private long REFRESH_EXPIRE;

	private Key secretKey;

	private final CustomUserDetailsService userDetailsService;

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
			.orElseThrow(() -> new BadCredentialsException("No refresh token found"));

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
			throw new BadCredentialsException("Invalid access token", e);
		}

		return false;
	}

	public Authentication getAuthentication(String token) {
		String claimsEmail = parseClaimsBody(token).getSubject();
		CustomUserDetails userDetails = userDetailsService.loadUserByUsername(claimsEmail);

		return new UsernamePasswordAuthenticationToken(userDetails, " ", userDetails.getAuthorities());
	}

	public void verifyRefreshTokenDb(String refreshToken) {
		RefreshToken findRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new BadCredentialsException("No refresh token found"));

		if (!findRefreshToken.matches(refreshToken)) {
			throw new BadCredentialsException("No refresh token match");
		}
	}

	@Transactional
	public void updateRefreshTokenDb(String accessToken, String newRefreshToken) {
		String email = parseClaimsBody(accessToken).getSubject();
		RefreshToken findRefreshToken = refreshTokenRepository.findById(email)
			.orElseThrow(() -> new BadCredentialsException("No refresh token found"));
		findRefreshToken.updateToken(newRefreshToken);
	}

	private Date getExpireTime(Date now, long expire) {
		return new Date(now.getTime() + MINUTE * expire);
	}

	private Map<String, Object> getHeaders() {
		Map<String, Object> headers = new HashMap<>();
		headers.put("alg", "HS256");
		headers.put("typ", "JWT");

		return headers;
	}

	private Claims parseClaimsBody(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build().parseClaimsJws(token).getBody();
	}
}
