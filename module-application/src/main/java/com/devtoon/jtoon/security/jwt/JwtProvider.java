package com.devtoon.jtoon.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {

	@Value("${jwt.secret.key}")
	private String salt;

	@Value("${jwt.iss}")
	private String iss;

	@Value("${jwt.expire}")
	private long expire;

	private Key secretKey;

	private final UserDetailsService userDetailsService;

	@PostConstruct
	private void init() {
		secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
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
		Jws<Claims> claimsJws = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build().parseClaimsJws(token);
		if (claimsJws.getBody().getExpiration().before(Date.valueOf(LocalDate.now()))) {
			throw new RuntimeException("Token Expired");
		}
	}

	public Authentication getAuthentication(String token) {
		String email = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build().parseClaimsJws(token).getBody().getSubject();

		UserDetails userDetails = userDetailsService.loadUserByUsername(email);

		return new UsernamePasswordAuthenticationToken(userDetails, " ", userDetails.getAuthorities());
	}

	private Claims getClaims(String email) {
		Date now = Date.valueOf(LocalDate.now());
		return Jwts.claims()
			.setSubject(email)
			.setIssuer(iss)
			.setExpiration(getExpireTime())
			.setIssuedAt(now);
	}

	private Date getExpireTime() {
		Date now = Date.valueOf(LocalDate.now());
		return new Date(now.getTime() + 1000 * 60 * expire);
	}
}

