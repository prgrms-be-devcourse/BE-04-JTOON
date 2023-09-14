package shop.jtoon.global.error.exception;

import lombok.Getter;
import shop.jtoon.global.error.model.ErrorStatus;

/**
 * 인증되지 않은 사용자가 보호된 리소스에 액세스하려고 할 때 발생하는 예외
 */
@Getter
public class UnauthorizedException extends RuntimeException {

	private final ErrorStatus errorStatus;

	public UnauthorizedException(ErrorStatus errorStatus) {
		super(errorStatus.getMessage());
		this.errorStatus = errorStatus;
	}
}
