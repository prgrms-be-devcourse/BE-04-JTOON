package com.devtoon.jtoon.error.exception;

import com.devtoon.jtoon.error.model.ErrorStatus;
import lombok.Getter;

/**
 * 클라이언트의 요청이 잘못되었을 때 발생하는 예외
 */
@Getter
public class InvalidRequestException extends RuntimeException {

    private final ErrorStatus errorStatus;

    public InvalidRequestException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }
}
