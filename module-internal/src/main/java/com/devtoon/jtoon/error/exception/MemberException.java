package com.devtoon.jtoon.error.exception;

import com.devtoon.jtoon.error.model.ExceptionCode;

public class MemberException extends RuntimeException {

    private ExceptionCode exceptionCode;

    public MemberException(String message) {
        super(message);
    }

    public MemberException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
