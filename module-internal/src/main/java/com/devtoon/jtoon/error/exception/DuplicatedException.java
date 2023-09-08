package com.devtoon.jtoon.error.exception;


import com.devtoon.jtoon.error.model.ErrorStatus;

/**
 * 이미 존재하는 리소스를 생성하려고 할 때 발생하는 예외
 */
public class DuplicatedException extends RuntimeException {

    public DuplicatedException(String message) {
        super(message);
    }

    public DuplicatedException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
    }
}
