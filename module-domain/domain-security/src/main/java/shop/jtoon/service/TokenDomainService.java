package shop.jtoon.service;

import static shop.jtoon.type.ErrorStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.jtoon.entity.RefreshToken;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.repository.RefreshTokenRepository;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenDomainService {

	private final RefreshTokenRepository refreshTokenRepository;

	public String getRefreshTokenEmail(String refreshToken) {
		RefreshToken findRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new NotFoundException(MEMBER_REFRESH_TOKEN_NOT_FOUND));

		return findRefreshToken.getEmail();
	}

	public void verifyRefreshTokenDb(String refreshToken) {
		RefreshToken findRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new NotFoundException(MEMBER_REFRESH_TOKEN_NOT_FOUND));

		if (!findRefreshToken.matches(refreshToken)) {
			throw new InvalidRequestException(MEMBER_REFRESH_TOKEN_NOT_MATCH);
		}
	}

	@Transactional
	public void updateRefreshTokenDb(String email, String newRefreshToken) {
		RefreshToken findRefreshToken = refreshTokenRepository.findById(email)
			.orElseThrow(() -> new NotFoundException(MEMBER_REFRESH_TOKEN_NOT_FOUND));
		findRefreshToken.updateToken(newRefreshToken);
	}

	@Transactional
	public void saveRefreshTokenDb(String email, String refreshToken) {
		refreshTokenRepository.save(RefreshToken.builder()
			.email(email)
			.refreshToken(refreshToken)
			.build());
	}
}
