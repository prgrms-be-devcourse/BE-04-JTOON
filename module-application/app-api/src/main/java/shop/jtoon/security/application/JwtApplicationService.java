package shop.jtoon.security.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.jtoon.security.service.RefreshTokenService;
import shop.jtoon.service.TokenDomainService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JwtApplicationService implements RefreshTokenService {

	private final TokenDomainService tokenDomainService;
	
	@Override
	public String getRefreshTokenEmail(String refreshToken) {
		return tokenDomainService.getRefreshTokenEmail(refreshToken);
	}

	@Override
	public void verifyRefreshTokenDb(String refreshToken) {
		tokenDomainService.verifyRefreshTokenDb(refreshToken);
	}

	@Override
	@Transactional
	public void updateRefreshTokenDb(String email, String newRefreshToken) {
		tokenDomainService.updateRefreshTokenDb(email, newRefreshToken);
	}

	@Override
	@Transactional
	public void saveRefreshTokenDb(String email, String refreshToken) {
		tokenDomainService.saveRefreshTokenDb(email, refreshToken);
	}
}
