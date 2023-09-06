package com.devtoon.jtoon.smtp.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Mail {

	private static final String DEFAULT_SUBJECT = "[JTOON] 이메일 관련 인증입니다.";

	private String subject = DEFAULT_SUBJECT;
	private String to;
	private String text;

	@Builder
	private Mail(String subject, String to, String text) {
		this.subject = subject;
		this.to = to;
		this.text = text;
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
			.to(to)
			.text(text)
			.build();
	}
}
