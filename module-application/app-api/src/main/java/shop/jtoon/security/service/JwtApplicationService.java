package shop.jtoon.security.service;

import static shop.jtoon.util.SecurityConstant.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.jtoon.entity.CustomUserDetails;
import shop.jtoon.entity.Member;
import shop.jtoon.response.LoginRes;
import shop.jtoon.security.request.LoginReq;
import shop.jtoon.service.MemberDomainService;
import shop.jtoon.service.TokenDomainService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JwtApplicationService implements JwtService {

	private final TokenDomainService tokenDomainService;
	private final MemberDomainService memberDomainService;

	@Transactional
	public LoginRes getLoginTokens(LoginReq loginReq) {
		memberDomainService.localLoginMember(loginReq.toDto());
		return tokenDomainService.getLoginTokens(loginReq.email());
	}

	@Override
	public String generateAccessToken(String email) {
		return tokenDomainService.generateAccessToken(email);
	}

	@Override
	public String reGenerateAccessToken(String refreshToken) {
		return tokenDomainService.reGenerateAccessToken(refreshToken);
	}

	@Override
	public String generateRefreshToken() {
		return tokenDomainService.generateRefreshToken();
	}

	@Override
	public boolean isTokenValid(String token) {
		return tokenDomainService.isTokenValid(token);
	}

	@Override
	public Authentication getAuthentication(String token) {
		String claimsEmail = tokenDomainService.getClaimsBodyEmail(token);
		Member member = memberDomainService.findByEmail(claimsEmail);
		UserDetails userDetails = new CustomUserDetails(member);

		return new UsernamePasswordAuthenticationToken(userDetails, BLANK, userDetails.getAuthorities());
	}

	@Override
	public void verifyRefreshTokenDb(String refreshToken) {
		tokenDomainService.verifyRefreshTokenDb(refreshToken);
	}

	@Override
	@Transactional
	public void updateRefreshTokenDb(String accessToken, String newRefreshToken) {
		tokenDomainService.updateRefreshTokenDb(accessToken, newRefreshToken);
	}

	@Override
	@Transactional
	public void saveRefreshTokenDb(String email, String refreshToken) {
		tokenDomainService.saveRefreshTokenDb(email, refreshToken);
	}
}
