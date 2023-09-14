package shop.jtoon.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.jtoon.entity.Member;
import shop.jtoon.exception.DuplicatedException;
import shop.jtoon.repository.MemberRepository;
import shop.jtoon.request.SignUpDto;
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
		String encryptedPassword = encodePassword(signUpDto.password());
		Member member = signUpDto.toEntity(encryptedPassword);

		memberRepository.save(member);
	}

	public void validateDuplicateEmail(String email) {
		if (memberRepository.existsByEmail(email)) {
			throw new DuplicatedException(ErrorStatus.MEMBER_EMAIL_CONFLICT);
		}
	}

	private String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}
}
