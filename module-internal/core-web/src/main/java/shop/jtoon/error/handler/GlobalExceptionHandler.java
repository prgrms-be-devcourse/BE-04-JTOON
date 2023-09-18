package shop.jtoon.error.handler;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import shop.jtoon.error.model.ErrorResponse;
import shop.jtoon.exception.*;
import shop.jtoon.type.ErrorStatus;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final ErrorStatus error = ErrorStatus.COMMON_DTO_FIELD_INVALID_FORMAT;

        return makeResponseErrorFormat(error, e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullPointerException.class)
    public ErrorResponse handleNullPointerException(NullPointerException e) {
        return makeResponseErrorFormat(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IamportException.class)
    protected ErrorResponse handleIamportException(IamportException e) {
        return makeResponseErrorFormat(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    protected ErrorResponse handleNotFoundException(NotFoundException e) {
        return makeResponseErrorFormat(e.getErrorStatus());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidRequestException.class)
    protected ErrorResponse handleInvalidRequestException(InvalidRequestException e) {
        return makeResponseErrorFormat(e.getErrorStatus());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicatedException.class)
    protected ErrorResponse handleDuplicatedException(DuplicatedException e) {
        return makeResponseErrorFormat(e.getErrorStatus());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    protected ErrorResponse handleUnauthorizedException(UnauthorizedException e) {
        return makeResponseErrorFormat(e.getErrorStatus());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
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
