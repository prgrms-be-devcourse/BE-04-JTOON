package shop.jtoon.global.error.exception;

import lombok.Getter;
import shop.jtoon.global.error.model.ErrorStatus;

/**
 * 이미 존재하는 리소스를 생성하려고 할 때 발생하는 예외
 */
@Getter
public class DuplicatedException extends RuntimeException {

	private final ErrorStatus errorStatus;

	public DuplicatedException(ErrorStatus errorStatus) {
		super(errorStatus.getMessage());
		this.errorStatus = errorStatus;
	}
}
