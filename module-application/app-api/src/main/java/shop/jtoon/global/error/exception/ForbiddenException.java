package shop.jtoon.global.error.exception;

import lombok.Getter;
import shop.jtoon.global.error.model.ErrorStatus;

/**
 * 인증된 사용자가 권한이 없는 리소스에 액세스하려고 할 때 발생하는 예외
 */
@Getter
public class ForbiddenException extends RuntimeException {

	private final ErrorStatus errorStatus;

	public ForbiddenException(ErrorStatus errorStatus) {
		super(errorStatus.getMessage());
		this.errorStatus = errorStatus;
	}
}
