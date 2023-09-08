package com.devtoon.jtoon.error.exception;

/**
 * 요청한 리소스가 존재하지 않을 때 발생하는 예외
 */
public class NotFoundException extends RuntimeException {


    public NotFoundException(String message) {
        super(message);
    }
}
