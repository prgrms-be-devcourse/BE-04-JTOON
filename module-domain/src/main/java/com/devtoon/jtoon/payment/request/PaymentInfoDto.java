package com.devtoon.jtoon.payment.request;

import static com.devtoon.jtoon.global.util.RegExp.*;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.payment.entity.PG;
import com.devtoon.jtoon.payment.entity.PaymentInfo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PaymentInfoDto(
	@NotBlank String impUid,
	@NotBlank String merchantUid,
	@NotBlank String pg,
	@NotBlank String payMethod,
	@NotBlank String productName,
	@Min(1) int amount,
	@Pattern(regexp = EMAIL_PATTERN) String buyerEmail,
	@NotEmpty @Size(max = 10) String buyerName,
	@Pattern(regexp = PHONE_PATTERN) String buyerPhone
) {

	public PaymentInfo toEntity(Member member) {
		return PaymentInfo.builder()
			.impUid(this.impUid)
			.merchantUid(this.merchantUid)
			.pg(PG.from(this.pg))
			.payMethod(this.payMethod)
			.productName(this.productName)
			.amount(this.amount)
			.member(member)
			.build();
	}
}
