package shop.jtoon.exception;

import lombok.Getter;
import shop.jtoon.type.ErrorStatus;

/**
 * 요청한 리소스가 존재하지 않을 때 발생하는 예외
 */
@Getter
public class NotFoundException extends RuntimeException {

	private final ErrorStatus errorStatus;

	public NotFoundException(ErrorStatus errorStatus) {
		super(errorStatus.getMessage());
		this.errorStatus = errorStatus;
	}
}
