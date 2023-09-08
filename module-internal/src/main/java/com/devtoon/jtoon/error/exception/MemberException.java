package com.devtoon.jtoon.error.exception;

import com.devtoon.jtoon.error.model.ExceptionStatus;

public class MemberException extends RuntimeException {

    private ExceptionStatus exceptionStatus;

    public MemberException(String message) {
        super(message);
    }

    public MemberException(ExceptionStatus exceptionStatus) {
        this.exceptionStatus = exceptionStatus;
    }
}
