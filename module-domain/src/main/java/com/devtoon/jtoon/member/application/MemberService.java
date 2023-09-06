package com.devtoon.jtoon.member.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.exception.ExceptionCode;
import com.devtoon.jtoon.exception.MemberException;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.member.repository.MemberRepository;
import com.devtoon.jtoon.member.request.SignUpReq;
import com.devtoon.jtoon.smtp.application.SmtpService;
import com.devtoon.jtoon.smtp.entity.Mail;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final SmtpService smtpService;
	private final MemberRepository memberRepository;
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

	private String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	private void validateDuplicateEmail(String email) {
		if (memberRepository.existsByEmail(email)) {
			throw new MemberException(ExceptionCode.MEMBER_EMAIL_CONFLICT);
		}
	}
}
