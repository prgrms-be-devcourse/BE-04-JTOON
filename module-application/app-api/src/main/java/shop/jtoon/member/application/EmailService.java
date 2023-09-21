package shop.jtoon.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import shop.jtoon.application.SmtpService;
import shop.jtoon.entity.Mail;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailService {

	private final SmtpService smtpService;

	public void sendEmailAuthentication(String email) {
		UUID uuid = UUID.randomUUID();
		String randomUuid = uuid.toString().substring(0, 6);

		smtpService.sendMail(Mail.forAuthentication(email, randomUuid));
	}
}
