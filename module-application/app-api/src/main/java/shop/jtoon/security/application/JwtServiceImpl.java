package shop.jtoon.security.application;

import static shop.jtoon.type.ErrorStatus.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.jtoon.exception.UnauthorizedException;
import shop.jtoon.security.service.JwtService;
import shop.jtoon.security.service.RefreshTokenService;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {

	@Value("${jwt.iss}")
	private String ISS;

	@Value("${jwt.secret.access-key}")
	private String SALT;

	@Value("${jwt.access-expire}")
	private long ACCESS_EXPIRE;

	@Value("${jwt.refresh-expire}")
	private long REFRESH_EXPIRE;

	private Key secretKey;
	private final RefreshTokenService refreshTokenService;

	@PostConstruct
	private void init() {
		secretKey = Keys.hmacShaKeyFor(SALT.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public String reGenerateAccessToken(String refreshToken) {
		String email = refreshTokenService.getRefreshTokenEmail(refreshToken);
		return generateAccessToken(email);
	}

	@Override
	public String generateAccessToken(String email) {
		return commonJwtBuilder(ACCESS_EXPIRE, secretKey)
			.setSubject(email)
			.compact();
	}

	@Override
	public String generateRefreshToken() {
		return commonJwtBuilder(REFRESH_EXPIRE, secretKey)
			.compact();
	}

	@Override
	public void updateRefreshTokenDb(String accessToken, String newRefreshToken, String oldRefreshToken) {
		String email = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(accessToken)
			.getBody()
			.getSubject();

		refreshTokenService.updateRefreshToken(newRefreshToken, email, oldRefreshToken);
	}

	@Override
	public void validateRefreshTokenRedis(String refreshToken) throws UnauthorizedException {
		if (!refreshTokenService.hasRefreshToken(refreshToken)) {
			throw new UnauthorizedException(MEMBER_REFRESH_TOKEN_NOT_FOUND);
		}
	}

	private JwtBuilder commonJwtBuilder(long expireTime, Key key) {
		Date issueDate = new Date();
		Date exipireDate = new Date(issueDate.getTime() + expireTime);

		return Jwts.builder()
			.setIssuer(ISS)
			.setIssuedAt(issueDate)
			.setExpiration(exipireDate)
			.signWith(key, SignatureAlgorithm.HS256)
			.setHeaderParam("alg", "HS256")
			.setHeaderParam("typ", "JWT");
	}
}
