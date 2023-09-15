package shop.jtoon.dto;

import static shop.jtoon.util.RegExp.*;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import shop.jtoon.entity.CookieItem;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.PaymentInfo;

@Builder
public record PaymentDto(
	@NotBlank String impUid,
	@NotBlank String merchantUid,
	@NotBlank String payMethod,
	@NotBlank CookieItem cookieItem,
	@NotNull @DecimalMin("1") BigDecimal amount,
	@Pattern(regexp = EMAIL_PATTERN) String buyerEmail
) {

	public PaymentInfo toEntity(Member member) {
		return PaymentInfo.builder()
			.impUid(this.impUid)
			.merchantUid(this.merchantUid)
			.payMethod(this.payMethod)
			.cookieItem(this.cookieItem)
			.amount(this.amount)
			.member(member)
			.build();
	}
}
