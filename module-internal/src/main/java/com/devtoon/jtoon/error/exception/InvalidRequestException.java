package com.devtoon.jtoon.error.exception;

/**
 * 클라이언트의 요청이 잘못되었을 때 발생하는 예외
 */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
