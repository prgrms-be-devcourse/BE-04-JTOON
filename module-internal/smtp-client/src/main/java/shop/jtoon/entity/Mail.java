package shop.jtoon.entity;

import static java.util.Objects.*;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Mail {

	public static final String DEFAULT_SUBJECT = "[JTOON] 이메일 관련 인증입니다.";

	private String subject;
	private String to;
	private String text;

	@Builder
	private Mail(String subject, String to, String text) {
		this.subject = requireNonNull(subject);
		this.to = requireNonNull(to);
		this.text = requireNonNull(text);
	}

	public static Mail forEvent(String subject, String to, String text) {
		return Mail.builder()
			.subject(subject)
			.to(to)
			.text(text)
			.build();
	}

	public static Mail forAuthentication(String to, String text) {
		return Mail.builder()
			.subject(DEFAULT_SUBJECT)
			.to(to)
			.text(text)
			.build();
	}
}
