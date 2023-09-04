package com.devtoon.jtoon.paymentinfo.request;

import static com.devtoon.jtoon.global.util.RegExp.*;

import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.paymentinfo.entity.CookieItem;
import com.devtoon.jtoon.paymentinfo.entity.PG;
import com.devtoon.jtoon.paymentinfo.entity.PaymentInfo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PaymentInfoReq(
	@NotBlank String impUid,
	@NotBlank String merchantUid,
	@NotBlank String pg,
	@NotBlank String payMethod,
	@NotBlank String cookieItem,
	@Min(1) int amount,
	@Pattern(regexp = EMAIL_PATTERN) String buyerEmail,
	@NotBlank @Size(max = 10) String buyerName,
	@Pattern(regexp = PHONE_PATTERN) String buyerPhone
) {

	public PaymentInfo toEntity(Member member) {
		return PaymentInfo.builder()
			.impUid(this.impUid)
			.merchantUid(this.merchantUid)
			.pg(PG.from(this.pg))
			.payMethod(this.payMethod)
			.cookieItem(CookieItem.from(this.cookieItem))
			.amount(this.amount)
			.member(member)
			.build();
	}
}
