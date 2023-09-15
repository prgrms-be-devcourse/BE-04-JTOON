package shop.jtoon.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import shop.jtoon.application.SmtpService;
import shop.jtoon.entity.Mail;
import shop.jtoon.member.request.SignUpReq;
import shop.jtoon.service.MemberDomainService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberApplicationService {

	private final MemberDomainService memberDomainService;
	private final SmtpService smtpService;

	@Transactional
	public void signUp(SignUpReq signUpReq) {
		memberDomainService.createMember(signUpReq.toDto());
	}

	public String sendEmailAuthentication(String email) {
		memberDomainService.validateDuplicateEmail(email);
		UUID uuid = UUID.randomUUID();
		String randomUuid = uuid.toString().substring(0, 6);
		Mail mail = Mail.forAuthentication(email, randomUuid);
		smtpService.sendMail(mail);

		return randomUuid;
	}
}
