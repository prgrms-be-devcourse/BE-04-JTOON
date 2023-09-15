package shop.jtoon.config;

import jakarta.mail.Session;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MailSession {

	private String userName;
	private Session session;

	public MailSession(String userName, Session session) {
		this.userName = userName;
		this.session = session;
	}
}
