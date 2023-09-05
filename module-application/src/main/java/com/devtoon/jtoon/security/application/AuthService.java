package com.devtoon.jtoon.security.application;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.repository.MemberRepository;
import com.devtoon.jtoon.security.jwt.JwtProvider;
import com.devtoon.jtoon.security.request.LogInReq;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	@Transactional
	public String login(LogInReq logInReq) {
		Member member = memberRepository.findByEmail(logInReq.email()).orElseThrow(
			() -> new BadCredentialsException("너 안돼!")
		);

		if (!isPasswordSame(logInReq.password(), member.getPassword())) {
			throw new BadCredentialsException("너 안돼!");
		}

		member.updateLastLogin();
		return jwtProvider.generateToken(logInReq.email());
	}

	public boolean isPasswordSame(String rawPassword, String memberPassword) {
		return passwordEncoder.matches(rawPassword, memberPassword);
	}
}
