package com.devtoon.jtoon.smtp.entity;

import lombok.Getter;

@Getter
public class Mail {

	private static final String DEFAULT_SUBJECT = "[JTOON] 이메일 관련 인증입니다.";

	private String subject = DEFAULT_SUBJECT;
	private String to;
	private String text;

	private Mail(String to, String text) {
		this.to = to;
		this.text = text;
	}

	private Mail(String subject, String to, String text) {
		this(to, text);
		this.subject = subject;
	}

	public static Mail createEvent(String subject, String to, String text) {
		return new Mail(subject, to, text);
	}

	public static Mail createAuthentication(String to, String text) {
		return new Mail(to, text);
	}
}
