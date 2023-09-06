package com.devtoon.jtoon.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
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
@Component
@RequiredArgsConstructor
public class JwtProvider {

	@Value("${jwt.secret.key}")
	private String SALT;

	@Value("${jwt.iss}")
	private String ISS;

	@Value("${jwt.expire}")
	private long EXPIRE;

	private Key secretKey;

	private final UserDetailsService userDetailsService;

	@PostConstruct
	private void init() {
		secretKey = Keys.hmacShaKeyFor(SALT.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(String email) {
		Claims claims = getClaims(email);

		return Jwts.builder()
			.setClaims(claims)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.setHeader(getHeaders())
			.compact();
	}

	private Map<String, Object> getHeaders() {
		Map<String, Object> headers = new HashMap<>();
		headers.put("alg", "HS256");
		headers.put("typ", "JWT");

		return headers;
	}

	public void validateToken(String token) {
		Date claimsExpiration = parseClaimsBody(token).getExpiration();

		if (claimsExpiration.before(new Date())) {
			throw new RuntimeException("Token Expired");
		}
	}

	public Authentication getAuthentication(String token) {
		String ClaimsEmail = parseClaimsBody(token).getSubject();
		UserDetails userDetails = userDetailsService.loadUserByUsername(ClaimsEmail);

		return new UsernamePasswordAuthenticationToken(userDetails, " ", userDetails.getAuthorities());
	}

	private Claims parseClaimsBody(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build().parseClaimsJws(token).getBody();
	}

	private Claims getClaims(String email) {
		Date now = new Date();

		return Jwts.claims()
			.setSubject(email)
			.setIssuer(ISS)
			.setExpiration(getExpireTime(now))
			.setIssuedAt(now);
	}

	private Date getExpireTime(Date now) {
		return new Date(now.getTime() + 1000 * 60 * EXPIRE);
	}
}

