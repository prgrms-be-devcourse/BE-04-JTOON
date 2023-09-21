package shop.jtoon.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.jtoon.entity.Mail;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmtpService {

	private final JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String senderUsername;

	public void sendMail(Mail mail) {
		MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			message.setFrom(senderUsername);
			message.setTo(mail.getTo());
			message.setSubject(mail.getSubject());
			message.setText(mail.getText(), true);
		};
		javaMailSender.send(mimeMessagePreparator);
	}
}
