package com.devtoon.jtoon.security.application;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.repository.MemberRepository;
import com.devtoon.jtoon.security.entity.RefreshToken;
import com.devtoon.jtoon.security.repository.RefreshTokenRepository;
import com.devtoon.jtoon.security.request.LogInReq;
import com.devtoon.jtoon.security.response.LoginRes;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public LoginRes login(LogInReq logInReq) {
		Member member = memberRepository.findByEmail(logInReq.email())
			.orElseThrow(() -> new BadCredentialsException("너 안돼!"));

		if (!isPasswordSame(logInReq.password(), member.getPassword())) {
			throw new BadCredentialsException("너 안돼!");
		}

		member.updateLastLogin();
		String accessToken = jwtService.generateAccessToken(logInReq.email());
		String refreshToken = jwtService.generateRefreshToken();
		RefreshToken token = RefreshToken.builder()
			.refreshToken(refreshToken)
			.email(logInReq.email())
			.build();
		refreshTokenRepository.save(token);

		return LoginRes.toDto(accessToken, refreshToken);
	}

	public boolean isPasswordSame(String rawPassword, String memberPassword) {
		return passwordEncoder.matches(rawPassword, memberPassword);
	}
}
