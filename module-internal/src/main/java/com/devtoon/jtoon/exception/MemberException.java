package com.devtoon.jtoon.exception;

public class MemberException extends RuntimeException {

	private final ExceptionCode exceptionCode;

	public MemberException(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
