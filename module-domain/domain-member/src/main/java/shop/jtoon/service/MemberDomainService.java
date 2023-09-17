package shop.jtoon.service;

import static shop.jtoon.type.ErrorStatus.*;
import static shop.jtoon.util.SecurityConstant.*;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import shop.jtoon.dto.LoginDto;
import shop.jtoon.dto.SignUpDto;
import shop.jtoon.entity.LoginType;
import shop.jtoon.entity.Member;
import shop.jtoon.exception.DuplicatedException;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberDomainService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void createMember(SignUpDto signUpDto) {
		validateDuplicateEmail(signUpDto.email());
		String encryptedPassword = passwordEncoder.encode(signUpDto.password());
		Member member = signUpDto.toEntity(encryptedPassword);

		memberRepository.save(member);
	}

	@Transactional
	public void localLoginMember(LoginDto loginDto) {
		Member member = memberRepository.findByEmail(loginDto.email())
			.orElseThrow(() -> new BadCredentialsException(MEMBER_WRONG_LOGIN_INFO.getMessage()));

		if (!passwordEncoder.matches(loginDto.password(), member.getPassword())) {
			throw new BadCredentialsException(MEMBER_WRONG_LOGIN_INFO.getMessage());
		}

		if (!member.getLoginType().equals(LoginType.LOCAL)) {
			throw new BadCredentialsException(MEMBER_DUPLICATE_SOCIAL_LOGIN.getMessage());
		}

		member.updateLastLogin();
	}

	public void validateDuplicateEmail(String email) {
		if (memberRepository.findByEmail(email).isPresent()) {
			throw new DuplicatedException(MEMBER_EMAIL_CONFLICT);
		}
	}

	@Transactional
	public Member generateOrGetSocialMember(SignUpDto signUpDto) {
		Optional<Member> member = memberRepository.findByEmail(signUpDto.email());

		return member.orElseGet(() -> memberRepository.save(signUpDto.toEntity(BLANK)));
	}

	public Member findByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundException(MEMBER_EMAIL_CONFLICT));
	}
}
