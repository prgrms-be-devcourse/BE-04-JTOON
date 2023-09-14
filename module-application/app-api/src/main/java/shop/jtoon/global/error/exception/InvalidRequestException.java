package shop.jtoon.global.error.exception;

import lombok.Getter;
import shop.jtoon.global.error.model.ErrorStatus;

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
