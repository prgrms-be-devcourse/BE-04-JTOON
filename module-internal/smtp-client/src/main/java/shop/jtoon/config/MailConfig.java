package shop.jtoon.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;

@Configuration
public class MailConfig {

	@Value("${smtp.username}")
	private String USERNAME;

	@Value("${smtp.password}")
	private String PASSWORD;

	@Value("${smtp.host}")
	private String SMTP_HOST;

	@Value("${smtp.port}")
	private Integer PORT;

	@Bean
	public MailSession initMailSession() {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", SMTP_HOST);
		props.put("mail.smtp.port", PORT);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust", SMTP_HOST);
		props.setProperty("mail.debug", "true");
		props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

		Session session = Session.getInstance(props,
			new Authenticator() {
				@Override
				protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(USERNAME, PASSWORD);
				}
			});

		return new MailSession(USERNAME, session);
	}
}
