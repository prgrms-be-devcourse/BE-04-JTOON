package com.devtoon.jtoon.iamport.request;

import static com.devtoon.jtoon.global.util.RegExp.*;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record IamportReq(
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
}
