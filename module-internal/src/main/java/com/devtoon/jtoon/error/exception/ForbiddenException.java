package com.devtoon.jtoon.error.exception;

/**
 * 인증된 사용자가 권한이 없는 리소스에 액세스하려고 할 때 발생하는 예외
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
