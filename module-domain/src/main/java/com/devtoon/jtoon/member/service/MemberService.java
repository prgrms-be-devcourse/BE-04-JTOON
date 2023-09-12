package com.devtoon.jtoon.member.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.error.exception.MemberException;
import com.devtoon.jtoon.error.exception.NotFoundException;
import com.devtoon.jtoon.error.model.ErrorStatus;
import com.devtoon.jtoon.global.common.MemberThreadLocal;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.entity.MemberCookie;
import com.devtoon.jtoon.member.repository.MemberRepository;
import com.devtoon.jtoon.member.request.SignUpReq;
import com.devtoon.jtoon.payment.repository.MemberCookieRepository;
import com.devtoon.jtoon.smtp.application.SmtpService;
import com.devtoon.jtoon.smtp.entity.Mail;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final SmtpService smtpService;
	private final MemberRepository memberRepository;
	private final MemberCookieRepository memberCookieRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void createMember(SignUpReq signUpReq) {
		validateDuplicateEmail(signUpReq.email());
		String encryptedPassword = encodePassword(signUpReq.password());
		Member member = signUpReq.toEntity(encryptedPassword);

		memberRepository.save(member);
	}

	public String sendEmailAuthentication(String email) {
		validateDuplicateEmail(email);
		UUID uuid = UUID.randomUUID();
		String randomUuid = uuid.toString().substring(0, 6);
		Mail mail = Mail.forAuthentication(email, randomUuid);
		smtpService.sendMail(mail);

		return randomUuid;
	}

	public int getCookieCount() {
		Member member = MemberThreadLocal.getMember();
		MemberCookie memberCookie = memberCookieRepository.findByMember(member)
			.orElseThrow(() -> new NotFoundException(ErrorStatus.COOKIE_MEMBER_NOT_FOUND));

		return memberCookie.getCookieCount();
	}

	private String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	private void validateDuplicateEmail(String email) {
		if (memberRepository.existsByEmail(email)) {
			throw new MemberException(ErrorStatus.MEMBER_EMAIL_CONFLICT);
		}
	}
}
