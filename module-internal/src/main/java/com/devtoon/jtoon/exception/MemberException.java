package com.devtoon.jtoon.exception;

public class MemberException extends RuntimeException {

	private ExceptionCode exceptionCode;

	public MemberException(String message) {
		super(message);
	}

	public MemberException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
