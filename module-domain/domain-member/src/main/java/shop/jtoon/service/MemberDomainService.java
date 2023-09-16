package shop.jtoon.service;

import static shop.jtoon.util.SecurityConstant.*;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import shop.jtoon.repository.MemberRepository;
import shop.jtoon.type.ErrorStatus;

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
			.orElseThrow(() -> new BadCredentialsException("너 안돼!"));

		if (!passwordEncoder.matches(loginDto.password(), member.getPassword())) {
			throw new BadCredentialsException("너 안돼!");
		}

		if (!member.getLoginType().equals(LoginType.LOCAL)) {
			throw new BadCredentialsException("소셜 로그인으로 등록된 아이디입니다.");
		}

		member.updateLastLogin();
	}

	public void validateDuplicateEmail(String email) {
		if (memberRepository.findByEmail(email).isPresent()) {
			throw new DuplicatedException(ErrorStatus.MEMBER_EMAIL_CONFLICT);
		}
	}

	@Transactional
	public Member generateOrGetSocialMember(SignUpDto signUpDto) {
		Optional<Member> member = memberRepository.findByEmail(signUpDto.email());

		return member.orElseGet(() -> memberRepository.save(signUpDto.toEntity(BLANK)));
	}

	public Member findByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException(ErrorStatus.MEMBER_EMAIL_CONFLICT.toString()));
	}
}
