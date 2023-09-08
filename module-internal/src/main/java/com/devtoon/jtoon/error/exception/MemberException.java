package com.devtoon.jtoon.error.exception;

import com.devtoon.jtoon.error.model.ErrorStatus;

public class MemberException extends RuntimeException {

    private ErrorStatus errorStatus;

    public MemberException(String message) {
        super(message);
    }

    public MemberException(ErrorStatus errorStatus) {
        this.errorStatus = errorStatus;
    }
}
