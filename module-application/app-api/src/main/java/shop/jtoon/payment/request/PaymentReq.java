package shop.jtoon.payment.request;

import static shop.jtoon.util.RegExp.*;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import shop.jtoon.dto.PaymentDto;
import shop.jtoon.entity.CookieItem;

public record PaymentReq(
	@NotBlank String impUid,
	@NotBlank String merchantUid,
	@NotBlank String payMethod,
	@NotBlank String cookieItem,
	@NotNull @DecimalMin("1") BigDecimal amount,
	@Pattern(regexp = EMAIL_PATTERN) String buyerEmail,
	@NotBlank @Size(max = 10) String buyerName,
	@Pattern(regexp = PHONE_PATTERN) String buyerPhone
) {

	public PaymentDto toDto() {
		return PaymentDto.builder()
			.impUid(this.impUid)
			.merchantUid(this.merchantUid)
			.payMethod(this.payMethod)
			.cookieItem(CookieItem.from(this.cookieItem))
			.amount(this.amount)
			.buyerEmail(this.buyerEmail)
			.build();
	}
}
