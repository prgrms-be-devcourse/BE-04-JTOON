package shop.jtoon.application;

import org.springframework.stereotype.Service;

import jakarta.mail.Message;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import shop.jtoon.config.MailSession;
import shop.jtoon.entity.Mail;

@Service
@RequiredArgsConstructor
public class SmtpService {

	private final MailSession mailSession;

	public void sendMail(Mail mail) {
		try {
			Message message = new MimeMessage(mailSession.getSession());
			message.setFrom(new InternetAddress(mailSession.getUserName()));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getTo()));
			message.setSubject(mail.getSubject());
			message.setText(mail.getText());
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
