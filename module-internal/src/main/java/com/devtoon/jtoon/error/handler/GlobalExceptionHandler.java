package com.devtoon.jtoon.error.handler;

import com.devtoon.jtoon.error.exception.*;
import com.devtoon.jtoon.error.model.ErrorResponse;
import com.devtoon.jtoon.error.model.ErrorStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final ErrorStatus error = ErrorStatus.COMMON_DTO_FIELD_INVALID_FORMAT;

        return makeResponseErrorFormat(error, e);
    }

    @ExceptionHandler(NullPointerException.class)
    public ErrorResponse handleNullPointerException(NullPointerException e) {
        return makeResponseErrorFormat(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    protected ErrorResponse handleNotFoundException(NotFoundException e) {
        return makeResponseErrorFormat(e.getErrorStatus());
    }

    @ExceptionHandler(InvalidRequestException.class)
    protected ErrorResponse handleInvalidRequestException(InvalidRequestException e) {
        return makeResponseErrorFormat(e.getErrorStatus());
    }

    @ExceptionHandler(DuplicatedException.class)
    protected ErrorResponse handleDuplicatedException(DuplicatedException e) {
        return makeResponseErrorFormat(e.getErrorStatus());
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ErrorResponse handleUnauthorizedException(UnauthorizedException e) {
        return makeResponseErrorFormat(e.getErrorStatus());
    }

    @ExceptionHandler(ForbiddenException.class)
    protected ErrorResponse handleForbiddenException(ForbiddenException e) {
        return makeResponseErrorFormat(e.getErrorStatus());
    }

    private ErrorResponse makeResponseErrorFormat(String message) {
        return new ErrorResponse(message, null);
    }

    private ErrorResponse makeResponseErrorFormat(ErrorStatus error) {
        return new ErrorResponse(error.getMessage(), null);
    }

    private ErrorResponse makeResponseErrorFormat(ErrorStatus error, BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult()
                .getFieldErrors();
        List<ErrorResponse.FieldErrorStatus> errors = fieldErrors.stream()
                .map(ErrorResponse.FieldErrorStatus::of)
                .toList();

        return new ErrorResponse(error.getMessage(), errors);
    }
}
